package nz.ac.canterbury.seng303.ass.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.ass.models.FlashCard
import nz.ac.canterbury.seng303.ass.viewmodels.FlashCardViewModel

@Composable
fun PlayCard(navController: NavController,
             cardViewModel: FlashCardViewModel,
             tag: String? = null
             ) {
    cardViewModel.getCards()
    val context = LocalContext.current
    var currentIndex by remember { mutableStateOf(0) }
    val cards: List<FlashCard> by cardViewModel.cards.collectAsState()

    val filteredCards = if (tag != null) {
        cards.filter { it.tag == tag }
    } else {
        cards
    }

    val shuffledCards by remember { mutableStateOf(filteredCards.shuffled()) }
    var answers by remember { mutableStateOf<List<Pair<String, Boolean>>>(emptyList()) }
    var selectedAnswer by remember { mutableStateOf<Pair<String, Boolean>?>(null) }
    var results by remember { mutableStateOf<List<Pair<String, Boolean>>>(emptyList()) }

    // Update answers when currentIndex changes
    LaunchedEffect(currentIndex) {
        if (cards.isNotEmpty() && currentIndex < shuffledCards.size) {
            answers = shuffledCards[currentIndex].answers.shuffled()
            selectedAnswer = null
        }
    }

    if (cards.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "You do not have any cards. Please create one to get started.",
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 18.sp,
                    color = Color.Gray
                ),
                textAlign = TextAlign.Center
            )
        }
    } else {
        // Playing
        if (currentIndex < shuffledCards.size) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 2.dp,
                            color = Color.DarkGray,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = shuffledCards[currentIndex].question,
                            fontSize = 25.sp,
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = shuffledCards[currentIndex].tag,
                            fontSize = 14.sp,
                            color = Color.White,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .background(Color(0xFF7FF7BB), shape = RoundedCornerShape(16.dp))
                                .border(
                                    width = 2.dp,
                                    color = Color(0xFF7FF7BB),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(3.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                answers.forEach { answer ->
                    CardRow(
                        answer = answer.first,
                        isChecked = selectedAnswer == answer,
                        onCheckedChange = {
                            selectedAnswer = answer
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp)) // Adds space between cards
                }
                Spacer(modifier = Modifier.weight(1f)) // Spacer to push the button to the bottom

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 32.dp)
                        .padding(horizontal = 40.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "${currentIndex + 1}/${shuffledCards.size}")
                    Button(
                        onClick = {
                            if (selectedAnswer == null) {
                                Toast.makeText(
                                    context,
                                    "You need to select an answer",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val message =
                                    if (selectedAnswer?.second == true) "Correct" else "Incorrect"
                                val isCorrect = selectedAnswer?.second == true
                                results =
                                    results + (shuffledCards[currentIndex].question to isCorrect)
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                currentIndex++
                                selectedAnswer = null
                            }
                        },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text(text = "Submit")
                    }
                }
            }
        } else {
            // Summary
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val correctAnswersCount = results.count { it.second }
                Text(text = "Summary",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(text = "Score: ${correctAnswersCount}/${shuffledCards.size}",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.W400,
                )
                Spacer(modifier = Modifier.height(16.dp)) // Adds space between cards
                results.forEach { result ->
                    SummaryRow(result = result)
                    Spacer(modifier = Modifier.height(16.dp)) // Adds space between cards
                }
            }
        }
    }
}


@Composable
fun CardRow(
    answer: String,
    isChecked: Boolean,
    onCheckedChange: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(16.dp),
            )
            .clickable { onCheckedChange() }  // Make the row clickable
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = answer,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        )
        Checkbox(
            checked = isChecked,
            onCheckedChange = { checked -> if (checked) onCheckedChange() }
        )
    }
}

@Composable
fun SummaryRow(
    result: Pair<String, Boolean>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(32.dp),
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left Text with weight to take up available space and center it
        Text(
            text = result.first,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            textAlign = TextAlign.Center
        )

        // Right Icon
        val icon: ImageVector = if (result.second) Icons.Filled.Check else Icons.Filled.Close
        Icon(
            imageVector = icon,
            contentDescription = if (result.second) "Correct" else "Incorrect",
            tint = if (result.second) Color.Green else Color.Red,
            modifier = Modifier
                .size(24.dp),
        )
    }
}