package de.htwg.se.backgammon.model

case class Move(from: Int, to: Int) {

  def isWrongDirection(player: Player) = (player == Player.White && from > to)
    || (player == Player.Black && from < to)

}
