package de.htwg.se.backgammon.model

trait IDice {
  def roll: Int
  def roll(times: Int): List[Int]
}
