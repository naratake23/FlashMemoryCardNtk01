package com.example.flashmemorycardntk.ui.screen_bab_group.screen_vm_group2

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.flashmemorycardntk.R
import com.example.flashmemorycardntk.model.BaseAlertDialog
import com.example.flashmemorycardntk.model.BaseBackgroundBoxForG2
import com.example.flashmemorycardntk.model.BaseLazyColumn
import com.example.flashmemorycardntk.model.BaseOutlinedTextField
import com.example.flashmemorycardntk.model.BaseRowGroupNameAndIcon
import com.example.flashmemorycardntk.model.BoxForIcon
import com.example.flashmemorycardntk.model.CardForLazyColumn
import com.example.flashmemorycardntk.model.MainColumn
import com.example.flashmemorycardntk.model.VerticalSpacer
import com.example.flashmemorycardntk.model.cardSearch
import com.example.flashmemorycardntk.model.condicionalSpacerForLongScreen
import com.example.flashmemorycardntk.ui.theme.cor30Porcento500
import com.example.flashmemorycardntk.ui.theme.corDoTextoClaroPadrao100
import com.example.flashmemorycardntk.ui.theme.corDoTextoEscuroPadrao700
import com.example.flashmemorycardntk.ui.theme.fabRed400
import kotlinx.coroutines.delay


@Composable
fun ScreenGroup2(
    paddingValues: PaddingValues,
    nextScreenG3: (cardId: Long, isFrontSide: Boolean) -> Unit,
    nextScreenMultipleAdd: () -> Unit,
) {
    val vm: ViewModelGroup2 = hiltViewModel()
    val uiStateG2 by vm.group2State.collectAsState()

    val searchTextInput = remember { mutableStateOf("") }

    val textInputDialog = remember { mutableStateOf("") }
    var blankFieldError by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    val cleanDeletes = remember { mutableStateOf(true) }

    if (cleanDeletes.value) {
        vm.deleteSelectedItems()
        cleanDeletes.value = false
    }

    MainColumn(paddingValues = paddingValues, content = {
        condicionalSpacerForLongScreen()
        BaseRowGroupNameAndIcon(
            text = uiStateG2.groupName,
            composableBoxForIcon = {
                BoxForIcon(
                    iconVector = null,
                    iconDrawable = R.drawable.pencil,
                    onClickX = {
                        vm.handleEvent(Group2Events.ButtonEditGroupNameClicked)
                    }
                )
            }
        )
        VerticalSpacer()
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BoxForIcon(
                iconVector = null,
                iconDrawable = R.drawable.delete_forever_outline,
                enabledBox = uiStateG2.selectionMode,
                boxColor = corDoTextoClaroPadrao100,
                iconColor = if (uiStateG2.selectionMode) fabRed400 else cor30Porcento500,
                elevation = if (uiStateG2.selectionMode) 2 else 0,
                iconSize = 30,
                yOffSet = 4,
                onClickX = {
                    vm.handleEvent(Group2Events.ButtonDeleteClicked)
                }
            )
            Spacer(modifier = Modifier.width(9.dp))
            BoxForIcon(
                iconVector = null,
                iconDrawable = R.drawable.format_list_group_plus,
                yOffSet = 4,
                onClickX = {
                    nextScreenMultipleAdd()
                }
            )
            Spacer(modifier = Modifier.width(9.dp))
            BaseOutlinedTextField(
                textInput = searchTextInput,
                labelText = "Find a card",
                iconDrawable = R.drawable.magnify,
            )
            Spacer(modifier = Modifier.width(6.dp))
        }
        VerticalSpacer()
        Row {
            Text(
                text = "Question",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = corDoTextoEscuroPadrao700,
                modifier = Modifier
                    .weight(0.5f)
            )
            Text(
                text = "Answer",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = corDoTextoEscuroPadrao700,
                modifier = Modifier
                    .weight(0.5f)
            )
        }
        if (uiStateG2.cardList.isNotEmpty()) {
            VerticalSpacer(height = 4)
            BaseLazyColumn(
                listItens = cardSearch(
                    uiStateG2.cardList,
                    searchTextInput
                )
            ) { itemCard ->
                BaseBackgroundBoxForG2(
                    item = itemCard,
                    selectedItems = uiStateG2.selectedItems,
                    selectionMode = uiStateG2.selectionMode,
                    onClickSelectionX = {
                        vm.toggleItemSelection(itemCard)
                    }, onLongClickX = {
                        vm.enterSelectionMode()
                        vm.toggleItemSelection(itemCard)
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 6.dp, horizontal = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CardForLazyColumn(
                            text = itemCard.question,
                            isDottedBackground = true,
                            onClickX = {
                                nextScreenG3(itemCard.cardId, true)
                            }
                        )
                        Spacer(modifier = Modifier.weight(0.09f))
                        CardForLazyColumn(
                            text = itemCard.answer,
                            isDottedBackground = false,
                            onClickX = {
                                nextScreenG3(itemCard.cardId, false)
                            }
                        )
                    }
                }
            }
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.weight(0.85f)
            ) {
                Text(text = "EMPTY", fontSize = 30.sp, color = cor30Porcento500)
            }
        }
    }
    )

    // ------------ DIALOG ---------------------------------------------------------------------
    if (uiStateG2.showDialogEditGroupName) {

        LaunchedEffect(0) {
            textInputDialog.value = ""
            delay(100) // delay para garantir que o programa não quebre na rotação de tela
            focusRequester.requestFocus()
        }
        BaseAlertDialog(
            title = "Edit Group's name",
            textYesButton = "Edit",
            body = {
                BaseOutlinedTextField(
                    textInput = textInputDialog,
                    labelText = "New name",
                    isError = blankFieldError,
                    focusRequester = focusRequester,
                    iconDrawable = null
                )
            },
            onCancel = {
                vm.handleEvent(Group2Events.CancelDialog)
                textInputDialog.value = ""
                blankFieldError = false
            },
            onConfirm = {
                blankFieldError = textInputDialog.value.isBlank()
                if (!blankFieldError) {
                    vm.handleEvent(Group2Events.ButtonDialogEditNameClicked(textInputDialog.value))
                    textInputDialog.value = ""
                }
            })
    }
}