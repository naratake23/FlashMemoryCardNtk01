package com.example.flashmemorycardntk.ui.screen_bab_group.screen_vm_group3

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashmemorycardntk.data.entities.CardEntity
import com.example.flashmemorycardntk.data.repository.CardRepositoryImpl
import com.example.flashmemorycardntk.data.repository.GroupRepositoryImpl
import com.example.flashmemorycardntk.model.EnumQuestionAnswer
import com.example.flashmemorycardntk.model.SCDifficulty
import com.example.flashmemorycardntk.model.SnackbarManager
import com.example.flashmemorycardntk.model.SnackbarMessage
import com.example.flashmemorycardntk.model.getSCDifficultyFromString
import com.example.flashmemorycardntk.ui.theme.levelGreen800
import com.example.flashmemorycardntk.ui.theme.swipeGreenColor50
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelGroup3 @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val groupRepo: GroupRepositoryImpl,
    private val cardRepo: CardRepositoryImpl
) : ViewModel() {

    private val groupId = savedStateHandle.get<Long>("groupId")
    private val cardId = savedStateHandle.get<Long>("cardId")
    private val isFrontSide = savedStateHandle.get<Boolean>("isFrontSide")

    private val _group3State = MutableStateFlow(Group3Status())
    val group3State = _group3State.asStateFlow()

    init {
        loadGroupAndCard()
    }

    private fun loadGroupAndCard() {
        if (cardId != null && groupId != null && isFrontSide != null) {

            viewModelScope.launch {
                val cardSelected = cardRepo.getCardByIdRepo(cardId = cardId)
                val groupSelected = groupRepo.getGroupByIdRepo(groupId = groupId)
                val buttonStatus =
                    if (isFrontSide) EnumQuestionAnswer.QUESTION else EnumQuestionAnswer.ANSWER

                _group3State.value = _group3State.value.copy(
                    cardSelected = cardSelected,
                    groupSelected = groupSelected,
                    segmentedButtonStatus = buttonStatus,
                    levelSelected = getSCDifficultyFromString(cardSelected.difficultyLevel)
                        ?: SCDifficulty.Level1
                )
            }
        }
    }

    fun handleEvent(events: Group3Events) {
        when (events) {

            is Group3Events.SegmentedButtonClicked -> {
                _group3State.value = _group3State.value.copy(segmentedButtonStatus = events.toggle)
            }

            is Group3Events.ButtonLevelClicked -> {
                _group3State.value = _group3State.value.copy(levelSelected = events.level)
            }

            is Group3Events.ButtonSaveClicked -> {
                if (_group3State.value.cardSelected!!.difficultyLevel != _group3State.value.levelSelected.levelMode) {
                    updateCard(
                        group3State.value.cardSelected!!.copy(
                            question = events.question,
                            answer = events.answer,
                            difficultyLevel = group3State.value.levelSelected.levelMode,
                            successRate = 0,
                            errorRate = 0,
                        )
                    )
                } else {
                    updateCard(
                        group3State.value.cardSelected!!.copy(
                            question = events.question,
                            answer = events.answer,
                            difficultyLevel = group3State.value.levelSelected.levelMode
                        )
                    )
                }
                showMessageSnackbar(
                    message = SnackbarMessage(
                        text = "Card has been successfully updated",
                        backgroundColor = swipeGreenColor50,
                        textColor = levelGreen800
                    )
                )
            }
        }
    }

    private fun updateCard(editedCard: CardEntity) {
        viewModelScope.launch {
            cardRepo.updateCardRepo(editedCard)
        }
    }

    fun showMessageSnackbar(message: SnackbarMessage) {
        viewModelScope.launch {
            SnackbarManager.showMessage(message = message)
        }
    }

}