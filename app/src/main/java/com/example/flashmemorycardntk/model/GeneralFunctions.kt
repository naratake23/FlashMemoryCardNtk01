package com.example.flashmemorycardntk.model

import android.content.ClipboardManager
import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.example.flashmemorycardntk.data.entities.CardEntity

fun getSCDifficultyFromString(strLevel: String): SCDifficulty? {
    return when (strLevel) {
        SCDifficulty.Level1.levelMode -> SCDifficulty.Level1
        SCDifficulty.Level2.levelMode -> SCDifficulty.Level2
        SCDifficulty.Level3.levelMode -> SCDifficulty.Level3
        else -> null
    }
}
//-----------------------------------------------------------------------------
fun String.limitStringWithEllipses(maxLength: Int = 33): String {
    return if (this.length <= maxLength) this
    else this.take(maxLength - 3) + "..."
}
//-----------------------------------------------------------------------------
fun concatenateTextsAtCursorPosition(
    clipboardManager: ClipboardManager,
    textState: MutableState<TextFieldValue>
) {
    // Acessa o conteúdo da área de transferência
    val clipData = clipboardManager.primaryClip
    val item = clipData?.getItemAt(0)?.text?.toString()

    item?.let { clipText ->
        // Calcula a nova posição do cursor após inserir o texto
        val cursorPosition = textState.value.selection.start
        // Concatena o texto em torno da posição do cursor
        val newText = if (cursorPosition in textState.value.text.indices) {
            textState.value.text.substring(0, cursorPosition) + clipText +
                    textState.value.text.substring(cursorPosition)
        } else {
            textState.value.text + clipText
        }
        // Atualiza o estado com o novo texto e move o cursor para o final do texto colado
        textState.value = TextFieldValue(
            text = newText,
            selection = TextRange(cursorPosition + clipText.length)
        )
    }
}
//-----------------------------------------------------------------------------
//busca os cards que contem o texto inserido G2
fun cardSearch(cardList: List<CardEntity>, text: MutableState<String>): List<CardEntity> {
    return cardList.filter { card ->
        card.question.contains(text.value, ignoreCase = true)
                ||
                card.answer.contains(text.value, ignoreCase = true)
    }
}
//-----------------------------------------------------------------------------
//função para identificar e filtrar perguntas e respostas em um texto usando o padrão qx: para pergunta e ax: para resposta
fun extractQuestionsAndAnswersRegex(text: String, groupId: Long): List<CardEntity> {
    val questionPattern = "qx:(.*?)(?=ax:|$)".toRegex(RegexOption.DOT_MATCHES_ALL)
    val answerPattern = "ax:(.*?)(?=qx:|$)".toRegex(RegexOption.DOT_MATCHES_ALL)

    val questions = questionPattern.findAll(text).map { it.groupValues[1].trim() }.toList()
    val answers = answerPattern.findAll(text).map { it.groupValues[1].trim() }.toList()

    val cardEntities = mutableListOf<CardEntity>()

    for (i in questions.indices) {
        if (i < answers.size) {
            val question = questions[i]
            val answer = answers[i]
            // Criar um novo CardEntity com a pergunta e resposta
            val cardEntity = CardEntity(
                question = question,
                answer = answer,
                difficultyLevel = SCDifficulty.Level1.levelMode,
                successRate = 0,
                errorRate = 0,
                groupId = groupId
            )
            cardEntities.add(cardEntity)
        }
    }
    return cardEntities
}
//-----------------------------------------------------------------------------
