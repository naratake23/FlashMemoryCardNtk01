package com.example.flashmemorycardntk.ui.screen_bab_shuffle.screen_vm_shuffle1

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.flashmemorycardntk.R
import com.example.flashmemorycardntk.model.BaseBackgroundBoxForG1
import com.example.flashmemorycardntk.model.BaseBoxForShuffleSettingsColumn
import com.example.flashmemorycardntk.model.BaseBoxForShuffleSettingsRow
import com.example.flashmemorycardntk.model.BaseButton
import com.example.flashmemorycardntk.model.BaseDialog
import com.example.flashmemorycardntk.model.BaseLazyColumn
import com.example.flashmemorycardntk.model.BaseRowGroupNameAndIcon
import com.example.flashmemorycardntk.model.BoxForIcon
import com.example.flashmemorycardntk.model.EnumSortSequence
import com.example.flashmemorycardntk.model.MainColumn
import com.example.flashmemorycardntk.model.VerticalSpacer
import com.example.flashmemorycardntk.model.condicionalSpacerForLongScreen
import com.example.flashmemorycardntk.ui.theme.cor10Porcento50
import com.example.flashmemorycardntk.ui.theme.cor30Porcento300
import com.example.flashmemorycardntk.ui.theme.cor30Porcento400
import com.example.flashmemorycardntk.ui.theme.cor30Porcento50
import com.example.flashmemorycardntk.ui.theme.cor30Porcento500
import com.example.flashmemorycardntk.ui.theme.corDoTextoClaroPadrao100
import com.example.flashmemorycardntk.ui.theme.corDoTextoEscuroPadrao700
import com.example.flashmemorycardntk.ui.theme.corDoTextoEscuroPadrao900

@Composable
fun ScreenShuffle1(
    paddingValues: PaddingValues
) {
    val vm: ViewModelShuffle1 = hiltViewModel()
    val uiStateShuffle by vm.shuffleState.collectAsState()

    var infinitySwitch by remember(uiStateShuffle.infinitySwitch) { mutableStateOf(uiStateShuffle.infinitySwitch) }
    var shuffleSwitch by remember(uiStateShuffle.shuffleSwitch) { mutableStateOf(uiStateShuffle.shuffleSwitch) }
    var ratioSlider by remember(uiStateShuffle.balanceSlider) { mutableFloatStateOf(uiStateShuffle.balanceSlider.toFloat()) }

    val textColor =
        if (uiStateShuffle.isEmptyGroupList || shuffleSwitch) cor30Porcento500 else corDoTextoEscuroPadrao900
    val textColorBalance = if (!shuffleSwitch) cor30Porcento500 else corDoTextoEscuroPadrao900

    val boxColor = if (shuffleSwitch) corDoTextoClaroPadrao100 else corDoTextoEscuroPadrao700
    val iconTint = if (shuffleSwitch) cor30Porcento500 else cor10Porcento50

    MainColumn(paddingValues = paddingValues) {
        condicionalSpacerForLongScreen()
        BaseRowGroupNameAndIcon(
            text = uiStateShuffle.groupSelectedInDialog?.name ?: "",
            textColor = if (!uiStateShuffle.isEmptyGroupList) corDoTextoEscuroPadrao900 else cor30Porcento500,
            composableBoxForIcon = {
                BoxForIcon(
                    enabledBox = !uiStateShuffle.isEmptyGroupList,
                    iconVector = null,
                    iconDrawable = R.drawable.menu_down,
                    iconSize = 32,
                    onClickX = {
                        vm.handleEvent(Shuffle1Events.ButtonDropDownDialogGroupClicked)
                    }
                )
            }
        )
        VerticalSpacer()
        BaseBoxForShuffleSettingsRow(
            title = "Infinite review",
            description = "Enable to keep revising cards without end"
        ) {
            Switch(
                enabled = !uiStateShuffle.isEmptyGroupList,
                checked = infinitySwitch,
                onCheckedChange = {
                    infinitySwitch = it
                    vm.handleEvent(Shuffle1Events.InfinitySwitchClicked(infinitySwitch))
                },
                colors = SwitchDefaults.colors(
                    checkedTrackColor = corDoTextoEscuroPadrao700,
                    uncheckedTrackColor = cor30Porcento50
                ),
                modifier = Modifier.scale(0.81f)
            )
        }
        BaseBoxForShuffleSettingsColumn(
            title = "Sort by",
            description = "Choose the sequence in which cards will be displayed during review"
        ) {
            BaseRowGroupNameAndIcon(
                text = uiStateShuffle.orderSelectedInDialog,
                textColor = textColor,
                textSize = 18,
                composableBoxForIcon = {
                    BoxForIcon(
                        enabledBox = !uiStateShuffle.isEmptyGroupList && !shuffleSwitch,
                        iconColor = iconTint,
                        boxColor = boxColor,
                        iconVector = null,
                        iconDrawable = R.drawable.menu_down,
                        iconSize = 32,
                        onClickX = {
                            vm.handleEvent(Shuffle1Events.ButtonDropDownDialogOrderClicked)
                        }
                    )
                }
            )
        }
        BaseBoxForShuffleSettingsRow(
            title = "Shuffle cards",
            description = "Toggle to mix the order of the cards"
        ) {
            Switch(
                enabled = !uiStateShuffle.isEmptyGroupList,
                checked = shuffleSwitch,
                colors = SwitchDefaults.colors(
                    checkedTrackColor = corDoTextoEscuroPadrao700,
                    uncheckedTrackColor = cor30Porcento50
                ),
                onCheckedChange = {
                    shuffleSwitch = it
                    vm.handleEvent(Shuffle1Events.ShuffleSwitchClicked(shuffleSwitch))
                },
                modifier = Modifier.scale(0.81f)
            )
        }
        BaseBoxForShuffleSettingsColumn(
            title = "Difficulty balance",
//            description = "Adjust the balance between easy and hard cards."
            description = "Review ${ratioSlider.toInt()} Hard cards for each Easy card"
        ) {
            VerticalSpacer(height = 3)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Slider(
                    value = ratioSlider,
                    enabled = !uiStateShuffle.isEmptyGroupList && shuffleSwitch,
                    onValueChange = {
                        ratioSlider = it
                        vm.handleEvent(Shuffle1Events.BalanceSliderClicked(ratioSlider.toInt()))
                    },
                    valueRange = 3f..6f,
                    steps = 2,
                    colors = SliderDefaults.colors(
                        thumbColor = corDoTextoEscuroPadrao700,
                        activeTrackColor = corDoTextoEscuroPadrao700,
                        inactiveTrackColor = cor30Porcento50,

                        disabledThumbColor = cor30Porcento400,
                        disabledActiveTrackColor = cor30Porcento400,
                        disabledInactiveTickColor = corDoTextoClaroPadrao100,
                    ),
                    modifier = Modifier.weight(1f)

                )
                Text(
                    text = "${ratioSlider.toInt()}",
                    fontSize = 20.sp,
                    color = textColorBalance,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 27.dp, end = 18.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
        VerticalSpacer(3)
        BaseButton(
            text = "APPLY",
            enableButton = !uiStateShuffle.isEmptyGroupList,
            onClick = {
                vm.handleEvent(Shuffle1Events.ButtonApplyClicked)
            }
        )

    }


    // ------------ GROUP DIALOG ---------------------------------------------------------------------
    if (uiStateShuffle.showDialogGroup) {
        BaseDialog(
            title = "Groups",
            body = { columnScope ->
                columnScope.BaseLazyColumn(
                    listItens = uiStateShuffle.groupList,
                    weight = 1f,
                    content = { groupEntity ->
                        BaseBackgroundBoxForG1(
                            color = cor30Porcento300,
                            onClickX = {
                                vm.handleEvent(
                                    Shuffle1Events.DropDownDialogGroupItemSelected(
                                        groupEntity
                                    )
                                )
                                vm.handleEvent(Shuffle1Events.CancelDialogs)
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
            onCancel = {
                vm.handleEvent(Shuffle1Events.CancelDialogs)
            })
    }

    // ------------ ORDER DIALOG ---------------------------------------------------------------------
    if (uiStateShuffle.showDialogOrder) {
        BaseDialog(
            title = "Sort by",
            isMaxHeight = false,
            body = { columnScope ->
                columnScope.BaseLazyColumn(
                    listItens = listOf(
                        EnumSortSequence.ORIGINAL,
                        EnumSortSequence.REVERSED,
                        EnumSortSequence.EASY,
                        EnumSortSequence.HARD
                    ),
                    weight = null,
                    content = { sequence ->
                        BaseBackgroundBoxForG1(
                            color = cor30Porcento300,
                            onClickX = {
                                vm.handleEvent(
                                    Shuffle1Events.DropDownDialogOrderItemSelected(
                                        sequence
                                    )
                                )
                                vm.handleEvent(Shuffle1Events.CancelDialogs)
                            }) {
                            Text(
                                text = sequence.strEnum,
                                modifier = Modifier
                                    .padding(horizontal = 9.dp, vertical = 12.dp),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                )
            },
            onCancel = {
                vm.handleEvent(Shuffle1Events.CancelDialogs)
            })
    }
}