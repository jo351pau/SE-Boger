package de.htwg.se.backgammon.model

private val INVALID_POSITION = 999

case class Move(
    val from: Int = INVALID_POSITION,
    val steps: Int,
    val player: Player = Player.None
) {
  def outOfBar = player != Player.None
}
