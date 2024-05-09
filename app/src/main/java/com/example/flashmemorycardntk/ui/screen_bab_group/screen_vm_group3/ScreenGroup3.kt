package com.example.flashmemorycardntk.ui.screen_bab_group.screen_vm_group3

import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.flashmemorycardntk.model.BaseButton
import com.example.flashmemorycardntk.model.BaseCardFrontNBack
import com.example.flashmemorycardntk.model.BaseRowGroupNameAndIcon
import com.example.flashmemorycardntk.model.EnumQuestionAnswer
import com.example.flashmemorycardntk.model.LevelChangeButton
import com.example.flashmemorycardntk.model.MainColumn
import com.example.flashmemorycardntk.model.SegmentedButtonRow
import com.example.flashmemorycardntk.model.VerticalSpacer
import com.example.flashmemorycardntk.model.concatenateTextsAtCursorPosition
import com.example.flashmemorycardntk.model.condicionalSpacerForLongScreen
import com.example.flashmemorycardntk.ui.theme.corDoTextoEscuroPadrao700
import com.example.flashmemorycardntk.ui.theme.corDoTextoEscuroPadrao900

@Composable
fun ScreenGroup3(
    paddingValues: PaddingValues,
    backScreen: () -> Unit
) {
    val vm: ViewModelGroup3 = hiltViewModel()
    val uiStateG3 by vm.group3State.collectAsState()

    val options = listOf(EnumQuestionAnswer.QUESTION, EnumQuestionAnswer.ANSWER)
    val selectedSegmentedButtonOption = uiStateG3.segmentedButtonStatus
    var isBigScreen by remember { mutableStateOf(false) }
    val selectedLevel = uiStateG3.levelSelected
    //uiStateG3.cardSelected é usado como chave para que, sempre que cardSelected for atualizado,
    //o remember seja invalidado e os valores de textInput sejam recalculados com os novos valores.
    val textInputQuestion = remember(uiStateG3.cardSelected) {
        mutableStateOf(TextFieldValue(uiStateG3.cardSelected?.question ?: "xxtq"))
    }
    val textInputAnswer = remember(uiStateG3.cardSelected) {
        mutableStateOf(TextFieldValue(uiStateG3.cardSelected?.answer ?: "xxta")) }

    val context = LocalContext.current
    val clipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val keyboardController = LocalSoftwareKeyboardController.current



    MainColumn(paddingValues = paddingValues) {
        condicionalSpacerForLongScreen().also { isBigScreen = it }
        BaseRowGroupNameAndIcon(
            text = uiStateG3.groupSelected?.name ?: "xxt01",
            textColor = corDoTextoEscuroPadrao900,
        )
        VerticalSpacer()
        SegmentedButtonRow(
            options = options,
            selectedOption = selectedSegmentedButtonOption,
            onClickX = { optionEnumQuestionAnswer ->
                vm.handleEvent(Group3Events.SegmentedButtonClicked(optionEnumQuestionAnswer))
            }
        )
        VerticalSpacer()
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "Edit Card",
            fontSize = 16.sp,
            color = corDoTextoEscuroPadrao700,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        VerticalSpacer(height = 4)
        when (uiStateG3.segmentedButtonStatus) {
            EnumQuestionAnswer.QUESTION -> {
                BaseCardFrontNBack(
                    textInput = textInputQuestion,
                    isDottedBackground = true,
                    isBigScreen = isBigScreen,
                    onClickClear = {
                        textInputQuestion.value = TextFieldValue("")
                    },
                    onClickPaste = {
                        concatenateTextsAtCursorPosition(
                            clipboardManager = clipboardManager,
                            textState = textInputQuestion
                        )
                    }
                )
            }

            EnumQuestionAnswer.ANSWER -> {
                BaseCardFrontNBack(
                    textInput = textInputAnswer,
                    isDottedBackground = false,
                    isBigScreen = isBigScreen,
                    onClickClear = {
                        textInputAnswer.value = TextFieldValue("")
                    },
                    onClickPaste = {
                        concatenateTextsAtCursorPosition(
                            clipboardManager = clipboardManager,
                            textState = textInputAnswer
                        )
                    }
                )
            }
        }

        VerticalSpacer(9)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            LevelChangeButton(
                selectedLevel = selectedLevel,
                onClickX = { scDifficulty ->
                    vm.handleEvent(Group3Events.ButtonLevelClicked(level = scDifficulty))
                }
            )
            Spacer(modifier = Modifier.weight(1f))
            BaseButton(
                text = "EDIT",
                enableButton = textInputQuestion.value.text.isNotEmpty()
                        && textInputAnswer.value.text.isNotEmpty(),
                onClick = {
                    keyboardController?.hide()
                    vm.handleEvent(
                        Group3Events.ButtonSaveClicked(
                            question = textInputQuestion.value.text,
                            answer = textInputAnswer.value.text,
                        )
                    )
                    backScreen()
                }
            )
        }
    }
}