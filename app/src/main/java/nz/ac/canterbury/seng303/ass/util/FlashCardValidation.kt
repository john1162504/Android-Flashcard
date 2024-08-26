package nz.ac.canterbury.seng303.ass.util

import nz.ac.canterbury.seng303.ass.models.FlashCard

fun validateQuestion(newQuestion: String, question: String,  cards: List<FlashCard>):Pair<String,Boolean> {
    if (newQuestion.isEmpty()) {
        return Pair("Question can not be empty", false)
    }
    for (card in cards) {
        if (newQuestion == card.question && card.question != question) {
            return Pair("Question must be unique", false)
        }
    }
    return Pair("", true)
}

fun validateAnswer(answers: List<Pair<String, Boolean>>): Pair<String, Boolean> {
    var hasCorrectAnswer = false
    if (answers.size < 2) {
        return Pair("A flash card must have at least two possible answers.", false)
    }
    for (answer in answers) {
        val (text, isCorrect) = answer
        if (text.isBlank()) {
            return Pair("Answer text cannot be empty.", false)
        }
        if (isCorrect) {
            if (hasCorrectAnswer) {
                return Pair("A flash card can only have one correct answer.", false)
            }
            hasCorrectAnswer = true
        }
    }
    if (!hasCorrectAnswer) {
        return Pair("A flash card must have exactly one correct answer.", false)
    }
    return Pair("", true)
}
