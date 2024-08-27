package nz.ac.canterbury.seng303.ass.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.ass.util.validateAnswer
import nz.ac.canterbury.seng303.ass.util.validateQuestion
import nz.ac.canterbury.seng303.ass.viewmodels.CreateCardViewModel
import nz.ac.canterbury.seng303.ass.viewmodels.FlashCardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateFlashCard(
    navController: NavController,
    createCardViewModel: CreateCardViewModel,
    cardViewModel: FlashCardViewModel,
    question: String,
    onQuestionChange: (String) -> Unit,
    tag: String,
    onTagChange: (String) -> Unit,
    answers: List<Pair<String, Boolean>>,
    onAnswersChange: (List<Pair<String, Boolean>>) -> Unit,
    createFlashCardFn: (String, String,  List<Pair<String, Boolean>>) -> Unit
) {
    cardViewModel.getCards()
    val context = LocalContext.current
    val defaultAnswers = listOf(
        "" to false,
        "" to false,
    )

    LaunchedEffect(Unit){
        createCardViewModel.resetModel()
    }

    // Make the entire screen scrollable by wrapping the content in a verticalScroll
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // Enable scrolling for the entire screen
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

        OutlinedTextField(
            value = tag,
            onValueChange = { onTagChange(it) },
            label = { Text("Tag (Optional)") },
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

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val (errorQuestion, isValidQuestion) = validateQuestion(
                    createCardViewModel.question,
                     "",
                    cardViewModel.cards.value
                )
                if (!isValidQuestion) {
                    Toast.makeText(context, errorQuestion, Toast.LENGTH_LONG).show()
                } else {
                    val (errorMsg, isValid) = validateAnswer(answers)
                    if (isValid) {
                        createFlashCardFn(question, tag , answers)
                        val builder = android.app.AlertDialog.Builder(context)
                        builder.setMessage("Created Card!")
                            .setCancelable(false)
                            .setPositiveButton("Ok") { dialog, id ->
                                onQuestionChange("")
                                onAnswersChange(defaultAnswers)
                                navController.navigate("CardList") {
                                    popUpTo("CreateCard") {inclusive = true}
                                }
                            }
                        val alert = builder.create()
                        alert.show()
                    } else {
                        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp) // Padding for the button
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
