package com.example.flashmemorycardntk.ui.screen_bab_fab.screen_vm_fab1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashmemorycardntk.data.entities.CardEntity
import com.example.flashmemorycardntk.data.entities.GroupEntity
import com.example.flashmemorycardntk.data.repository.CardRepositoryImpl
import com.example.flashmemorycardntk.data.repository.DataStoreRepository
import com.example.flashmemorycardntk.data.repository.GroupRepositoryImpl
import com.example.flashmemorycardntk.model.EnumDSKeysLong
import com.example.flashmemorycardntk.model.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelFab1 @Inject constructor(
    private val groupRepo: GroupRepositoryImpl,
    private val cardRepo: CardRepositoryImpl,
    private val dataStoreRepo: DataStoreRepository
) : ViewModel() {

    private val _fabState = MutableStateFlow(Fab1Status())
    val fabState: StateFlow<Fab1Status> = _fabState.asStateFlow()

    init {
        loadGroup()
    }

    private fun loadGroup() {
        viewModelScope.launch {
            val groupSelectedDSLong =
                dataStoreRepo.getLongValue(EnumDSKeysLong.GROUP_SELECTED_FAB.key)
                    .firstOrNull()
            groupRepo.getAllGroupsRepo().collect(collector = { listGroups ->
                val list = listGroups.filter { !it.isMarkedForDeletion }
                _fabState.value = _fabState.value.copy(
                    groupList = list,
                    isEmptyGroupList = list.isEmpty(),
                    groupSelectedInDialog = list.find { it.groupId == groupSelectedDSLong }
                        ?: if (list.isNotEmpty()) list.last()
                        else GroupEntity(name = "EMPTY")
                )
            })
        }
    }

    fun handleEvent(events: Fab1Events) {
        when (events) {
            is Fab1Events.ButtonDropDownDialogClicked -> {
                _fabState.value = _fabState.value.copy(showDialog = true)
            }

            is Fab1Events.CancelDialog -> {
                _fabState.value = _fabState.value.copy(showDialog = false)
            }

            is Fab1Events.DropDownDialogItemSelected -> {
                _fabState.value = _fabState.value.copy(
                    groupSelectedInDialog = events.groupSelected,
                    showDialog = false)
                saveLongDataStore(events.groupSelected.groupId)
            }

            is Fab1Events.SegmentedButtonClicked -> {
                _fabState.value = _fabState.value.copy(segmentedButtonStatus = events.toggle)
            }

            is Fab1Events.ButtonLevelClicked -> {
                _fabState.value = _fabState.value.copy(levelSelected = events.level)
            }

            is Fab1Events.ButtonSaveClicked -> {
                saveCard(events.cardEntity)
                viewModelScope.launch {
                    SnackbarManager.showGreenMessage("Card has been successfully saved")
                }
            }
        }
    }

    private fun saveCard(newCard: CardEntity) {
        viewModelScope.launch {
            cardRepo.insertCardRepo(newCard)
        }
    }

    private fun saveLongDataStore(value: Long){
        viewModelScope.launch {
            dataStoreRepo.saveLongValue(EnumDSKeysLong.GROUP_SELECTED_FAB.key, value)
        }
    }
}