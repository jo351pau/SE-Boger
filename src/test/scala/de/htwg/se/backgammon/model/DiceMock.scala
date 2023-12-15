package de.htwg.se.backgammon.model

class DiceMock(results: Int*) extends IDice {
    var index: Int = 0 
    def roll: Int = { 
        val num = results(index)
        index = index + 1

        num
    }

    def roll(times: Int): List[Int] = List.tabulate(times)(_ => roll)
}
