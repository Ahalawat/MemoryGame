package game.memroy.viewModels

import androidx.lifecycle.ViewModel
import game.memroy.data.Repository.CardsRepository
import game.memroy.data.model.Card

class CardsViewModel : ViewModel() {
    private val cardsRepository: CardsRepository

    init {
        cardsRepository = CardsRepository()
    }

    fun getCards(count: Int): List<Card> {
        return cardsRepository.getCards(count)
    }
}