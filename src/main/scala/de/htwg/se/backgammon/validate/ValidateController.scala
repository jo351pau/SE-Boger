package de.htwg.se.backgammon.validate

import de.htwg.se.backgammon.model.Move
import de.htwg.se.backgammon.model.BearOffMove
import de.htwg.se.backgammon.model.Player
import de.htwg.se.backgammon.model.Game
import de.htwg.se.backgammon.model.FieldDoesNotExistException
import de.htwg.se.backgammon.model.MoveException
import de.htwg.se.backgammon.model.NotYourFieldException
import de.htwg.se.backgammon.model.DieNotExistException
import de.htwg.se.backgammon.model.Field
import de.htwg.se.backgammon.controller.Controller
import scala.util.Try

class ValidateMove(game: Game, move: Move, player: Player, dice: List[Int])
    extends ValidateMoveStrategy(game, move, dice) {
  val field = Try(game.get(move.from)).getOrElse(Field(Player.None))

  override def validate() = {
    super.validate()
    require(
      (field.occupier == player),
      NotYourFieldException(move.from, field.occupier, player)
    )
  }
}

class ValidateBearOffMove(game: Game, move: Move, dice: List[Int])
    extends ValidateMoveStrategy(game, move, dice)

trait ValidateMoveStrategy(val game: Game, val move: Move, val dice: List[Int])
    extends ValidateStrategy {
  override def validate() = {
    require(
      (move.from < game.fields.length && move.from >= 0),
      FieldDoesNotExistException(move.from, move.steps, move.from)
    )
    require(dice.contains(move.steps), DieNotExistException(move.steps, dice))
  }
}

object ValidateMoveStrategy {
  def apply(
      game: Game,
      move: Move,
      player: Player,
      dice: List[Int]
  ): ValidateMoveStrategy = move match {
    case _: BearOffMove =>
      ValidateBearOffMove(game, move, dice)
    case _ => ValidateMove(game, move, player, dice)
  }

  def apply(c: Controller, move: Move): ValidateMoveStrategy =
    apply(c.game, move, c.currentPlayer, c.dice)
}
