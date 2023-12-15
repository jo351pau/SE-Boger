package de.htwg.se.backgammon.model

trait IDice {
  def roll: int
  def roll(times: Int): List[Int]
}
