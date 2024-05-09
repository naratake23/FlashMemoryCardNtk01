package com.example.flashmemorycardntk.ui.screen_bab_shuffle.screen_vm_shuffle1

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashmemorycardntk.data.entities.GroupEntity
import com.example.flashmemorycardntk.data.repository.CardRepositoryImpl
import com.example.flashmemorycardntk.data.repository.CardUpdateServiceRepository
import com.example.flashmemorycardntk.data.repository.DataStoreRepository
import com.example.flashmemorycardntk.data.repository.GroupRepositoryImpl
import com.example.flashmemorycardntk.model.EnumDSKeysLong
import com.example.flashmemorycardntk.model.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelShuffle1 @Inject constructor(
    private val groupRepo: GroupRepositoryImpl,
    private val cardRepo: CardRepositoryImpl,
    private val dataStoreRepo: DataStoreRepository,
    private val cardUpdateRepo: CardUpdateServiceRepository
) : ViewModel() {

    private val _shuffleState = MutableStateFlow(Shuffle1Status())
    val shuffleState = _shuffleState.asStateFlow()

    private var fixSelected = false

    init {
        viewModelScope.launch {
            val groupSelectedDSLong = dataStoreRepo
                .getLongValue(EnumDSKeysLong.GROUP_SELECTED_SHUFFLE.key)
                .firstOrNull()
            groupRepo.getAllGroupsRepo().collect { listGroups ->
                val list = listGroups.filter { !it.isMarkedForDeletion }
                val groupSelectedInDialog = list.find { it.groupId == groupSelectedDSLong }
                    ?: if (list.isNotEmpty()) {
                        dataStoreRepo.saveLongValue(
                            EnumDSKeysLong.GROUP_SELECTED_SHUFFLE.key,
                            list.last().groupId
                        )
                        list.last()
                    } else GroupEntity(name = "EMPTY")
                _shuffleState.value = _shuffleState.value.copy(
                    groupList = list,
                    isEmptyGroupList = list.isEmpty(),
                )
                if (!fixSelected) fillSetting(groupSelectedInDialog)
                fixSelected = true
            }
        }
    }


    fun handleEvent(events: Shuffle1Events) {
        when (events) {
            is Shuffle1Events.ButtonDropDownDialogGroupClicked -> {
                _shuffleState.value = _shuffleState.value.copy(showDialogGroup = true)
            }

            is Shuffle1Events.CancelDialogs -> {
                _shuffleState.value = _shuffleState.value.copy(
                    showDialogGroup = false,
                    showDialogOrder = false
                )
            }

            is Shuffle1Events.DropDownDialogGroupItemSelected -> {
                fillSetting(events.groupSelected)
            }

            is Shuffle1Events.InfinitySwitchClicked -> {
                _shuffleState.value = _shuffleState.value.copy(infinitySwitch = events.isSelected)
            }

            is Shuffle1Events.ButtonDropDownDialogOrderClicked -> {
                _shuffleState.value = _shuffleState.value.copy(showDialogOrder = true)
            }

            is Shuffle1Events.DropDownDialogOrderItemSelected -> {
                _shuffleState.value =
                    _shuffleState.value.copy(orderSelectedInDialog = events.orderSelected.strEnum)
            }

            is Shuffle1Events.ShuffleSwitchClicked -> {
                _shuffleState.value = _shuffleState.value.copy(shuffleSwitch = events.isSelected)
            }

            is Shuffle1Events.BalanceSliderClicked -> {
                _shuffleState.value = _shuffleState.value.copy(balanceSlider = events.ratio)
            }

            is Shuffle1Events.ButtonApplyClicked -> {
                shuffleState.value.groupSelectedInDialog?.let { groupSelected ->
                    updateCardsForPlay(groupSelected)
                    updateGroup(
                        groupEntity = groupSelected.copy(
                            settingInfinite = shuffleState.value.infinitySwitch,
                            settingSortSequence = shuffleState.value.orderSelectedInDialog,
                            settingShuffle = shuffleState.value.shuffleSwitch,
                            settingBalance = shuffleState.value.balanceSlider,
                        )
                    )
                    saveLongDataStore(groupSelected.groupId)
                }
                viewModelScope.launch {
                    SnackbarManager.showGreenMessage("Changes have been successfully applied")
                }
            }
        }
    }

    private fun updateCardsForPlay(groupEntity: GroupEntity) {
        viewModelScope.launch {
            cardRepo.getCardListByGroupIdRepo(groupEntity.groupId).firstOrNull()?.let { cardList ->
                cardRepo.updateCardListRepo(cardList.map { it.copy(isMarkedForPlay = true) })
                cardUpdateRepo.updateServiceCards()
            }
        }
    }

    private fun saveLongDataStore(value: Long) {
        viewModelScope.launch {
            val currentDSLongValue =
                dataStoreRepo.getLongValue(EnumDSKeysLong.GROUP_SELECTED_SHUFFLE.key).firstOrNull()
            if (currentDSLongValue != value) {
                dataStoreRepo.saveLongValue(EnumDSKeysLong.GROUP_SELECTED_SHUFFLE.key, value)
            }
        }
    }

    private fun fillSetting(groupEntity: GroupEntity) {
        _shuffleState.value = _shuffleState.value.copy(
            groupSelectedInDialog = groupEntity,
            infinitySwitch = groupEntity.settingInfinite,
            orderSelectedInDialog = groupEntity.settingSortSequence,
            shuffleSwitch = groupEntity.settingShuffle,
            balanceSlider = groupEntity.settingBalance
        )
    }

    private fun updateGroup(groupEntity: GroupEntity) {
        viewModelScope.launch {
            groupRepo.updateGroupRepo(groupEntity)
        }
    }
}