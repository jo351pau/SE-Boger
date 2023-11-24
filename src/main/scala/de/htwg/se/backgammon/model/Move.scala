package de.htwg.se.backgammon.model

private val BAR_POSITION = 999

case class Move(
    val from: Int,
    val steps: Int
)

class BearOffMove(val player: Player, steps: Int)
    extends Move(BAR_POSITION, steps)
