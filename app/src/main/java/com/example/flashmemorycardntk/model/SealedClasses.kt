package com.example.flashmemorycardntk.model

import androidx.compose.ui.graphics.Color
import com.example.flashmemorycardntk.R
import com.example.flashmemorycardntk.ui.theme.levelGreen
import com.example.flashmemorycardntk.ui.theme.levelRed
import com.example.flashmemorycardntk.ui.theme.levelYellow

sealed class SCBottomAppBar(
    val route: String,
    val title: String,
    val icon: Int,
) {
    object Play: SCBottomAppBar(
        route = "play",
        title = "Play",
        icon = R.drawable.cards_outline
    )
    object Shuffle: SCBottomAppBar(
        route = "shuffle",
        title = "Shuffle",
        icon = R.drawable.shuffle
    )
    object Group: SCBottomAppBar(
        route = "group",
        title = "Group",
        icon = R.drawable.format_list_bulleted_square
    )
    object Add: SCBottomAppBar(
        route = "add",
        title = "Add",
        icon = R.drawable.plus
    )
}
//--------------------------------------------------------------------------
sealed class SCDifficulty(
    val levelMode: String,
    val icon: Int,
    val color: Color
) {
    object Level1 : SCDifficulty(
        "Easy",
        R.drawable.signal_cellular_1,
        levelGreen
    )

    object Level2 : SCDifficulty(
        "Medium",
        R.drawable.signal_cellular_2,
        levelYellow
    )

    object Level3 : SCDifficulty(
        "Hard",
        R.drawable.signal_cellular_3,
        levelRed
    )
}
//-----------------------------------------------------------------------------