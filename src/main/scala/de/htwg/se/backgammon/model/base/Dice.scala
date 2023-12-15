package de.htwg.se.backgammon.model.base

import de.htwg.se.backgammon.model.IDice
import scala.util.Random

class Dice extends IDice {
  def roll: Int = Random.nextInt(6) + 1

  def roll(times: Int): List[Int] = List.tabulate(times)(_ => roll)
}
