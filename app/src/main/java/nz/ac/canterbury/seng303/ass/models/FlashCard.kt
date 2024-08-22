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
                            "What is 2 + 2?" to true,
                            "What is the square root of 9?" to true,
                            "Is 5 a prime number?" to true,
                            "What is 10 / 0?" to false
                        )
                    ),
                    FlashCard(
                        2,
                        "Geography Quiz",
                        listOf(
                            "Is Paris the capital of France?" to true,
                            "Is the Amazon River the longest river in the world?" to false,
                            "Does Australia have more than 10 deserts?" to true,
                            "Is the Sahara desert located in South America?" to false
                        )
                    ),
                    FlashCard(
                        3,
                        "History Facts",
                        listOf(
                            "Did World War I begin in 1914?" to true,
                            "Was George Washington the first President of the USA?" to true,
                            "Did the Titanic sink in 1912?" to true,
                            "Was the Berlin Wall torn down in 1999?" to false
                        )
                    ),
                    FlashCard(
                        4,
                        "Science Trivia",
                        listOf(
                            "Is water made up of two hydrogen atoms and one oxygen atom?" to true,
                            "Does the Earth revolve around the Sun?" to true,
                            "Can humans breathe in space without assistance?" to false,
                            "Is the human body made up of 70% water?" to true
                        )
                    ),
                    FlashCard(
                        5,
                        "Literature Test",
                        listOf(
                            "Is '1984' written by George Orwell?" to true,
                            "Is 'To Kill a Mockingbird' a novel by J.K. Rowling?" to false,
                            "Is Shakespeare known for writing plays?" to true,
                            "Is 'The Great Gatsby' set in the Roaring Twenties?" to true
                        )
                    )
                )
            }
        }

    override fun getIdentifier(): Int {
        return id;
    }

}