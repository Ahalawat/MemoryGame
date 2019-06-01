package game.memroy.data.Repository

import game.memroy.data.model.Card

class CardsRepository() {
    fun getCards(count: Int): List<Card> {
        val urls = ArrayList<String>(count)
        urls.add("lion")
        urls.add("lion")
        urls.add("horse")
        urls.add("horse")
        urls.add("elephant")
        urls.add("elephant")
/*        urls.add(R.drawable.lion)
        urls.add(R.drawable.horse)
        urls.add(R.drawable.horse)
        urls.add(R.drawable.elephant)
        urls.add(R.drawable.elephant)*/
        //  var cardsLiveData : MutableLiveData<List<Card>> = MutableLiveData()
        val cardsList: MutableList<Card> = mutableListOf()
        for (i in 0 until count) {
            cardsList.add(Card(i, urls[i]))
        }
        //  cardsLiveData.value = cardsList
        return cardsList
    }
}