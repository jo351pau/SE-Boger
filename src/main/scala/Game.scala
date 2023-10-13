class Game(val player1: List[Row], val player2: List[Row]){

    def this(numberOfFields: Int) = this(List.fill(numberOfFields)(new Row()), List.fill(numberOfFields)(new Row()))

    def copy(newPlayer1: List[Row], newPlayer2: List[Row]): Game = new Game(newPlayer1, newPlayer2)

    override def toString: String = {
        val output1 = player1.mkString("\t")
        val output2 = player2.mkString("\t")

        val midline = (0 until player1.length).map(_ => "-").mkString("\t")

        return output1 + "\n" + midline + "\n" + output2
    }
}