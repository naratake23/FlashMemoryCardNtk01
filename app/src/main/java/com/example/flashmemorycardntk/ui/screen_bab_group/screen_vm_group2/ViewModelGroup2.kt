package com.example.flashmemorycardntk.ui.screen_bab_group.screen_vm_group2

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashmemorycardntk.data.entities.CardEntity
import com.example.flashmemorycardntk.data.entities.GroupEntity
import com.example.flashmemorycardntk.data.repository.CardRepositoryImpl
import com.example.flashmemorycardntk.data.repository.GroupRepositoryImpl
import com.example.flashmemorycardntk.model.EnumSnackbarActionScreens
import com.example.flashmemorycardntk.model.GlobalProperties
import com.example.flashmemorycardntk.model.SnackbarManager
import com.example.flashmemorycardntk.model.SnackbarMessage
import com.example.flashmemorycardntk.model.extractQuestionsAndAnswersRegex
import com.example.flashmemorycardntk.model.limitStringWithEllipses
import com.example.flashmemorycardntk.ui.theme.fabRed400
import com.example.flashmemorycardntk.ui.theme.levelGreen800
import com.example.flashmemorycardntk.ui.theme.swipeGreenColor50
import com.example.flashmemorycardntk.ui.theme.vermelhoFundoSnackbar
import com.example.flashmemorycardntk.ui.theme.vermelhotextoSnackbar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelGroup2 @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val groupRepo: GroupRepositoryImpl,
    private val cardRepo: CardRepositoryImpl
) : ViewModel() {

    private val groupId: Long? = savedStateHandle.get<Long>("groupId")
    private var groupEntityOn: GroupEntity? = null

    private val _group2State = MutableStateFlow(Group2Status())
    val group2State = _group2State.asStateFlow()

    private var deletionJob: Job? = null

    init {
        loadGroupName()
        loadCardList()
    }

    private fun loadGroupName() {
        viewModelScope.launch {
            groupId?.let { groupId ->
                groupEntityOn = groupRepo.getGroupByIdRepo(groupId)
                _group2State.value = _group2State.value.copy(
                    groupName = groupEntityOn!!.name,
                )
            }
        }
    }

    private fun loadCardList() {
        viewModelScope.launch {
            groupId?.let { groupId ->
                cardRepo.getCardListByGroupIdRepo(groupId).collect { cardList ->
                    _group2State.value = _group2State.value.copy(
                        cardList = cardList.filter { !it.isMarkedForDeletion },
                    )
                }
            }
        }
    }

    fun handleEvent(event: Group2Events) {
        when (event) {
            is Group2Events.ButtonEditGroupNameClicked -> {
                _group2State.value = _group2State.value.copy(showDialogEditGroupName = true)
            }

            is Group2Events.ButtonDialogEditNameClicked -> {
                updateGroupName(event.groupNewName)
                _group2State.value = _group2State.value.copy(
                    showDialogEditGroupName = false,
                    groupName = event.groupNewName
                )
                showMessageSnackbar(
                    message = SnackbarMessage(
                        text = "Name change successful for  '${event.groupNewName.limitStringWithEllipses()}'",
                        backgroundColor = swipeGreenColor50,
                        textColor = levelGreen800
                    )
                )
            }

            is Group2Events.CancelDialog -> {
                _group2State.value = _group2State.value.copy(
                    showDialogEditGroupName = false
                )
            }

            is Group2Events.ButtonDeleteClicked -> {
                GlobalProperties.cardsSelected = _group2State.value.selectedItems.toList()

                updateSelectedItems(
                    cardsToUpdate = GlobalProperties.cardsSelected,
                    isMarkedForDelete = true
                )

                _group2State.value = _group2State.value.copy(
                    selectedItems = emptySet(),
                    selectionMode = false
                )

                showMessageSnackbar(
                    message = SnackbarMessage(
                        text = "${GlobalProperties.cardsSelected.size} cards have been deleted",
                        backgroundColor = vermelhoFundoSnackbar,
                        textColor = vermelhotextoSnackbar,
                        actionText = "UNDO",
                        actionTextColor = fabRed400,
                        duration = SnackbarDuration.Short,
                        screenSource = EnumSnackbarActionScreens.GROUP2
                    )
                )

                deleteSelectedItems()
                loadCardList()
            }

            is Group2Events.ButtonSaveMultipleCardsClicked -> {
                groupEntityOn?.let { group ->
                    val lists = extractQuestionsAndAnswersRegex(event.text, group.groupId)
                    if (lists.isNotEmpty()) {
                        saveMultipleCards(lists)
                        showMessageSnackbar(
                            message = SnackbarMessage(
                                text = "Cards have been successfully saved",
                                backgroundColor = swipeGreenColor50,
                                textColor = levelGreen800
                            )
                        )
                    }
                }
            }
        }
    }

    private fun updateGroupName(newName: String) {
        viewModelScope.launch {
            groupRepo.updateGroupRepo(groupEntityOn!!.copy(name = newName))
        }
    }

    private fun showMessageSnackbar(message: SnackbarMessage) {
        viewModelScope.launch {
            SnackbarManager.showMessage(message)
        }
    }

    fun enterSelectionMode() {
        _group2State.value = _group2State.value.copy(selectionMode = true)
    }

    fun toggleItemSelection(item: CardEntity) {
        val currentSelection = _group2State.value.selectedItems.toMutableSet()
        if (item in currentSelection) {
            currentSelection.remove(item)
        } else {
            currentSelection.add(item)
        }
        _group2State.value = _group2State.value.copy(selectedItems = currentSelection)
        if (currentSelection.isEmpty()) {
            _group2State.value = _group2State.value.copy(selectionMode = false)
        }
    }

    private fun updateSelectedItems(cardsToUpdate: List<CardEntity>, isMarkedForDelete: Boolean) {
        viewModelScope.launch {
            cardRepo.updateCardListRepo(cardsToUpdate.map { it.copy(isMarkedForDeletion = isMarkedForDelete) })
        }
    }

    fun deleteSelectedItems() {
        deletionJob?.cancel() // Cancela qualquer deleção anterior
        deletionJob = viewModelScope.launch {
            delay(5000) // Espera o tempo do Snackbar
            val cardList = cardRepo.getCardListByGroupIdRepo(groupId = groupId!!).firstOrNull()
            cardList?.let { list ->
                cardRepo.deleteCardListRepo(list.filter { it.isMarkedForDeletion })
            }
        }
    }

    fun onUndoSnackbarActionG2() {
        updateSelectedItems(
            cardsToUpdate = GlobalProperties.cardsSelected,
            isMarkedForDelete = false
        )
        loadCardList()
    }

    private fun saveMultipleCards(cardList: List<CardEntity>) {
        viewModelScope.launch {
            cardRepo.insertCardListRepo(cardList)
        }
    }
}