package com.example.flashmemorycardntk.ui.screen_bab_fab.screen_vm_fab1

import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ripple.rememberRipple
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.flashmemorycardntk.R
import com.example.flashmemorycardntk.data.entities.CardEntity
import com.example.flashmemorycardntk.model.BaseBackgroundBoxForG1
import com.example.flashmemorycardntk.model.BaseButton
import com.example.flashmemorycardntk.model.BaseCardFrontNBack
import com.example.flashmemorycardntk.model.BaseDialog
import com.example.flashmemorycardntk.model.BaseLazyColumn
import com.example.flashmemorycardntk.model.BaseRowGroupNameAndIcon
import com.example.flashmemorycardntk.model.BoxForIcon
import com.example.flashmemorycardntk.model.EnumQuestionAnswer
import com.example.flashmemorycardntk.model.LevelChangeButton
import com.example.flashmemorycardntk.model.MainColumn
import com.example.flashmemorycardntk.model.SCDifficulty
import com.example.flashmemorycardntk.model.SegmentedButtonRow
import com.example.flashmemorycardntk.model.VerticalSpacer
import com.example.flashmemorycardntk.model.concatenateTextsAtCursorPosition
import com.example.flashmemorycardntk.model.condicionalSpacerForLongScreen
import com.example.flashmemorycardntk.ui.theme.cor30Porcento300
import com.example.flashmemorycardntk.ui.theme.cor30Porcento500
import com.example.flashmemorycardntk.ui.theme.corDoTextoEscuroPadrao700
import com.example.flashmemorycardntk.ui.theme.corDoTextoEscuroPadrao900

@Composable
fun ScreenFab1(
    paddingValues: PaddingValues
) {
    val vm: ViewModelFab1 = hiltViewModel()
    val uiStateFab by vm.fabState.collectAsState()

    val options = listOf(EnumQuestionAnswer.QUESTION, EnumQuestionAnswer.ANSWER)
    val selectedSegmentedButtonOption = uiStateFab.segmentedButtonStatus
    var isBigScreen by remember { mutableStateOf(false) }
    val selectedLevel = uiStateFab.levelSelected
    val textInputQuestion = remember { mutableStateOf(TextFieldValue("")) }
    val textInputAnswer = remember { mutableStateOf(TextFieldValue("")) }

    val context = LocalContext.current
    val clipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val keyboardController = LocalSoftwareKeyboardController.current


    // Criando uma instância de MutableInteractionSource para controlar os estados de interação
    val interactionSourceRipple = remember { MutableInteractionSource() }
    // Determinando a indicação baseada no modo de seleção
    val indicationRipple = if (uiStateFab.isEmptyGroupList) null else rememberRipple()



    MainColumn(paddingValues = paddingValues) {
        condicionalSpacerForLongScreen().also { isBigScreen = it }
        BaseRowGroupNameAndIcon(
            text = uiStateFab.groupSelectedInDialog.name,
            textColor = if (!uiStateFab.isEmptyGroupList) corDoTextoEscuroPadrao900 else cor30Porcento500,
            composableBoxForIcon = {
                BoxForIcon(
                    enabledBox = !uiStateFab.isEmptyGroupList,
                    iconVector = null,
                    iconDrawable = R.drawable.menu_down,
                    iconSize = 32,
                    onClickX = {
                        vm.handleEvent(Fab1Events.ButtonDropDownDialogClicked)
                    }
                )
            }
        )
        VerticalSpacer()
        SegmentedButtonRow(
            options = options,
            selectedOption = selectedSegmentedButtonOption,
            onClickX = { optionEnumQuestionAnswer ->
                vm.handleEvent(Fab1Events.SegmentedButtonClicked(optionEnumQuestionAnswer))
            }
        )
        VerticalSpacer()
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "New Card",
            fontSize = 16.sp,
            color = corDoTextoEscuroPadrao700,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        VerticalSpacer(height = 4)
        when (uiStateFab.segmentedButtonStatus) {
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
                onClickX = {scDifficulty ->
                    vm.handleEvent(Fab1Events.ButtonLevelClicked(level = scDifficulty))
                }
            )
            Spacer(modifier = Modifier.weight(1f))
            BaseButton(
                text = "SAVE",
                enableButton = textInputQuestion.value.text.isNotEmpty()
                        && textInputAnswer.value.text.isNotEmpty()
                        && !uiStateFab.isEmptyGroupList,
                onClick = {
                    keyboardController?.hide()
                    vm.handleEvent(
                        Fab1Events.ButtonSaveClicked(
                            cardEntity = CardEntity(
                                question = textInputQuestion.value.text,
                                answer = textInputAnswer.value.text,
                                difficultyLevel = uiStateFab.levelSelected.levelMode,
                                successRate = 0,
                                errorRate = 0,
                                groupId = uiStateFab.groupSelectedInDialog.groupId
                            )
                        )
                    )
                    textInputQuestion.value = TextFieldValue("")
                    textInputAnswer.value = TextFieldValue("")
                    vm.handleEvent(Fab1Events.SegmentedButtonClicked(EnumQuestionAnswer.QUESTION))
                    vm.handleEvent(Fab1Events.ButtonLevelClicked(level = SCDifficulty.Level1))
                }
            )
        }
    }


// ------------ DIALOG ---------------------------------------------------------------------
    if (uiStateFab.showDialog) {
        BaseDialog(
            title = "Groups",
            body = { columnScope ->
                columnScope.BaseLazyColumn(
                    listItens = uiStateFab.groupList,
                    weight = 1f,
                    content = { groupEntity ->
                        BaseBackgroundBoxForG1(
                            color = cor30Porcento300,
                            onClickX = {
                                vm.handleEvent(Fab1Events.DropDownDialogItemSelected(groupEntity))
                            }) {
                            Text(
                                text = groupEntity.name,
                                modifier = Modifier
                                    .padding(horizontal = 9.dp, vertical = 12.dp),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                )
            },
            onCancel = { vm.handleEvent(Fab1Events.CancelDialog) })
    }
}