package nz.ac.canterbury.seng303.ass.models

class FlashCard (
    val id: Int,
    val question: String,
    val answers: List<Pair<String, Boolean>>): Identifiable {

        companion object {
            fun getFlashCards(): List<FlashCard> {
                return listOf(
                    FlashCard(
                        1,
                        "Math Basics",
                        listOf(
                            "What is 2 + 2?" to false,
                            "4" to true,  // Correct answer
                            "5" to false,
                            "3" to false
                        )
                    ),
                    FlashCard(
                        2,
                        "Geography Quiz",
                        listOf(
                            "Is Paris the capital of France?" to false,
                            "Paris" to true,  // Correct answer
                            "Berlin" to false,
                            "London" to false
                        )
                    ),
                    FlashCard(
                        3,
                        "History Facts",
                        listOf(
                            "Did World War I begin in 1914?" to false,
                            "Yes" to true,  // Correct answer
                            "No" to false
                        )
                    ),
                    FlashCard(
                        4,
                        "Science Trivia",
                        listOf(
                            "Is water made up of two hydrogen atoms and one oxygen atom?" to false,
                            "Yes" to true,  // Correct answer
                            "No" to false
                        )
                    ),
                    FlashCard(
                        5,
                        "Literature Test",
                        listOf(
                            "Is '1984' written by George Orwell?" to false,
                            "Yes" to true,  // Correct answer
                            "No" to false
                        )
                    )
                )
            }
        }

    override fun getIdentifier(): Int {
        return id
    }

}