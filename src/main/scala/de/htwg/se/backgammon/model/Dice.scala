package de.htwg.se.backgammon.model

import scala.util.Random

object Dice {
  def roll(): Int = Random.nextInt(6) + 1
}
