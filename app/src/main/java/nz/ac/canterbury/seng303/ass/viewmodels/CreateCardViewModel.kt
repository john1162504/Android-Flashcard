package nz.ac.canterbury.seng303.ass.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CreateCardViewModel: ViewModel() {
    private val defaultAnswers = listOf(
        "" to false,
        "" to false,
        "" to false,
        "" to false
    )

    var question by mutableStateOf("")
        private set

    fun updateQuestion(newQuestion: String) {
        question = newQuestion
    }

    var answers: List<Pair<String, Boolean>> by mutableStateOf(defaultAnswers)
        private set

    fun updateAnswers(newAnswers: List<Pair<String, Boolean>>) {
        answers = newAnswers
    }
}