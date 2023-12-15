package de.htwg.se.backgammon.model

private val BAR_POSITION = 999

case class Move(from: Int, steps: Int) extends Input with IMove{
  def whereToGo(game: IGame) = game(from).occupier match {
    case Player.White => from + steps
    case _            => from - steps
  }
}
class DefinedMove(player: Player, from: Int, to: Int)
    extends Move(
      from,
      player match {
        case Player.White => to - from
        case _            => from - to
      }
    ) {
  override def whereToGo(game: IGame) = to
}

class BearInMove(val player: Player, steps: Int)
    extends Move(BAR_POSITION, steps) {
  override def whereToGo(game: IGame) = player match {
    case Player.White => steps - 1
    case _            => game.length - steps
  }
}

class BearOffMove(from: Int, steps: Int) extends Move(from, steps) {
  def isValid(game: Game) = game(from).occupier match {
    case Player.White => whereToGo(game) == game.length
    case _            => whereToGo(game) == -1
  }

  def movesToField(game: IGame) =
    whereToGo(game) < game.length && whereToGo(game) >= 0
}

class NoMove extends Move(0, 0)
