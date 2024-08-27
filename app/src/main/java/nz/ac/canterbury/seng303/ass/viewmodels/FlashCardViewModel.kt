package nz.ac.canterbury.seng303.ass.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng303.ass.datastore.Storage
import nz.ac.canterbury.seng303.ass.models.FlashCard
import kotlin.random.Random

class FlashCardViewModel(
    private val cardStorage: Storage<FlashCard>
) : ViewModel() {

    private val _cards = MutableStateFlow<List<FlashCard>>(emptyList())
    val cards: StateFlow<List<FlashCard>> = _cards

    private val _selectedCard = MutableStateFlow<FlashCard?>(null)
    val selectedCard: StateFlow<FlashCard?> = _selectedCard

    fun getCards() = viewModelScope.launch {
        cardStorage.getAll().catch {
            Log.e("FLASHCARD_VIEW_MODEL", it.toString())
        } .collect {_cards.emit(it)}
    }

    fun loadDefaultCards() = viewModelScope.launch {
        if (cardStorage.getAll().first().isEmpty()) {
            Log.d("FLASHCARD_VIEW_MODEL", "Inserting default flashcards")
            cardStorage.insertAll(FlashCard.getFlashCards())
                .catch { Log.w("FLASHCARD_VIEW_MODEL", "Could not insert default cards") }
                .collect { Log.d("FLASHCARD_VIEW_MODEL", "Default flashcards inserted") }
                _cards.emit(FlashCard.getFlashCards())
        }
    }

    fun getCardById(cardId: Int?) = viewModelScope.launch {
        if (cardId != null) {
            _selectedCard.value = cardStorage.get { it.getIdentifier() == cardId }.first()
        } else {
            _selectedCard.value = null
        }
    }

    fun createCard(question: String, tag: String, answers: List<Pair<String, Boolean>>) = viewModelScope.launch {
        val card = FlashCard(
            id = Random.nextInt(0, Int.MAX_VALUE),
            question = question,
            tag = tag,
            answers = answers
        )
        cardStorage.insert(card).catch { Log.e("CARD_VIEW_MODEL", "Could not insert card") }
            .collect()
        cardStorage.getAll().catch { Log.e("CARD_VIEW_MODEL", it.toString()) }
            .collect {_cards.emit(it)}
    }

    fun editCardById(cardId: Int?, card: FlashCard) = viewModelScope.launch {
        Log.d("CARD_VIEW_MODEL", "Editing card: $cardId")
        if (cardId != null) {
            cardStorage.edit(cardId, card).collect()
            cardStorage.getAll().catch { Log.e("CARD_VIEW_MODEL", it.toString()) }
                .collect { _cards.emit(it) }
        }
    }

    fun deleteCardById(cardId: Int?) = viewModelScope.launch {
        Log.d("CARD_VIEW_MODEL", "Deleting card: $cardId")
        if (cardId != null) {
            cardStorage.delete(cardId).collect()
            cardStorage.getAll().catch { Log.e("CARD_VIEW_MODEL", it.toString()) }
                .collect { _cards.emit(it) }
        }
    }

    fun getNextCardId(currentCardId: Int?, inputTag: String? = null): Int {
        val cardList = _cards.value
        val filteredCardList = inputTag?.let { tag -> cardList.filter { it.tag == tag } } ?: cardList
        val currentIndex = filteredCardList.indexOfFirst { it.getIdentifier() == currentCardId }

        return if (currentIndex != -1) {
            val nextIndex = (currentIndex + 1) % filteredCardList.size
            filteredCardList[nextIndex].getIdentifier()
        } else {
            filteredCardList.firstOrNull()?.getIdentifier() ?: -1 // Return -1 if list is empty
        }
    }

    fun getPreviousCardId(currentCardId: Int?, inputTag: String? = null): Int {
        val cardList = _cards.value
        val filteredCardList = inputTag?.let { tag -> cardList.filter { it.tag == tag } } ?: cardList
        val currentIndex = filteredCardList.indexOfFirst { it.getIdentifier() == currentCardId }

        return if (currentIndex != -1) {
            val prevIndex = (currentIndex - 1 + filteredCardList.size) % filteredCardList.size
            filteredCardList[prevIndex].getIdentifier()
        } else {
            filteredCardList.lastOrNull()?.getIdentifier() ?: -1 // Return -1 if list is empty
        }
    }
}