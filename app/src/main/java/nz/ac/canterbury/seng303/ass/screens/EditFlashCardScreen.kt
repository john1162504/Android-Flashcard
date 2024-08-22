package nz.ac.canterbury.seng303.ass.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.ass.models.FlashCard
import nz.ac.canterbury.seng303.ass.viewmodels.EditCardViewModel
import nz.ac.canterbury.seng303.ass.viewmodels.FlashCardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCard(
    cardId: String,
    editCardViewModel: EditCardViewModel,
    cardViewModel: FlashCardViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val selectedCardState by cardViewModel.selectedCard.collectAsState(null)
    val card: FlashCard? = selectedCardState

    LaunchedEffect(card) {
        if (card == null) {
            cardViewModel.getCardById(cardId.toIntOrNull())
        } else {
            editCardViewModel.setDefaultCard(card)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = editCardViewModel.question,
            onValueChange = { editCardViewModel.updateQuestion(it) },
            label = { Text("Question") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // Display each answer with a delete button
        editCardViewModel.answers.forEachIndexed { index, answer ->
            AnswerRow(
                answer = answer.first,
                onAnswerChange = { newAnswer ->
                    val updatedAnswers = editCardViewModel.answers.toMutableList().apply {
                        this[index] = newAnswer to answer.second
                    }
                    editCardViewModel.updateAnswers(updatedAnswers)
                },
                isCorrect = answer.second,
                onCheckedChange = { isChecked ->
                    val updatedAnswers = editCardViewModel.answers.toMutableList().apply {
                        this[index] = answer.first to isChecked
                    }
                    editCardViewModel.updateAnswers(updatedAnswers)
                },
                onDeleteClick = {
                    val updatedAnswers = editCardViewModel.answers.toMutableList().apply {
                        removeAt(index)
                    }
                    editCardViewModel.updateAnswers(updatedAnswers)
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Button to add a new answer
        Button(
            onClick = {
                val updatedAnswers = editCardViewModel.answers.toMutableList().apply {
                    add("" to false) // Add a new empty answer
                }
                editCardViewModel.updateAnswers(updatedAnswers)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Add Answer")
        }

        Button(
            onClick = {
                cardViewModel.editCardById(cardId.toIntOrNull(),
                    card = FlashCard(cardId.toInt(),
                        editCardViewModel.question, editCardViewModel.answers))
                val builder = android.app.AlertDialog.Builder(context)
                builder.setMessage("Edited card!")
                    .setCancelable(false)
                    .setPositiveButton("Ok") { dialog, id ->
                        navController.navigate("CardList")
                    }
                    .setNegativeButton("Cancel") { dialog, id -> dialog.dismiss() }
                val alert = builder.create()
                alert.show()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .fillMaxWidth()
        ) {
            Text(text = "Save")
        }
    }
}
