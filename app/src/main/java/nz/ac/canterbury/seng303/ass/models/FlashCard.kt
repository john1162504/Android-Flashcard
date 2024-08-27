package nz.ac.canterbury.seng303.ass.models

class FlashCard (
    val id: Int,
    val question: String,
    val tag: String,
    val answers: List<Pair<String, Boolean>>): Identifiable {


    companion object {
        fun getFlashCards(): List<FlashCard> {
            return listOf(
                FlashCard(
                    id = 1,
                    question = "What is the capital of France?",
                    tag = "Geography",
                    answers = listOf(
                        "Paris" to true,
                        "London" to false,
                        "Berlin" to false,
                        "Madrid" to false
                    )
                ),
                FlashCard(
                    id = 2,
                    question = "Which continent is France located in?",
                    tag = "Geography",
                    answers = listOf(
                        "Europe" to true,
                        "Asia" to false,
                        "Africa" to false,
                        "Australia" to false
                    )
                ),
                FlashCard(
                    id = 3,
                    question = "Who wrote 'Romeo and Juliet'?",
                    tag = "Literature",
                    answers = listOf(
                        "William Shakespeare" to true,
                        "Charles Dickens" to false,
                        "Mark Twain" to false,
                        "Jane Austen" to false
                    )
                ),
                FlashCard(
                    id = 4,
                    question = "What is the powerhouse of the cell?",
                    tag = "Biology",
                    answers = listOf(
                        "Mitochondria" to true,
                        "Nucleus" to false,
                        "Ribosome" to false,
                        "Chloroplast" to false
                    )
                ),
                FlashCard(
                    id = 5,
                    question = "What is the chemical symbol for water?",
                    tag = "Chemistry",
                    answers = listOf(
                        "H2O" to true,
                        "CO2" to false,
                        "NaCl" to false,
                        "O2" to false
                    )
                )
            )
        }
    }

    override fun getIdentifier(): Int {
        return id
    }

}