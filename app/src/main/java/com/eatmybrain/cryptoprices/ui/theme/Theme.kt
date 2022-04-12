package com.eatmybrain.cryptoprices.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val DarkColorPalette = darkColors(
    primary = MarineBlue,
    background = LightGrey,
    surface = LightGrey,
    onBackground = Color.White,
    onSurface = Color.White,
)



@Composable
fun CryptoPricesTheme( content: @Composable () -> Unit) {


    MaterialTheme(
        colors = DarkColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}