package nz.ac.canterbury.seng303.ass.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.ass.viewmodels.FlashCardViewModel

@Composable
fun Tag(
    navController: NavController,
    cardViewModel: FlashCardViewModel
) {
    // Fetch cards from the view model
    cardViewModel.getCards()
    val cards by cardViewModel.cards.collectAsState()

    // Extract unique tags
    val uniqueTags = remember(cards) {
        cards.map { it.tag }.toSet() // Convert list to set to get unique tags
    }

    // Display tags
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Tags",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        uniqueTags.forEach { tag ->
            Button(
                onClick = {
                    // Navigate to CardList with the selected tag
                    navController.navigate("CardList/$tag")
                },
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(text = tag)
            }
        }
    }
}
