package com.example.flashmemorycardntk.ui.screen_bab_group.screen_vm_group2

import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.flashmemorycardntk.model.BaseButton
import com.example.flashmemorycardntk.model.BaseCardFrontNBack
import com.example.flashmemorycardntk.model.MainColumn
import com.example.flashmemorycardntk.model.VerticalSpacer
import com.example.flashmemorycardntk.model.concatenateTextsAtCursorPosition
import com.example.flashmemorycardntk.ui.theme.corDoTextoEscuroPadrao700

@Composable
fun ScreenGroup2MultipleAdds(
    paddingValues: PaddingValues,
    backScreen: ()->Unit
) {

    val vm: ViewModelGroup2 = hiltViewModel()

    val textInput = remember { mutableStateOf(TextFieldValue("")) }

    val context = LocalContext.current
    val clipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val keyboardController = LocalSoftwareKeyboardController.current


    MainColumn(paddingValues = paddingValues) {

        VerticalSpacer()

        Text(
            text = "Add multiple questions and answers by prefixing questions with 'qx:' and answers with 'ax:' before the text. Ensure each question-answer pair is separated distinctly.\n" +
                    "Example: 'qx: What is 3^2? ax: 3^2 equals 3*3, which is 9'",
            fontSize = 16.sp,
            color = corDoTextoEscuroPadrao700,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        VerticalSpacer()

        BaseCardFrontNBack(
            textInput = textInput,
            isDottedBackground = false,
            isBigScreen = true,
            onClickClear = {
                textInput.value = TextFieldValue("")
            },
            onClickPaste = {
                concatenateTextsAtCursorPosition(
                    clipboardManager = clipboardManager,
                    textState = textInput
                )
            }
        )

        VerticalSpacer(9)

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))

            BaseButton(
                text = "SAVE",
                enableButton = textInput.value.text.isNotEmpty(),
                onClick = {
                    keyboardController?.hide()
                    vm.handleEvent(Group2Events.ButtonSaveMultipleCardsClicked(textInput.value.text))
                    textInput.value = TextFieldValue("")
                    backScreen()
                }
            )
        }
    }
}