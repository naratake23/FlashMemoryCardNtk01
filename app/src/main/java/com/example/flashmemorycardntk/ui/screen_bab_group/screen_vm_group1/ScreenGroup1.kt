package com.example.flashmemorycardntk.ui.screen_bab_group.screen_vm_group1

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.flashmemorycardntk.model.BaseAlertDialog
import com.example.flashmemorycardntk.model.BaseBackgroundBoxForG1
import com.example.flashmemorycardntk.model.BaseButton
import com.example.flashmemorycardntk.model.BaseLazyColumn
import com.example.flashmemorycardntk.model.BaseOutlinedTextField
import com.example.flashmemorycardntk.model.BaseRowDeleteButtonAndGroupQtyText
import com.example.flashmemorycardntk.model.MainColumn
import com.example.flashmemorycardntk.model.VerticalSpacer
import com.example.flashmemorycardntk.model.condicionalSpacerForLongScreen
import com.example.flashmemorycardntk.ui.theme.cor30Porcento500
import com.example.flashmemorycardntk.ui.theme.corDoTextoEscuroPadrao700
import kotlinx.coroutines.delay

@Composable
fun ScreenGroup1(
    paddingValues: PaddingValues,
    nextScreen: (groupId: Long) -> Unit
) {
    val vm: ViewModelGroup1 = hiltViewModel()
    val uiStateG by vm.groupState.collectAsState()

    val textInput = remember { mutableStateOf("") }
    var blankFieldError by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    val cleanDeletes = remember { mutableStateOf(true) }

    if (cleanDeletes.value) {
        vm.deleteSelectedItems()
        cleanDeletes.value = false
    }


    MainColumn(
        paddingValues = paddingValues,
        content = {
            condicionalSpacerForLongScreen()
            BaseButton(text = "Add New Group", textSize = 20) {
                vm.handleEvent(Group1Events.ButtonAddClicked)
            }
            VerticalSpacer(30)
            BaseRowDeleteButtonAndGroupQtyText(
                selectionMode = uiStateG.selectionMode,
                groupQtyText = uiStateG.groupSize,
                onClickDelete = {
                    vm.handleEvent(Group1Events.ButtonDeleteClicked)
                }
            )
            VerticalSpacer()
            Row {
                Text(
                    text = "Name",
                    fontSize = 16.sp,
                    color = corDoTextoEscuroPadrao700,
                    modifier = Modifier
                        .weight(0.7f)
                        .padding(start = 6.dp)
                )
                Text(
                    text = "Quantity",
                    fontSize = 16.sp,
                    color = corDoTextoEscuroPadrao700,
                    modifier = Modifier
                        .weight(0.2f)
                        .padding(start = 6.dp)
                )
                Spacer(modifier = Modifier.weight(0.1f))
            }

            if (uiStateG.groupList.isNotEmpty()) {
                VerticalSpacer(height = 4)
                BaseLazyColumn(listItens = uiStateG.groupList) { item ->
                    BaseBackgroundBoxForG1(
                        item = item.group,
                        selectedItems = uiStateG.selectedItems,
                        selectionMode = uiStateG.selectionMode,
                        onClickX = {
                            nextScreen(item.group.groupId)
                        }, onClickSelectionX = {
                            vm.toggleItemSelection(item.group)
                        }, onLongClickX = {
                            vm.enterSelectionMode()
                            vm.toggleItemSelection(item.group)
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.group.name,
                                modifier = Modifier
                                    .weight(0.7f)
                                    .padding(horizontal = 9.dp),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${item.cardCount}",
                                modifier = Modifier.weight(0.2f),
                                textAlign = TextAlign.Center
                            )
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowRight,
                                contentDescription = "Arrow right",
                                modifier = Modifier
                                    .size(width = 33.dp, height = 33.dp)
                                    .weight(0.1f),
                                tint = corDoTextoEscuroPadrao700
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
    if (uiStateG.showDialog) {

        LaunchedEffect(0) {
            textInput.value = ""
            delay(100) // delay para garantir que o programa não quebre na rotação de tela
            focusRequester.requestFocus()
        }
        BaseAlertDialog(
            title = "Add new group",
            body = {
                BaseOutlinedTextField(
                    textInput = textInput,
                    labelText = "Group's name",
                    isError = blankFieldError,
                    focusRequester = focusRequester,
                    iconDrawable = null
                )
            },
            onCancel = {
                vm.handleEvent(Group1Events.CancelDialog)
                textInput.value = ""
                blankFieldError = false
            },
            onConfirm = {
                blankFieldError = textInput.value.isBlank()
                if (!blankFieldError) {
                    vm.handleEvent(Group1Events.ButtonSaveClicked(textInput.value))
                    textInput.value = ""
                }
            })
    }
}

