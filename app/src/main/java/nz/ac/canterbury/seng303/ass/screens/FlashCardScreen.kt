package nz.ac.canterbury.seng303.ass.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import nz.ac.canterbury.seng303.ass.models.FlashCard
import nz.ac.canterbury.seng303.ass.viewmodels.FlashCardViewModel

@Composable
fun FlashCard(cardId: String, cardViewModel: FlashCardViewModel) {
    cardViewModel.getCardById(cardId = cardId.toIntOrNull())
    val selectedCardState by cardViewModel.selectedCard.collectAsState(null)
    val card: FlashCard? = selectedCardState


}