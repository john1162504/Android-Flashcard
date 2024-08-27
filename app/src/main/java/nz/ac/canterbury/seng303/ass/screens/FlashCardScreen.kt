package nz.ac.canterbury.seng303.ass.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.ass.models.FlashCard
import nz.ac.canterbury.seng303.ass.viewmodels.FlashCardViewModel

@Composable
fun FlashCard(
    cardId: String,
    cardViewModel: FlashCardViewModel,
    navController: NavController) {
    cardViewModel.getCardById(cardId = cardId.toIntOrNull())
    val selectedCardState by cardViewModel.selectedCard.collectAsState(null)
    val card: FlashCard? = selectedCardState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    change.consume()
                    when {
                        dragAmount > 0 -> {
                            val nextId = cardViewModel.getNextCardId(cardId.toIntOrNull())
                            navController.navigate("FlashCard/${nextId}") {
                                popUpTo("CardList") { inclusive = false }

                            }
                        }

                        dragAmount < 0 -> {
                            val previousId = cardViewModel.getPreviousCardId(cardId.toIntOrNull())
                            navController.navigate("FlashCard/${previousId}") {
                                popUpTo("CardList") { inclusive = false }
                            }
                        }
                    }
                }
            }
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
            if (card != null) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = card.question,
                        fontSize = 25.sp,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = card.tag,
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
        }

        Spacer(modifier = Modifier.height(16.dp))

        card?.answers?.forEach { answer ->
            SummaryRow(
                result = answer
            )
            Spacer(modifier = Modifier.height(16.dp)) // Adds space between cards
        }
    }

}