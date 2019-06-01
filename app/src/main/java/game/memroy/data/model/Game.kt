package game.memroy.data.model

class Game(var level: Level, var score: Int = 0) {
    fun getCardsCount(): Int {
        return level.getCardsCount()
    }

    fun setScore(timeRemaing: String) {
        val timeRemaingInMillis = timeRemaing.toInt()
        score += (timeRemaingInMillis + 10) * level.level
    }
}