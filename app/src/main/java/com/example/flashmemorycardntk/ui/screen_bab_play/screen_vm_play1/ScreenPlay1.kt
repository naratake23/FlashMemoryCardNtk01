package com.example.flashmemorycardntk.ui.screen_bab_play.screen_vm_play1

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.flashmemorycardntk.model.BackCardContentPlayScreen
import com.example.flashmemorycardntk.model.CardForPlayScreen
import com.example.flashmemorycardntk.model.EmptyCardContentPlayScreen
import com.example.flashmemorycardntk.model.EnumQuestionAnswer
import com.example.flashmemorycardntk.model.FinishPlayScreen
import com.example.flashmemorycardntk.model.FrontCardContentPlayScreen
import com.example.flashmemorycardntk.model.MainColumn
import com.example.flashmemorycardntk.model.SCDifficulty
import com.example.flashmemorycardntk.model.StatusRowForPlayScreen
import com.example.flashmemorycardntk.model.VerticalSpacer
import com.example.flashmemorycardntk.model.getSCDifficultyFromString
import com.example.flashmemorycardntk.ui.theme.levelYellow
import com.example.flashmemorycardntk.ui.theme.swipeGreenColor100
import com.example.flashmemorycardntk.ui.theme.swipeRedColor100
import kotlin.math.min


@Composable
fun ScreenPlay1(
    paddingValues: PaddingValues
) {
    val vm: ViewModelPlay1 = hiltViewModel()
    val uiStatePlay by vm.playState.collectAsState()

    val backgroundColor by animateColorAsState(
        targetValue = when {
            uiStatePlay.offsetX.floatValue > 0 -> swipeGreenColor100.copy(
                alpha = min(
                    a = 1f,
                    b = uiStatePlay.offsetX.floatValue / 300f
                )
            )

            uiStatePlay.offsetX.floatValue < 0 -> swipeRedColor100.copy(
                alpha = min(
                    a = 1f,
                    b = -uiStatePlay.offsetX.floatValue / 300f
                )
            )

            else -> Color.Transparent
        }, label = "Right and Wrong backgroundColor"
    )

    MainColumn(
        paddingValues = paddingValues,
        color = backgroundColor
    ) {

        VerticalSpacer(9)

        if (uiStatePlay.isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.weight(1f)
            ) {
                CircularProgressIndicator(color = levelYellow)
            }
        } else if (uiStatePlay.cardList.isNotEmpty() && uiStatePlay.completedCards < uiStatePlay.cardList.size) {

            val currentCard = uiStatePlay.cardList[uiStatePlay.completedCards]

            StatusRowForPlayScreen(
                questionAnswerLabel = if (uiStatePlay.cardFrontSide) EnumQuestionAnswer.QUESTION.str
                else EnumQuestionAnswer.ANSWER.str,
                cardCounter = uiStatePlay.completedCardsCounter,
                totalNumbersOfCards = if (!uiStatePlay.isInfinity) uiStatePlay.cardsQty.toString() else "-",
                selectedLevel = getSCDifficultyFromString(currentCard.difficultyLevel)
                    ?: SCDifficulty.Level1
            )

            VerticalSpacer(3)

            CardForPlayScreen(
                flipped = uiStatePlay.cardFrontSide,
                skipAnimation = uiStatePlay.skipAnimation,
                offsetX = uiStatePlay.offsetX,
                offsetY = uiStatePlay.offsetY,
                frontCard = {
                    FrontCardContentPlayScreen(stringContent = currentCard.question)
                },
                backCard = {
                    BackCardContentPlayScreen(
                        modifier = it,
                        stringContent = currentCard.answer
                    )
                },
                doubleClickX = {
                    vm.handleEvent(Play1Events.DoubleClicked)
                },
                swipeRightX = {
                    vm.handleEvent(Play1Events.SwipedRight(currentCard))
                },
                swipeLeftX = {
                    vm.handleEvent(Play1Events.SwipedLeft(currentCard))
                }
            )
        } else if (uiStatePlay.cardList.isEmpty() || uiStatePlay.groupSelected == null) {
            EmptyCardContentPlayScreen()
        } else {
            FinishPlayScreen(
                cardsCorrect = uiStatePlay.correctCardsCounter,
                totalNumbersOfCards = uiStatePlay.cardList.size,
                onClickX = {
                    vm.handleEvent(Play1Events.RetakeButtonClicked)
                })
        }
        VerticalSpacer(3)
    }
}