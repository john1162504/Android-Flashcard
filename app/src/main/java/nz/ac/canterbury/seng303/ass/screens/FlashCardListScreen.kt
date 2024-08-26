package nz.ac.canterbury.seng303.ass.screens

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import nz.ac.canterbury.seng303.ass.models.FlashCard
import nz.ac.canterbury.seng303.ass.viewmodels.FlashCardViewModel

@Composable
fun CardList(navController: NavController, cardViewModel: FlashCardViewModel ) {
    cardViewModel.getCards()
    val cards: List<FlashCard> by cardViewModel.cards.collectAsState()
    LazyColumn {
        items(cards) { card ->
            CardItem(navController = navController, card = card, deleteFn = {id: Int -> cardViewModel.deleteCardById(id) })
            Divider()
        }
    }
}

@Composable
fun CardItem(navController: NavController, card: FlashCard,  deleteFn: (id:Int) -> Unit) {
    val context = LocalContext.current
    fun searchWeb(query: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://www.google.com/search?q=$query")
        }
        context.startActivity(intent)
    }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { navController.navigate("FlashCard/${card.id}") },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .weight(3f)
                .fillMaxWidth()
        ) {
            Text(
                text = card.question,
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.align(Alignment.CenterHorizontally) // Aligns vertically within the Row
            )
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        IconButton(onClick = {
            searchWeb(card.question)
        },
            modifier = Modifier
                .size(56.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Search",
                tint = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.DarkGray, shape = RoundedCornerShape(12.dp))
            )
        }
        IconButton(onClick = {
                navController.navigate("EditCard/${card.id}")
        },
            modifier = Modifier
                .size(56.dp)
            ) {
            Icon(
                imageVector = Icons.TwoTone.Edit,
                contentDescription = "Edit",
                tint = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(25.dp)
                    .background(Color.Blue, shape = RoundedCornerShape(12.dp))
            )
        }
        IconButton(onClick = {
            val builder = AlertDialog.Builder(context)
            builder.setMessage("Delete Flashcard \"${card.question}\"?")
                .setCancelable(false)
                .setPositiveButton("Delete") { dialog, id ->
                        deleteFn(card.id)
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, id ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        },
            modifier = Modifier
                .size(56.dp)
            ) {
            Icon(
                imageVector = Icons.TwoTone.Delete,
                contentDescription = "Delete",
                tint = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(25.dp)
                    .background(Color.Red, shape = RoundedCornerShape(12.dp))
            )
        }
    }
}
