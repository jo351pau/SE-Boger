package de.htwg.se.backgammon.model

import de.htwg.se.backgammon.validate.ValidateStrategy

class ValidateBearOffMoveStrategy(
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
      (game(from) hasSameOccupier game(to)) || game(to).number <= 1,
      AttackNotPossibleException(from, to, game(to).number)
    )
  }
}
