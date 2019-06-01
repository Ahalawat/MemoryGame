package game.memroy.data.model

class Level(val level: Int = 1) {
    var row: Int = 3
    var column: Int = 2
    var totalTimeInSecs = 100


    init {
        setData(level)
    }

    fun getCardsCount(): Int {
        return row * column
    }

    private fun setData(level: Int) {
        when (level) {
            1 -> {
                row = 3
                column = 2
            }
            2 -> {
                row = 4
                column = 3
            }
            else -> {
                row = 5
                column = 2
            }
        }
    }


}