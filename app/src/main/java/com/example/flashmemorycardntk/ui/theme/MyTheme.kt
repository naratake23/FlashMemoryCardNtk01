package com.example.flashmemorycardntk.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flashmemorycardntk.R

@Composable
fun MyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val minhaFonte = FontFamily(
        Font(R.font.noto_sans_variable_font)
    )
    val myTypographyP = Typography(

        //fonte dos Texts e OutlinedTextField
        bodyLarge = TextStyle(
            fontFamily = minhaFonte,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            color = corDoTextoEscuroPadrao900
        ),

//        bodySmall = TextStyle(
//            fontFamily = minhaFonte,
//            fontWeight = FontWeight.Normal,
//            fontSize = 20.sp
//        ),

        //fonte do TextField
        bodyMedium = TextStyle(
            fontFamily = minhaFonte,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            color = corDoTextoEscuroPadrao900
        ),

        //fonte dos botoes
        labelLarge = TextStyle(
            fontFamily = minhaFonte,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
//            color = corDoTextoClaroPadrao1
        ),
        //fonte da BottomNavigation
        labelMedium = TextStyle(
            fontFamily = minhaFonte,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp
        ),
        //fonte do titulo da Dialog
        headlineSmall = TextStyle(
            fontFamily = minhaFonte,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        ),
    )
    val myShapes = Shapes(
        //card
        medium = RoundedCornerShape(6.dp),
        //float action button
        large = RoundedCornerShape(8.dp),
        //dialog box
        extraLarge = RoundedCornerShape(6.dp),

        small = RoundedCornerShape(6.dp),
        extraSmall = RoundedCornerShape(6.dp),
    )

    MaterialTheme(
        colorScheme = LightColorSchemeX,
        typography = myTypographyP,
        shapes = myShapes,
        content = content
    )
}