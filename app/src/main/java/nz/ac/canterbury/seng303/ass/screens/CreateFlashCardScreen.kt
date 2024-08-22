package nz.ac.canterbury.seng303.ass.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateFlashCard(
    navController: NavController,
    question: String,
    onQuestionChange: (String) -> Unit,
    answers: List<Pair<String, Boolean>>,
    onAnswersChange: (List<Pair<String, Boolean>>) -> Unit,
    createFlashCardFn: (String, List<Pair<String, Boolean>>) -> Unit
) {

    val context = LocalContext.current
    val defaultAnswers = listOf(
        "" to false,
        "" to false,
        "" to false,
        "" to false
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = question,
            onValueChange = { onQuestionChange(it) },
            label = { Text("Question") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // Display each answer with a delete button
        answers.forEachIndexed { index, answer ->
            AnswerRow(
                answer = answer.first,
                onAnswerChange = { newAnswer ->
                    val updatedAnswers = answers.toMutableList().apply {
                        this[index] = newAnswer to answer.second
                    }
                    onAnswersChange(updatedAnswers)
                },
                isCorrect = answer.second,
                onCheckedChange = { isChecked ->
                    val updatedAnswers = answers.toMutableList().apply {
                        this[index] = answer.first to isChecked
                    }
                    onAnswersChange(updatedAnswers)
                },
                onDeleteClick = {
                    val updatedAnswers = answers.toMutableList().apply {
                        removeAt(index)
                    }
                    onAnswersChange(updatedAnswers)
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Button to add a new answer
        Button(
            onClick = {
                val updatedAnswers = answers.toMutableList().apply {
                    add("" to false) // Add a new empty answer
                }
                onAnswersChange(updatedAnswers)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Add Answer")
        }

        Button(
            onClick = {
                createFlashCardFn(question, answers)
                val builder = android.app.AlertDialog.Builder(context)
                builder.setMessage("Created Card!")
                    .setCancelable(false)
                    .setPositiveButton("Ok") { dialog, id ->
                        onQuestionChange("")
                        onAnswersChange(defaultAnswers)
                        navController.navigate("CardList")
                    }
                    .setNegativeButton("Cancel") { dialog, id -> dialog.dismiss() }
                val alert = builder.create()
                alert.show()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .fillMaxWidth()
        ) {
            Text(text = "Create")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswerRow(
    answer: String,
    onAnswerChange: (String) -> Unit,
    isCorrect: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = answer,
            onValueChange = onAnswerChange,
            label = { Text("Answer") },
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )
        Checkbox(
            checked = isCorrect,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.padding(end = 8.dp)
        )
        IconButton(onClick = onDeleteClick) {
            Icon(
                imageVector = Icons.Default.Delete, // You can use an appropriate icon from Material Icons
                contentDescription = "Delete Answer"
            )
        }
    }
}