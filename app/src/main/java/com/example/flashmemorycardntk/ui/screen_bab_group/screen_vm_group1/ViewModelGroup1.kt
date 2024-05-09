package com.example.flashmemorycardntk.ui.screen_bab_group.screen_vm_group1

import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashmemorycardntk.data.entities.GroupEntity
import com.example.flashmemorycardntk.data.repository.CardUpdateServiceRepository
import com.example.flashmemorycardntk.data.repository.DataStoreRepository
import com.example.flashmemorycardntk.data.repository.GroupRepositoryImpl
import com.example.flashmemorycardntk.model.EnumDSKeysLong
import com.example.flashmemorycardntk.model.EnumSnackbarActionScreens
import com.example.flashmemorycardntk.model.GlobalProperties
import com.example.flashmemorycardntk.model.SnackbarManager
import com.example.flashmemorycardntk.model.SnackbarMessage
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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelGroup1 @Inject constructor(
    private val groupRepo: GroupRepositoryImpl,
    private val dataStoreRepo: DataStoreRepository,
    private val cardUpdateRepo: CardUpdateServiceRepository
) : ViewModel() {

    private val _groupState = MutableStateFlow(Group1Status())
    val groupState: StateFlow<Group1Status> = _groupState.asStateFlow()

    private var deletionJob: Job? = null

    init {
        loadGroup()
    }

    private fun loadGroup() {
        viewModelScope.launch {
            groupRepo.getGroupsWithCardCountRepo().collect(collector = { listGroups ->
                val groupsNotMarkedForDeletion = listGroups.filter { !it.group.isMarkedForDeletion }
                _groupState.value = _groupState.value.copy(
                    groupList = groupsNotMarkedForDeletion,
                    groupSize = groupsNotMarkedForDeletion.size
                )
            })
        }
    }

    fun handleEvent(event: Group1Events) {
        when (event) {
            is Group1Events.ButtonAddClicked -> {
                _groupState.value = _groupState.value.copy(showDialog = true)
            }

            is Group1Events.CancelDialog -> {
                _groupState.value = _groupState.value.copy(showDialog = false)
            }

            is Group1Events.ButtonSaveClicked -> {
                saveGroup(event.groupName)
                loadGroup()
                _groupState.value = _groupState.value.copy(showDialog = false)

                saveDataStore(event)

                showMessageSnackbar(
                    message = SnackbarMessage(
                        text = "Group  '${event.groupName.limitStringWithEllipses()}'  saved",
                        backgroundColor = swipeGreenColor50,
                        textColor = levelGreen800
                    )
                )
            }

            is Group1Events.ButtonDeleteClicked -> {

                GlobalProperties.groupsSelected = _groupState.value.selectedItems.toList()

                updateSelectedItems(
                    groupsToUpdate = GlobalProperties.groupsSelected,
                    isMarkedForDelete = true
                )

                _groupState.value = _groupState.value.copy(
                    selectedItems = emptySet(),
                    selectionMode = false,
                )

                showMessageSnackbar(
                    message = SnackbarMessage(
                        text = "${GlobalProperties.groupsSelected.size} groups have been deleted",
                        backgroundColor = vermelhoFundoSnackbar,
                        textColor = vermelhotextoSnackbar,
                        actionText = "UNDO",
                        actionTextColor = fabRed400,
                        duration = SnackbarDuration.Short,
                        screenSource = EnumSnackbarActionScreens.GROUP1
                    )
                )

                deleteSelectedItems()
                loadGroup()
            }
        }
    }

    private fun saveDataStore(event: Group1Events.ButtonSaveClicked) {
        viewModelScope.launch {
            val list = groupRepo.getAllGroupsRepo().firstOrNull()
            val group = list?.find { it.name == event.groupName }
            group?.let {
                dataStoreRepo.saveLongValue(
                    EnumDSKeysLong.GROUP_SELECTED_FAB.key,
                    it.groupId
                )
            }
        }
    }

    private fun saveGroup(groupName: String) {
        viewModelScope.launch {
            val newGroup = GroupEntity(name = groupName)
            groupRepo.insertGroupRepo(newGroup)
        }
    }

    private fun showMessageSnackbar(message: SnackbarMessage) {
        viewModelScope.launch {
            SnackbarManager.showMessage(message = message)
        }
    }

    fun enterSelectionMode() {
        _groupState.value = _groupState.value.copy(selectionMode = true)
    }

    fun toggleItemSelection(item: GroupEntity) {
        val currentSelection = _groupState.value.selectedItems.toMutableSet()
        if (item in currentSelection) {
            currentSelection.remove(item)
        } else {
            currentSelection.add(item)
        }
        _groupState.value = _groupState.value.copy(selectedItems = currentSelection)
        if (currentSelection.isEmpty()) {
            _groupState.value = _groupState.value.copy(selectionMode = false)
        }
    }

    fun deleteSelectedItems() {
        deletionJob?.cancel()
        deletionJob = viewModelScope.launch {
            delay(5000) // Espera o tempo do Snackbar
            val groupList = groupRepo.getAllGroupsRepo().firstOrNull()
            groupList?.let { list ->
                groupRepo.deleteGroupListRepo(list.filter { it.isMarkedForDeletion })
            }
        }
    }

    private fun updateSelectedItems(groupsToUpdate: List<GroupEntity>, isMarkedForDelete: Boolean) {
        viewModelScope.launch {
            groupRepo.updateGroupListRepo(groupsToUpdate.map { it.copy(isMarkedForDeletion = isMarkedForDelete) })
            cardUpdateRepo.updateServiceDeleteG1()
        }
    }

    //optei por chamar a função fora do ciclo de vida da screen para que o Action do snackbar funcione em qualquer lugar do codigo e não só na screen que deu origem
    fun onUndoSnackbarActionG1() {
        updateSelectedItems(
            groupsToUpdate = GlobalProperties.groupsSelected,
            isMarkedForDelete = false
        )
        loadGroup()
    }
}