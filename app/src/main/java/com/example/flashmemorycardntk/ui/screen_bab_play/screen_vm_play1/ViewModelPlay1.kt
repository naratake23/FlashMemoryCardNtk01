package com.example.flashmemorycardntk.ui.screen_bab_play.screen_vm_play1

import android.util.Log
import androidx.compose.runtime.mutableFloatStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashmemorycardntk.data.entities.CardEntity
import com.example.flashmemorycardntk.data.entities.GroupEntity
import com.example.flashmemorycardntk.data.entities.SequenceEntity
import com.example.flashmemorycardntk.data.repository.CardRepositoryImpl
import com.example.flashmemorycardntk.data.repository.CardUpdateServiceRepository
import com.example.flashmemorycardntk.data.repository.DataStoreRepository
import com.example.flashmemorycardntk.data.repository.GroupRepositoryImpl
import com.example.flashmemorycardntk.data.repository.SequenceRepositoryImpl
import com.example.flashmemorycardntk.model.EnumDSKeysLong
import com.example.flashmemorycardntk.model.EnumSortSequence
import com.example.flashmemorycardntk.model.SCDifficulty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelPlay1 @Inject constructor(
    private val groupRepo: GroupRepositoryImpl,
    private val cardRepo: CardRepositoryImpl,
    private val sequenceRepo: SequenceRepositoryImpl,
    private val dataStoreRepo: DataStoreRepository,
    private val cardUpdateRepo: CardUpdateServiceRepository
) : ViewModel() {

    private val _playState = MutableStateFlow(Play1Status())
    val playState = _playState.asStateFlow()

    init {
        viewModelScope.launch {
            val groupIdDS =
                dataStoreRepo.getLongValue(EnumDSKeysLong.GROUP_SELECTED_SHUFFLE.key).firstOrNull()
            processGroup(groupRepo.getGroupByIdRepo(groupIdDS ?: -1))
        }

        viewModelScope.launch {
            cardUpdateRepo.updateServiceComplete.collect {
                val groupIdDS =
                    dataStoreRepo.getLongValue(EnumDSKeysLong.GROUP_SELECTED_SHUFFLE.key)
                        .firstOrNull()
                processGroup(groupRepo.getGroupByIdRepo(groupIdDS ?: -1))
            }
        }

        viewModelScope.launch {
            cardUpdateRepo.updateServiceDeleteG1.collect {

                val currentGroupId = _playState.value.groupSelected?.groupId ?: -1
                val groupByIdRepo = groupRepo.getGroupByIdRepo(currentGroupId)
                val markedForDeletion = groupByIdRepo.isMarkedForDeletion
                if (markedForDeletion) {
                    processGroup(groupByIdRepo)
                }
            }
        }

    }

    private fun processGroup(groupSelected: GroupEntity?) {
        if (groupSelected == null || groupSelected.isMarkedForDeletion) {
            _playState.value = _playState.value.copy(cardList = emptyList(), isLoading = false)
            return
        }
        if (!_playState.value.isLoading) {
            loadCards(groupSelected)
        } else {
            viewModelScope.launch {
                val cardsOnSequence =
                    sequenceRepo.getSequencesListByGroupIdRepo(groupSelected.groupId).firstOrNull()
                val cardList = cardRepo.getCardListByGroupIdRepo(groupSelected.groupId)
                    .firstOrNull()
                    ?.filter { !it.isMarkedForDeletion && it.isMarkedForPlay }
                val cardListRecovered =
                    cardsOnSequence?.sortedBy { it.orderInGroup }?.mapNotNull { sequence ->
                        cardList?.find { card -> card.cardId == sequence.cardId }
                    }

                val completedCards =
                    dataStoreRepo.getLongValue(EnumDSKeysLong.PLAY_CURRENT_CARD.key).firstOrNull()
                val completedCardsCounter =
                    dataStoreRepo.getLongValue(EnumDSKeysLong.PLAY_TOTAL_CARD.key).firstOrNull()
                val correctCardsCounter =
                    dataStoreRepo.getLongValue(EnumDSKeysLong.PLAY_CORRECT_CARD.key).firstOrNull()

                _playState.value = _playState.value.copy(
                    groupSelected = groupSelected,
                    isInfinity = groupSelected.settingInfinite,
                    cardList = cardListRecovered ?: emptyList(),
                    cardsQty = cardListRecovered?.size ?: 0,
                    completedCards = completedCards?.toInt() ?: 0,
                    completedCardsCounter = completedCardsCounter?.toInt() ?: 1,
                    correctCardsCounter = correctCardsCounter?.toInt() ?: 0,
                    isLoading = false,
                    cardFrontSide = true
                )
            }
        }
    }

    private fun loadCards(group: GroupEntity) {
        viewModelScope.launch {
            val cardList: List<CardEntity>? =
                cardRepo.getCardListByGroupIdRepo(group.groupId)
                    .firstOrNull()
                    ?.filter { !it.isMarkedForDeletion && it.isMarkedForPlay }

            val cardlistGenerated = generateCardList(group = group, cards = cardList)
            val sequenceList = cardlistGenerated.mapIndexed { index, card ->
                SequenceEntity(
                    groupId = group.groupId,
                    cardId = card.cardId,
                    orderInGroup = index
                )
            }
            sequenceRepo.deleteAllSequencesRepo()
            sequenceRepo.insertSequenceListRepo(sequenceList)

            saveCountersInDS(0, 1, 0)

            _playState.value = _playState.value.copy(
                groupSelected = group,
                isInfinity = group.settingInfinite,
                cardList = cardlistGenerated,
                cardsQty = cardlistGenerated.size,
                completedCards = 0,
                completedCardsCounter = 1,
                correctCardsCounter = 0,
                isLoading = false,
                cardFrontSide = true
            )
        }
    }


    fun handleEvent(events: Play1Events) {
        when (events) {

            is Play1Events.DoubleClicked -> {
                flipCard()
            }

            is Play1Events.SwipedRight -> {
                _playState.value = _playState.value.copy(
                    correctCardsCounter = _playState.value.correctCardsCounter + 1
                )
                viewModelScope.launch {
                    cardRepo.incrementSuccessRateRepo(events.card.cardId)
                    completeCard()
                }
            }

            is Play1Events.SwipedLeft -> {
                viewModelScope.launch {
                    cardRepo.incrementErrorRateRepo(events.card.cardId)
                    completeCard()
                }
            }

            is Play1Events.RetakeButtonClicked -> {
                processGroup(_playState.value.groupSelected)
            }
        }
    }

    private fun flipCard() {
        _playState.value = _playState.value.copy(
            cardFrontSide = !playState.value.cardFrontSide,
            skipAnimation = false
        )
    }

    private fun completeCard() {
        _playState.value =
            _playState.value.copy(completedCardsCounter = _playState.value.completedCardsCounter + 1)

        if (_playState.value.completedCards + 1 < _playState.value.cardList.size) {
            fillStatusForCompleteCard()
            saveCountersInDS(
                _playState.value.completedCards,
                _playState.value.completedCardsCounter,
                _playState.value.correctCardsCounter,
            )
        } else {
            fillStatusForCompleteCard()
            checkAndRefreshCardsIfNeededOrRecalculate()
            saveCountersInDS(
                _playState.value.completedCards,
                _playState.value.completedCardsCounter,
                _playState.value.correctCardsCounter,
            )
        }
    }

    private fun fillStatusForCompleteCard() {
        _playState.value = _playState.value.copy(
            completedCards = _playState.value.completedCards + 1,
            cardFrontSide = true,
            skipAnimation = true, // Evita a animação automática após o swipe
            offsetX = mutableFloatStateOf(0f),
            offsetY = mutableFloatStateOf(0f)
        )
    }

    private fun generateCardList(group: GroupEntity, cards: List<CardEntity>?): List<CardEntity> {
        cards?.let {
            if (group.settingShuffle) {
                //garante um conjunto de elementos não repetidos para que o flatMap não retorne quantidades exponenciais
                val uniqueList = cards.toSet()
                // Separa os cartões por nível de dificuldade
                val easyCards =
                    uniqueList.filter { it.difficultyLevel == SCDifficulty.Level1.levelMode }
                val mediumCards =
                    uniqueList.filter { it.difficultyLevel == SCDifficulty.Level2.levelMode }
                val hardCards =
                    uniqueList.filter { it.difficultyLevel == SCDifficulty.Level3.levelMode }

                // Calcula o multiplicador para os cartões médios com base no balanceamento dos difíceis
                val mediumMultiplier = when (group.settingBalance) {
                    in 3..4 -> 2
                    else -> 3
                }

                // Multiplica os cartões médios e difíceis
                val multipliedMediumCards =
                    mediumCards.flatMap { card -> List(mediumMultiplier) { card } }
                val multipliedHardCards =
                    hardCards.flatMap { card -> List(group.settingBalance) { card } }

                // Combinam todas as listas
                val allCards = easyCards + multipliedMediumCards + multipliedHardCards

                // Embaralha os cartões
                return allCards.shuffled()
            } else {
                // Aplica a lógica de ordenação com base na configuração de sequência
                return when (group.settingSortSequence) {

                    EnumSortSequence.ORIGINAL.strEnum -> cards

                    EnumSortSequence.REVERSED.strEnum -> cards.reversed()

                    EnumSortSequence.EASY.strEnum -> cards.sortedBy { card ->
                        when (card.difficultyLevel) {
                            SCDifficulty.Level1.levelMode -> 1
                            SCDifficulty.Level2.levelMode -> 2
                            SCDifficulty.Level3.levelMode -> 3
                            else -> Int.MAX_VALUE
                        }
                    }

                    EnumSortSequence.HARD.strEnum -> cards.sortedBy { card ->
                        when (card.difficultyLevel) {
                            SCDifficulty.Level3.levelMode -> 1
                            SCDifficulty.Level2.levelMode -> 2
                            SCDifficulty.Level1.levelMode -> 3
                            else -> Int.MAX_VALUE
                        }
                    }

                    else -> cards
                }
            }
        } ?: return emptyList()
    }

    private fun checkAndRefreshCardsIfNeededOrRecalculate() {
        if (_playState.value.groupSelected!!.settingInfinite) {
            val originalCards = _playState.value.cardList
            if (_playState.value.groupSelected!!.settingShuffle) {
                // Gera uma nova lista embaralhada
                val newCardList = generateCardList(_playState.value.groupSelected!!, originalCards)
                _playState.value = _playState.value.copy(
                    cardList = newCardList,
                    completedCards = 0,
                    cardsQty = newCardList.size
                )
            } else {
                // Repete a lista atual
                _playState.value = _playState.value.copy(
                    completedCards = 0,
                )
            }
        } else {
            recalculateDifficultyLevel()
        }
    }

    private fun saveCountersInDS(currentCard: Int, totalCard: Int, correctCard: Int) {
        viewModelScope.launch {
            dataStoreRepo.saveLongValue(
                EnumDSKeysLong.PLAY_CURRENT_CARD.key,
                currentCard.toLong()
            )
            dataStoreRepo.saveLongValue(
                EnumDSKeysLong.PLAY_TOTAL_CARD.key,
                totalCard.toLong()
            )
            dataStoreRepo.saveLongValue(
                EnumDSKeysLong.PLAY_CORRECT_CARD.key,
                correctCard.toLong()
            )
        }
    }

    private fun recalculateDifficultyLevel() {
        viewModelScope.launch {
            val cardList =
                cardRepo.getCardListByGroupIdRepo(_playState.value.groupSelected?.groupId ?: 0)
                    .firstOrNull()
                    ?.filter { !it.isMarkedForDeletion && it.isMarkedForPlay }

            cardList?.let { cards ->
                val cardListForUpdate: MutableList<CardEntity> = mutableListOf()
                cards.forEach { cardEntity ->
                    if (cardEntity.successRate + cardEntity.errorRate >= 6) {
                        val totalAttempts =
                            cardEntity.successRate.toDouble() + cardEntity.errorRate.toDouble()
                        val successRateP = cardEntity.successRate / totalAttempts
                        val errorRateP = cardEntity.errorRate / totalAttempts

                        val updatedCard = when {
                            successRateP > 0.66 -> cardEntity.copy(difficultyLevel = SCDifficulty.Level1.levelMode)
                            errorRateP >= 0.66 -> cardEntity.copy(difficultyLevel = SCDifficulty.Level3.levelMode)
                            else -> cardEntity.copy(difficultyLevel = SCDifficulty.Level2.levelMode)
                        }
                        cardListForUpdate.add(updatedCard.copy(successRate = 0, errorRate = 0))
                    }
                }
                cardRepo.updateCardListRepo(cardListForUpdate)
            }
        }
    }
}