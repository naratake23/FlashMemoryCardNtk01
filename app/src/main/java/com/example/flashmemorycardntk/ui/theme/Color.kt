package com.example.flashmemorycardntk.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)



val customGray350 = Color(0xFFdbdbdb)

val levelGreen = Color(0xFF00bb00)
val levelGreen800 = Color(0xFF008800)
val levelYellow = Color(0xFFffb300)
val levelRed = Color(0xFFF44708)

val swipeGreenColor100 = Color(0xFFcfffcb)
val swipeGreenColor50 = Color(0xFFecffea)
val swipeRedColor100 = Color(0xFFffd3d9)

val fabGreen500 = Color(0xFF58f35b)
val fabRed400 = Color(0xFFff635e)

val vermelhotextoSnackbar = Color(0xFF880000)
val vermelhoFundoSnackbar = Color(0xFFffecec)

val cor10Porcento700 = Color(0xFFffa000) //amarelo
val cor10Porcento600 = Color(0xFFffb300) //amarelo
val cor10Porcento500 = Color(0xFFFFC107) //amarelo
val cor10Porcento400 = Color(0xFFffca28) //amarelo
val cor10Porcento300 = Color(0xFFffd54f) //amarelo
val cor10Porcento200 = Color(0xFFffe082) //amarelo
val cor10Porcento100 = Color(0xFFffecb3) //amarelo
val cor10Porcento50 = Color(0xFFfff8e1) //amarelo
val cor30Porcento600 = Color(0xFF838383)
val cor30Porcento500 = Color(0xFFadadad)
val cor30Porcento400 = Color(0xFFcacaca)
val cor30Porcento300 = Color(0xFFececec)
val cor30Porcento200 = Color(0xFFf2f2f2)
val cor30Porcento50 = Color(0xFFfcfcfc)
val corDoTextoEscuroPadrao900 = Color(0xFF2c2c2c)
val corDoTextoEscuroPadrao800 = Color(0xFF4e4e4e)
val corDoTextoEscuroPadrao700 = Color(0xFF6e6e6e)
val corDoTextoClaroPadrao100 = Color(0xFFf7f7f7)


val LightColorSchemeX = lightColorScheme(
    primary = cor10Porcento500,
//cor da bola de seleção do switch
    onPrimary = cor30Porcento50,
    primaryContainer = cor10Porcento700,
    onPrimaryContainer = cor10Porcento100,
    secondary = cor10Porcento400,
    onSecondary = corDoTextoEscuroPadrao900,
    secondaryContainer = cor10Porcento600,
    onSecondaryContainer = cor10Porcento200,
    tertiary = cor30Porcento600,
    onTertiary = corDoTextoClaroPadrao100,
    tertiaryContainer = cor30Porcento400,
    onTertiaryContainer = cor30Porcento200,
    background = cor30Porcento50,
    onBackground = corDoTextoEscuroPadrao900,
    surface = cor30Porcento300,
    onSurface = corDoTextoEscuroPadrao900,
    surfaceVariant = cor30Porcento200,
    onSurfaceVariant = corDoTextoEscuroPadrao800,
    outline = corDoTextoEscuroPadrao700,
    inverseOnSurface = corDoTextoClaroPadrao100,
    inverseSurface = cor30Porcento600,
    inversePrimary = cor10Porcento200,
//cor da bottom bar app
    surfaceTint = cor30Porcento50,
    scrim = cor30Porcento500
)