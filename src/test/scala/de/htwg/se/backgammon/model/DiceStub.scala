package de.htwg.se.backgammon.model

import de.htwg.se.backgammon.model.IDice

class DiceStub(results: Int*) extends IDice {
    var index: Int = 0 
    def roll: Int = { 
        val num = results(index)
        index += 1

        num
    }

    def roll(times: Int): List[Int] = List.tabulate(times)(_ => roll)
}
