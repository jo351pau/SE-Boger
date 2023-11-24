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
    extends ValidateMoveStrategy(move, dice) {
  val field = Try(game.get(move.from)).getOrElse(Field(Player.None))
  val occupier = field.occupier
  val from = move.from
  val steps = move.steps

  require(
    (from <= game.fields.length || from > 0),
    FieldDoesNotExistException(from, steps, from)
  )

  require((occupier == player), NotYourFieldException(from, occupier, player))
}

class ValidateBearOffMove(move: Move, dice: List[Int])
    extends ValidateMoveStrategy(move, dice)

trait ValidateMoveStrategy(val move: Move, val dice: List[Int])
    extends ValidateStrategy {
  require(dice.contains(move.steps), DieNotExistException(move.steps, dice))
}

object ValidateMoveStrategy {
  def apply(
      game: Game,
      move: Move,
      player: Player,
      dice: List[Int]
  ): ValidateMoveStrategy = move match {
    case _: BearOffMove =>
      ValidateBearOffMove(move, dice)
    case _ => ValidateMove(game, move, player, dice)
  }

  def apply(c: Controller, move: Move): ValidateMoveStrategy =
    apply(c.game, move, c.currentPlayer, c.dice)
}
