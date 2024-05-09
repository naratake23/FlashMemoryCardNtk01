package com.example.flashmemorycardntk.model

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.Color
import com.example.flashmemorycardntk.ui.theme.cor30Porcento50
import com.example.flashmemorycardntk.ui.theme.cor30Porcento600
import com.example.flashmemorycardntk.ui.theme.corDoTextoEscuroPadrao800
import com.example.flashmemorycardntk.ui.theme.levelGreen800
import com.example.flashmemorycardntk.ui.theme.swipeGreenColor50
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow


object SnackbarManager {
    private val _messages = MutableStateFlow<SnackbarMessage?>(null)
    val messages = _messages.asSharedFlow()

    suspend fun showMessage(message: SnackbarMessage) {
        _messages.emit(message)
    }
    suspend fun showGreenMessage(message: String) {
        _messages.emit(
            SnackbarMessage(
                text = message,
                backgroundColor = swipeGreenColor50,
                textColor = levelGreen800
            )
        )
    }
    fun clearMessage() {
        _messages.value = null
    }
}

data class SnackbarMessage(
    val text: String,
    val textColor: Color = cor30Porcento600,
    val actionText: String = "",
    val actionTextColor: Color = corDoTextoEscuroPadrao800,
    val duration: SnackbarDuration = SnackbarDuration.Short,
    val backgroundColor: Color = cor30Porcento50,
    val screenSource: EnumSnackbarActionScreens? = null
)