package de.htwg.se.backgammon.model.strategy

import de.htwg.se.backgammon.validate.ValidateStrategy
import de.htwg.se.backgammon.model.Game
import de.htwg.se.backgammon.model.Player
import de.htwg.se.backgammon.exception.FieldDoesNotExistException
import de.htwg.se.backgammon.exception.AttackNotPossibleException
import de.htwg.se.backgammon.exception.EmptyFieldException

class ValidateBearInMoveStrategy(
    val game: Game,
    val player: Player,
    val to: Int
) extends ValidateStrategy {
  def from = if (player == Player.White) -1 else game.length

  override def validate() = {
    require(
      (to < game.length && to >= 0),
      FieldDoesNotExistException(from, (from.abs - to.abs).abs, to)
    )
    require(
      (player == game(to).occupier || game(to).occupier == Player.None || game(
        to
      ).number <= 1),
      AttackNotPossibleException(from, to, game(to).number)
    )
  }
}

class DefaultValidateMoveStrategy(
    val game: Game,
    val from: Int,
    val to: Int
) extends ValidateStrategy {

  override def validate() = {
    require(
      (to < game.length && to >= 0),
      FieldDoesNotExistException(from, (from.abs - to.abs).abs, to)
    )

    require(game(from).isOccupied(), EmptyFieldException(from))

    require(
      (game(from) hasSameOccupierAs game(to)) || game(to).number <= 1,
      AttackNotPossibleException(from, to, game(to).number)
    )
  }
}
