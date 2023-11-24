package de.htwg.se.backgammon.model

private val BAR_POSITION = 999

case class Move(from: Int, steps: Int) {
  def whereToGo(game: Game) = game(from).occupier match {
    case Player.White => from + steps
    case _            => from - steps
  }
}

class BearOffMove(val player: Player, steps: Int)
    extends Move(BAR_POSITION, steps) {
  override def whereToGo(game: Game) = player match {
    case Player.White => steps - 1
    case _            => game.length - steps
  }
}
