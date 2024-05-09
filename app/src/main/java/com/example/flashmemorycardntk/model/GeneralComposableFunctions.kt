package com.example.flashmemorycardntk.model

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.flashmemorycardntk.R
import com.example.flashmemorycardntk.data.entities.CardEntity
import com.example.flashmemorycardntk.data.entities.GroupEntity
import com.example.flashmemorycardntk.ui.theme.Purple40
import com.example.flashmemorycardntk.ui.theme.cor10Porcento300
import com.example.flashmemorycardntk.ui.theme.cor10Porcento500
import com.example.flashmemorycardntk.ui.theme.cor30Porcento200
import com.example.flashmemorycardntk.ui.theme.cor30Porcento300
import com.example.flashmemorycardntk.ui.theme.cor30Porcento400
import com.example.flashmemorycardntk.ui.theme.cor30Porcento50
import com.example.flashmemorycardntk.ui.theme.cor30Porcento500
import com.example.flashmemorycardntk.ui.theme.cor30Porcento600
import com.example.flashmemorycardntk.ui.theme.corDoTextoClaroPadrao100
import com.example.flashmemorycardntk.ui.theme.corDoTextoEscuroPadrao700
import com.example.flashmemorycardntk.ui.theme.corDoTextoEscuroPadrao800
import com.example.flashmemorycardntk.ui.theme.corDoTextoEscuroPadrao900
import com.example.flashmemorycardntk.ui.theme.customGray350
import com.example.flashmemorycardntk.ui.theme.fabGreen500
import com.example.flashmemorycardntk.ui.theme.fabRed400
import com.example.flashmemorycardntk.ui.theme.levelGreen
import com.example.flashmemorycardntk.ui.theme.levelRed
import com.example.flashmemorycardntk.ui.theme.levelYellow
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun BaseBottomAppBar(
    babNavController: NavHostController,
    screens: List<SCBottomAppBar>,
    screenFab: SCBottomAppBar,
    onClickFab: (route: String) -> Unit,
    backgroundColor: Color = cor30Porcento300,
    fabColor: Color = fabGreen500,
    customItem: @Composable (isSelected: Boolean, item: SCBottomAppBar) -> Unit
) {

    val navBackStackEntry by babNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomAppBar(
        containerColor = backgroundColor,
        actions = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                NavigationBar(
                    // Parte da NavigationBar ocupando 75% do espaço
                    modifier = Modifier.weight(0.75f),
                    containerColor = Color.Transparent
                ) {
                    screens.forEach { item ->
                        val isSelected =
                            currentDestination?.hierarchy
                                ?.any { it.route == item.route } == true
                        customItem(isSelected, item)
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(0.25f)
                        .padding(top = 6.dp, end = 15.dp),
                    contentAlignment = Alignment.CenterEnd,
                ) {
                    FloatingActionButton(
                        containerColor = fabColor,
                        contentColor = corDoTextoEscuroPadrao800,
                        onClick = {
                            onClickFab(screenFab.route)

                        },
                    ) {
                        Icon(
                            painter = painterResource(id = screenFab.icon),
                            contentDescription = screenFab.title
                        )
                    }
                }
            }
        }
    )
}
//--------------------------------------------------------------------------
@Composable
fun BaseNavigationBarItem(
    selected: Boolean,
    onClickItens: () -> Unit,
    icon: Int,
    label: String,
    iconColorSelected: Color = cor30Porcento50,
    iconColorUnselected: Color = cor30Porcento600,
    boxColorSelected: Color = corDoTextoEscuroPadrao700,
    labelColorSelected: Color = corDoTextoEscuroPadrao800,
    paddingBetweenItems: Int = 14,
    paddingTop: Int = 8,
    shape: Shape = RoundedCornerShape(6.dp)
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .noRippleClickable(onClick = onClickItens) // Modificador personalizado para remover o ripple
            .padding(horizontal = paddingBetweenItems.dp)
            .padding(top = paddingTop.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(shape)
                .background(if (selected) boxColorSelected else Color.Transparent)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = if (selected) iconColorSelected else iconColorUnselected
            )
        }
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = if (selected) labelColorSelected else Color.Transparent, // Texto transparente quando não selecionado
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        onClick()
    }
}
//--------------------------------------------------------------------------
@Composable
fun MainColumn(
    paddingValues: PaddingValues,
    color: Color = cor30Porcento50,
    content: @Composable() (ColumnScope.() -> Unit),
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(paddingValues)
            .padding(9.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        content()
    }
}
//----------------------------------------------------------------------------
@Composable
fun VerticalSpacer(height: Int = 15) {
    Spacer(modifier = Modifier.height(height.dp))
}
//----------------------------------------------------------------------------
@Composable
fun StatusRowForPlayScreen(
    questionAnswerLabel: String,
    cardCounter: Int,
    totalNumbersOfCards: String,
    selectedLevel: SCDifficulty
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = questionAnswerLabel,
            fontSize = 18.sp,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "$cardCounter/$totalNumbersOfCards",
            fontSize = 18.sp,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center

        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .width(160.dp),
            contentAlignment = Alignment.CenterEnd,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = selectedLevel.levelMode,
                    fontSize = 18.sp,
                    color = selectedLevel.color,
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth(Alignment.CenterHorizontally) //centraliza horizontalmente o texto dentro da largura fixa do botão
                )
                Icon(
                    painter = painterResource(id = selectedLevel.icon),
                    tint = selectedLevel.color,
                    contentDescription = "Icon selectedLevel",
                )
            }
        }
    }
}
//----------------------------------------------------------------------------
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColumnScope.CardForPlayScreen(
    flipped: Boolean,
    skipAnimation: Boolean,
    offsetX: MutableFloatState,
    offsetY: MutableFloatState,
    frontCard: @Composable () -> Unit,
    backCard: @Composable (modifier: Modifier) -> Unit,
    doubleClickX: () -> Unit,
    swipeRightX: () -> Unit,
    swipeLeftX: () -> Unit,
) {

    val interactionSource = remember { MutableInteractionSource() }
    val rotateY = animateFloatAsState(
        targetValue = if (flipped) 0f else 180f,
        animationSpec = tween(if (skipAnimation) 0 else 300),
        label = ""
    ).value

    val densityX = LocalDensity.current
    val configuration = LocalConfiguration.current
    val screenWidth = with(densityX) { configuration.screenWidthDp.dp.toPx() }

    val continueSwipe = remember { mutableStateOf(false) }
    val swipeDirection = remember { mutableStateOf("") }
// Animação para continuar o movimento do card
    LaunchedEffect(continueSwipe.value) {
        if (continueSwipe.value) {
            val targetOffsetX = if (swipeDirection.value == "right") screenWidth else -screenWidth
            animate(
                initialValue = offsetX.value,
                targetValue = targetOffsetX,
                animationSpec = tween(durationMillis = 100, easing = LinearOutSlowInEasing)
            ) { value, _ ->
                offsetX.value = value
            }
            continueSwipe.value = false

            if (swipeDirection.value == "right") {
                swipeRightX()
            } else {
                swipeLeftX()
            }
        }
    }

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .fillMaxSize()
            .weight(1f)
            .offset {
                IntOffset(
                    x = offsetX.floatValue.roundToInt(),
                    y = offsetY.floatValue.roundToInt()
                )
            }
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {},
                onDoubleClick = {
                    doubleClickX()
                }
            )
            .graphicsLayer {
                rotationY = rotateY
                cameraDistance = 6 * density
            }
            .pointerInput(flipped) {
                if (!flipped) {
                    detectDragGestures(
                        onDragEnd = {
                            when {
                                abs(offsetX.value) >= 200f -> {
                                    swipeDirection.value =
                                        if (offsetX.value > 0) "right" else "left"
                                    continueSwipe.value = true
                                }

                                else -> {
                                    offsetX.value = 0f
                                    offsetY.value = 0f
                                }
                            }
                        }, onDrag = { _, dragAmount ->
                            val (dragAmountX, dragAmountY) = dragAmount
                            offsetX.floatValue -= dragAmountX
                            offsetY.floatValue += dragAmountY
                        }
                    )
                }
            }
    )
    {
        if (rotateY <= 90f || rotateY > 270f) frontCard()
        else backCard(Modifier.graphicsLayer { rotationY = 180f })
    }
}
//----------------------------------------------------------------------------
@Composable
fun FrontCardContentPlayScreen(
    stringContent: String,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = corDoTextoClaroPadrao100,
                shape = MaterialTheme.shapes.large
            )
    ) {
        CanvasDottedBackground()
        // Conteúdo rolável
        Column(
            modifier = Modifier
                .padding(top = 30.dp, bottom = 15.dp)
                .padding(horizontal = 15.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
//            contentAlignment = Alignment.Center
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = stringContent)
        }

    }
}
//----------------------------------------------------------------------------
@Composable
fun BackCardContentPlayScreen(
    modifier: Modifier,
    stringContent: String
) {
    Box(
        modifier = modifier
            .background(color = corDoTextoClaroPadrao100, shape = MaterialTheme.shapes.large)
            .padding(top = 30.dp, bottom = 15.dp)
            .padding(horizontal = 15.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Text(text = stringContent)
    }
}
//----------------------------------------------------------------------------
@Composable
fun CanvasDottedBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val dotRadius = 0.75.dp.toPx() //tamanho dos pontos
        val dotColor = customGray350
        val spacing = 20.dp.toPx() // Espaçamento entre os pontos

        for (x in 18 until size.width.toInt() step spacing.toInt()) {
            for (y in 1 until size.height.toInt() step spacing.toInt()) {
                drawCircle(
                    color = dotColor,
                    radius = dotRadius,
                    center = Offset(x.toFloat(), y.toFloat()),
                    style = Stroke(width = 1.dp.toPx())
                )
            }
        }
    }
}
//----------------------------------------------------------------------------
@Composable
fun ColumnScope.EmptyCardContentPlayScreen() {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .fillMaxSize()
            .weight(1f)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = corDoTextoClaroPadrao100,
                    shape = MaterialTheme.shapes.large
                )
        ) {
            CanvasDottedBackground()
            Text(text = "EMPTY", fontSize = 30.sp, color = cor30Porcento500)
        }
    }
}
//----------------------------------------------------------------------------
@Composable
fun ColumnScope.FinishPlayScreen(
    cardsCorrect: Int,
    totalNumbersOfCards: Int,
    onClickX: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .weight(0.1f)
            .fillMaxSize()
            .padding(30.dp)
    ) {
        Spacer(modifier = Modifier.weight(0.3f))
        VerticalSpacer(30)
        Text(text = "RESULT", fontSize = 30.sp, color = corDoTextoEscuroPadrao700)
        VerticalSpacer(30)
        Text(
            text = "$cardsCorrect/$totalNumbersOfCards",
            fontSize = 90.sp,
            color = corDoTextoEscuroPadrao800
        )
        VerticalSpacer(30)
        Text(text = "score", fontSize = 20.sp, color = cor30Porcento600)
        VerticalSpacer(60)
        BaseButton(text = "RETAKE") {
            onClickX()
        }
        Spacer(modifier = Modifier.weight(0.7f))
    }
}
//----------------------------------------------------------------------------
@Composable
fun BaseButton(
    text: String,
    enableButton: Boolean = true,
    textSize: Int = 18,
    textColor: Color = corDoTextoEscuroPadrao800, // Cor do texto
    backgroundColor: Color = cor10Porcento300, // Cor de fundo do botão
    onClick: () -> Unit,
) {

    ElevatedButton(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
        ),
        shape = RoundedCornerShape(6.dp),
        enabled = enableButton
    ) {
        Text(
            text = text,
            fontSize = textSize.sp,
            color = if (enableButton) textColor else cor30Porcento500
        )
    }
}
//----------------------------------------------------------------------------
@Composable
fun BaseSnackbarLayout(
    lastMessage: SnackbarMessage?,
    actionLabel: () -> Unit
) {
    if (lastMessage != null) {
        Snackbar(
            modifier = Modifier.padding(12.dp),
            containerColor = lastMessage.backgroundColor,
            actionContentColor = lastMessage.backgroundColor,
            action = {
                Text(
                    text = lastMessage.actionText,
                    fontSize = 18.sp,
                    color = lastMessage.actionTextColor,
                    modifier = Modifier
                        .padding(end = 6.dp)
                        .clickable {
                            actionLabel()
                        }
                )
            }
        ) {
            Text(
                text = lastMessage.text,
                color = lastMessage.textColor
            )
        }
    }
}
//----------------------------------------------------------------------------
@Composable
fun ColumnScope.condicionalSpacerForLongScreen(
    weightForLongScreen: Float = 0.15f,
    thresholdDp: Int = 570
): Boolean {
    val screenHeighDp = LocalConfiguration.current.screenHeightDp.dp
    if (screenHeighDp > thresholdDp.dp) {
        Spacer(modifier = Modifier.weight(weightForLongScreen))
        return true
    }
    return false
}
//----------------------------------------------------------------------------
@Composable
fun BaseRowGroupNameAndIcon(
    text: String,
    textSize: Int = 21,
    textColor: Color = corDoTextoEscuroPadrao900,
    spaceBetweenComponents: Int = 9,
    composableBoxForIcon: (@Composable RowScope.() -> Unit)? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(color = cor30Porcento300, shape = RoundedCornerShape(6.dp))
            .padding(top = 6.dp, bottom = 6.dp, start = 6.dp, end = 4.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontWeight = FontWeight.SemiBold,
            fontSize = textSize.sp,
            modifier = Modifier
                .weight(1f),
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        if (composableBoxForIcon != null) {
            Spacer(modifier = Modifier.width(spaceBetweenComponents.dp))
            composableBoxForIcon()
        }
    }
}
//----------------------------------------------------------------------------
@Composable
fun BoxForIcon(
    iconVector: ImageVector?,
    iconDrawable: Int?,
    boxSize: Int = 48,
    boxColor: Color = cor10Porcento300,
    iconSize: Int = 24,
    iconColor: Color = corDoTextoEscuroPadrao900,
    yOffSet: Int = 0,
    xOffSet: Int = 0,
    enabledBox: Boolean = true,
    elevation: Int = 2,
    onClickX: () -> Unit
) {

    Surface(
        modifier = Modifier
            .offset(
                y = yOffSet.dp,
                x = xOffSet.dp
            ) //move o componente além do alinhamento estabelecido pelo parent
            .clickable(
                enabled = enabledBox,
                onClick = onClickX
            )
            .size(
                width = boxSize.dp,
                height = boxSize.dp
            ), // Define o tamanho do Surface que é o tamanho do botão
        color = if (enabledBox) boxColor else corDoTextoClaroPadrao100,
        shape = MaterialTheme.shapes.small,
        shadowElevation = elevation.dp,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            if (iconDrawable != null) {
                Icon(
                    painter = painterResource(id = iconDrawable),
                    contentDescription = "Icon",
                    tint = if (enabledBox) iconColor else cor30Porcento500,
                    modifier = Modifier.size(
                        width = iconSize.dp,
                        height = iconSize.dp
                    ) // Tamanho do ícone
                )
            } else if (iconVector != null) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = "Icon",
                    tint = if (enabledBox) iconColor else cor30Porcento500,
                    modifier = Modifier.size(
                        width = iconSize.dp,
                        height = iconSize.dp
                    ) // Tamanho do ícone
                )
            }
        }
    }
}
//----------------------------------------------------------------------------
@Composable
fun ColumnScope.BaseBoxForShuffleSettingsRow(
    title: String,
    description: String,
    weight: Float = 1f,
    composeComponent: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(
                color = cor30Porcento200,
                shape = RoundedCornerShape(6.dp)
            )
            .padding(3.dp)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 9.dp, horizontal = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(weight),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = title)
                Text(
                    text = description,
                    fontSize = 16.sp,
                    color = corDoTextoEscuroPadrao700,
                )
            }
            Spacer(modifier = Modifier.width(9.dp))
            composeComponent()
        }
    }
}
//----------------------------------------------------------------------------
@Composable
fun BaseBoxForShuffleSettingsColumn(
    title: String,
    description: String,
    composeComponent: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(
                color = cor30Porcento200,
                shape = RoundedCornerShape(6.dp)
            )
            .padding(3.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 9.dp, horizontal = 9.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = title)
            Text(
                text = description,
                fontSize = 16.sp,
                color = corDoTextoEscuroPadrao700,
            )
            VerticalSpacer(height = 4)
            composeComponent()
        }
    }
}
//----------------------------------------------------------------------------
@Composable
fun BaseDialog(
    title: String,
    isMaxHeight: Boolean = true,
    body: @Composable (columnScope: ColumnScope) -> Unit,
    onCancel: () -> Unit,
    onConfirm: (() -> Unit)? = null,
) {

    Dialog(onDismissRequest = onCancel) {
        Surface(
            modifier = Modifier
                .then(
                    if (isMaxHeight) Modifier
                        .fillMaxWidth()
                        .heightIn(max = 500.dp)
                        .wrapContentHeight()
                    else Modifier
                )

                .shadow(3.dp, shape = MaterialTheme.shapes.medium),
            shape = MaterialTheme.shapes.medium, // Aplica a forma com cantos arredondados
            color = corDoTextoClaroPadrao100 // Substitua pela cor desejada
        ) {
            Column(modifier = Modifier.padding(all = 16.dp)) {
                // Título
                Text(text = title, fontWeight = FontWeight.SemiBold)
                VerticalSpacer(9)

                // Corpo
                body(this)
                VerticalSpacer(9)

                // Botões
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    BaseButton(
                        text = "Cancel",
                        backgroundColor = cor30Porcento50,
                        textColor = corDoTextoEscuroPadrao700
                    ) {
                        onCancel()
                    }
                    onConfirm?.let {
                        Spacer(modifier = Modifier.width(9.dp))
                        BaseButton(
                            text = "Confirm",
                            textColor = cor30Porcento50,
                            backgroundColor = corDoTextoEscuroPadrao700
                        ) {
                            it.invoke()
                        }
                    }
                }
            }
        }
    }
}
//----------------------------------------------------------------------------
@Composable
fun <T> ColumnScope.BaseLazyColumn(
    listItens: List<T>,
    weight: Float? = 0.85f,
    content: @Composable (item: T) -> Unit
) {
    LazyColumn(
        modifier = Modifier
                then (
                if (weight != null) Modifier
                    .fillMaxSize()
                    .weight(weight)
                else Modifier
                )
    ) {
        items(listItens) { item ->
            content(item)
        }
    }
}
//----------------------------------------------------------------------------
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BaseBackgroundBoxForG1(
    item: GroupEntity? = null,
    selectedItems: Set<GroupEntity>? = null,
    color: Color = cor30Porcento200,
    selectionMode: Boolean = false,
    onClickX: (() -> Unit)? = null,
    onClickSelectionX: (() -> Unit)? = null,
    onLongClickX: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val isSelected = selectedItems?.contains(item!!) == true
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .combinedClickable(
                onClick = {
                    onClickX?.let {
                        if (selectionMode) onClickSelectionX?.invoke()
                        else it.invoke()
                    }
                },
                onLongClick = {
                    if (onLongClickX != null) {
                        onLongClickX()
                    }
                }
            )
            .background(
                color = if (isSelected) cor30Porcento400 else color,
                shape = RoundedCornerShape(6.dp)
            )
            .padding(3.dp)
    )
    {
        content()
    }
}
//----------------------------------------------------------------------------
@Composable
fun BaseRowDeleteButtonAndGroupQtyText(
    iconDrawable: Int = R.drawable.delete_forever_outline,
    selectionMode: Boolean,
    groupQtyText: Int,
    onClickDelete: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        BoxForIcon(
            iconVector = null,
            iconDrawable = iconDrawable,
            enabledBox = selectionMode,
            boxColor = corDoTextoClaroPadrao100,
            iconColor = if (selectionMode) fabRed400 else cor30Porcento500,
            elevation = if (selectionMode) 2 else 0,
            iconSize = 30,
            onClickX = {
                onClickDelete()
            }
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                    append("$groupQtyText")
                }
                if (groupQtyText > 1) append(" groups")
                else append(" group")
            },
            textAlign = TextAlign.End,
        )
    }
}
//----------------------------------------------------------------------------
@Composable
fun BaseAlertDialog(
    title: String,
    textYesButton: String = "Confirm",
    body: @Composable () -> Unit,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
) {
    Dialog(onDismissRequest = onCancel) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .shadow(3.dp, shape = MaterialTheme.shapes.medium),
            shape = MaterialTheme.shapes.medium, // Aplica a forma com cantos arredondados
            color = corDoTextoClaroPadrao100
        ) {
            Column(modifier = Modifier.padding(all = 16.dp)) {
                // Título
                Text(text = title, fontWeight = FontWeight.SemiBold)
                VerticalSpacer()
                // Corpo
                body()
                VerticalSpacer()
                // Botões
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    BaseButton(
                        text = "Cancel",
                        backgroundColor = cor30Porcento50,
                        textColor = corDoTextoEscuroPadrao700
                    ) {
                        onCancel()
                    }
                    Spacer(modifier = Modifier.width(9.dp))
                    BaseButton(
                        text = textYesButton,
                        textColor = cor30Porcento50,
                        backgroundColor = corDoTextoEscuroPadrao700
                    ) {
                        onConfirm()
                    }
                }
            }
        }
    }
}
//----------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseOutlinedTextField(
    textInput: MutableState<String>,
    labelText: String,
    maxLengthForOutlinedTextField: Int = 90,
    iconDrawable: Int?,
    isError: Boolean = false,
    focusRequester: FocusRequester? = null
) {
    OutlinedTextField(
        value = textInput.value,
        onValueChange = { newValue ->
            if (newValue.length <= maxLengthForOutlinedTextField) {
                textInput.value = newValue
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .then(if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = cor30Porcento600,
            focusedBorderColor = cor10Porcento500,
            focusedLabelColor = corDoTextoEscuroPadrao700,
            containerColor = cor30Porcento50,
            errorBorderColor = levelRed,
            errorCursorColor = levelRed,
        ),
        label = { Text(text = labelText, color = cor30Porcento600) },
        isError = isError,
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        trailingIcon = {
            if (iconDrawable != null) {
                Icon(
                    painter = painterResource(id = iconDrawable),
                    contentDescription = "Icon $labelText"
                )
            }
        }
    )
    if (isError) {
        Text(text = "Blank field", color = levelRed, fontSize = 16.sp)
    }
}
//----------------------------------------------------------------------------
@Composable
fun ColumnScope.BaseCardFrontNBack(
    textInput: MutableState<TextFieldValue>,
    isDottedBackground: Boolean,
    color: Color = corDoTextoClaroPadrao100,
    isBigScreen: Boolean,
    cardHeight: Int = 240,
    onClickClear: () -> Unit,
    onClickPaste: () -> Unit,
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        modifier = Modifier
            .then(
                if (isBigScreen) Modifier.weight(0.85f)
                else Modifier.height(cardHeight.dp)
            )
    ) {
        Box {
            if (isDottedBackground) CanvasDottedBackground()
            Column {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButtonWithIcon(
                        text = "Clear",
                        iconDrawable = R.drawable.eraser
                    ) {
                        onClickClear()
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    OutlinedButtonWithIcon(
                        text = "Paste",
                        iconDrawable = R.drawable.content_paste
                    ) {
                        onClickPaste()
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                ) {

                    BasicTextField(
                        value = textInput.value,
                        onValueChange = { textInput.value = it },
                        textStyle = TextStyle(
                            fontSize = 18.sp,
                            color = corDoTextoEscuroPadrao900,
                        ),
                        singleLine = false,
                        modifier = Modifier
                            .fillMaxSize()
                            .heightIn(min = 330.dp)
                            .padding(horizontal = 15.dp)
                            .padding(top = 9.dp, bottom = 15.dp)
                    )
                }
            }
        }
    }
}
//----------------------------------------------------------------------------
@Composable
fun OutlinedButtonWithIcon(
    text: String,
    fontSize: Int = 16,
    textColor: Color = corDoTextoEscuroPadrao800,
    iconColor: Color = corDoTextoEscuroPadrao700,
    backgroundColor: Color = Color.Transparent,
    elevation: Int = 0,
    iconDrawable: Int,
    border: BorderStroke? = null,
    onClickX: () -> Unit
) {
    OutlinedButton(
        onClick = { onClickX() },
        shape = RoundedCornerShape(6.dp),
        border = border,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = elevation.dp)
    ) {
        Icon(
            painter = painterResource(id = iconDrawable),
            tint = iconColor,
            contentDescription = "Icon $text"
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(text = text, fontSize = fontSize.sp, color = textColor)
    }
}
//----------------------------------------------------------------------------
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BaseBackgroundBoxForG2(
    item: CardEntity,
    selectedItems: Set<CardEntity>,
    color: Color = cor30Porcento200,
    selectionMode: Boolean = false,
    onClickSelectionX: (() -> Unit),
    onLongClickX: (() -> Unit),
    content: @Composable () -> Unit
) {
    val isSelected = selectedItems.contains(item)
    val interactionSource = remember { MutableInteractionSource() }

    CompositionLocalProvider(LocalRippleTheme provides CustomRippleTheme) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .combinedClickable(
                    interactionSource = interactionSource,
                    indication = if (selectionMode) LocalIndication.current else null,
                    onClick = {
                        if (selectionMode) onClickSelectionX()
                    },
                    onLongClick = {
                        onLongClickX()
                    }
                )
                .background(
                    color = if (isSelected) cor30Porcento500 else when (item.difficultyLevel) {
                        SCDifficulty.Level1.levelMode -> levelGreen
                        SCDifficulty.Level2.levelMode -> levelYellow
                        SCDifficulty.Level3.levelMode -> levelRed
                        else -> Purple40
                    },
                    shape = RoundedCornerShape(6.dp)
                )
        )
        {
            Box(
                modifier = Modifier
                    .matchParentSize() // Ocupa tod.o o espaço disponível no Box pai
                    .padding(start = 6.dp) // Aplica o padding interno
                    .background( // Define a cor do padding interno
                        if (isSelected) cor30Porcento400 else color,
                        shape = RoundedCornerShape(
                            topStart = 0.dp,
                            bottomStart = 0.dp,
                            topEnd = 6.dp,
                            bottomEnd = 6.dp
                        )
                    )
            )
            content()
        }
    }
}

object CustomRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = LocalContentColor.current.copy(alpha = 0.3f)

    @Composable
    override fun rippleAlpha() = RippleAlpha(
        draggedAlpha = 0.01f,
        focusedAlpha = 0.01f,
        hoveredAlpha = 0.01f,
        pressedAlpha = 0.03f //controla a intensidade(cor) da onda do click
    )
}
//----------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowScope.CardForLazyColumn(
    text: String,
    isDottedBackground: Boolean,
    color: Color = corDoTextoClaroPadrao100,
    cardHeight: Int = 200,
    onClickX: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        modifier = Modifier
            .weight(0.5f)
            .height(cardHeight.dp)
            .padding(horizontal = 6.dp),
        onClick = { onClickX() }
    ) {
        Box {
            if (isDottedBackground) CanvasDottedBackground()
            Text(
                text = text,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(vertical = 9.dp, horizontal = 6.dp)
            )
        }
    }
}
//----------------------------------------------------------------------------
@Composable
fun SegmentedButtonRow(
    options: List<EnumQuestionAnswer>,
    selectedOption: EnumQuestionAnswer,
    boxWidth: Int = 120,
    boxHeight: Int = 40,
    onClickX: (option: EnumQuestionAnswer) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
    ) {
        options.forEachIndexed { index, option ->
            val isSelected = option == selectedOption
            val shape = when (index) {
                0 -> RoundedCornerShape(topStart = 6.dp, bottomStart = 6.dp)
                options.size - 1 -> RoundedCornerShape(topEnd = 6.dp, bottomEnd = 6.dp)
                else -> RoundedCornerShape(0.dp)
            }
            Surface(
                modifier = Modifier
                    .height(boxHeight.dp)
                    .width(boxWidth.dp),
                shape = shape,
                color = if (isSelected) corDoTextoEscuroPadrao700
                else corDoTextoClaroPadrao100,
                shadowElevation = 2.dp,
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isSelected) corDoTextoEscuroPadrao700
                    else cor30Porcento500
                )
            ) {
                Box(
                    modifier = Modifier
                        .clickable {
                            onClickX(option)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = option.str,
                        color = if (isSelected) cor30Porcento50 else cor30Porcento500,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    )
                }
            }
        }
    }
}
//----------------------------------------------------------------------------
@Composable
fun LevelChangeButton(
    selectedLevel: SCDifficulty,
    width: Int = 160,
    backgroundColor: Color = corDoTextoClaroPadrao100,
    onClickX: (selectedLevelx: SCDifficulty) -> Unit
) {
    ElevatedButton(
        onClick = {
            val selectedLevelx = when (selectedLevel) {
                SCDifficulty.Level1 -> SCDifficulty.Level2
                SCDifficulty.Level2 -> SCDifficulty.Level3
                SCDifficulty.Level3 -> SCDifficulty.Level1
            }
            onClickX(selectedLevelx)
        },
        border = null,
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier.width(width.dp),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor)
    ) {
        Text(
            text = selectedLevel.levelMode,
            fontSize = 18.sp,
            color = selectedLevel.color,
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.CenterHorizontally) //centraliza horizontalmente o texto dentro largura fixa do botão
        )
        Icon(
            painter = painterResource(id = selectedLevel.icon),
            tint = selectedLevel.color,
            contentDescription = "Icon selectedLevel",
        )
    }
}
//----------------------------------------------------------------------------
