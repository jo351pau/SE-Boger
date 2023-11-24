package de.htwg.se.backgammon.model

import de.htwg.se.backgammon.validate.ValidateStrategy
import scala.util.Try
import de.htwg.se.backgammon.model.Game

object MoveStrategy {
  def apply(game: Game, move: Move, to: Int) = move match {
    case move: BearOffMove =>
      if (!(game(to) hasOccupier move.player))
        BearOffAttackStrategy(game, move.player, to)
      else BearOffMoveStrategy(game, move.player, to)
    case Move(from, steps) =>
      if (!(game(from) hasSameOccupier game(to)))
        DefaultAttackStrategy(game, from, to)
      else DefaultMoveStrategy(game, from, to)
  }
}

trait MoveStrategy(val game: Game) {
  def execute(): Game

  def copy(
      changes: Map[Int, Field],
      barWhite: Int = game.barWhite,
      barBlack: Int = game.barBlack
  ): Game =
    Game(
      List.tabulate(game.length)(i => changes.getOrElse(i, game(i))),
      barWhite = barWhite,
      barBlack = barBlack
    )
}

class AttackStrategy{
}

class DefaultMoveStrategy(
    game: Game,
    val from: Int,
    val to: Int
) extends MoveStrategy(game) {
  override def execute(): Game =
    copy(
      Map(
        from -> (game(from) - 1),
        to -> (if (game(to).isEmpty() ) 
                 Field(game(from).occupier)
               else (game(to) + 1))
      )
    )
}

class BearOffMoveStrategy(
    game: Game,
    val player: Player,
    val to: Int
) extends MoveStrategy(game) {
  override def execute(): Game = {
    val changes = Map(
      to -> (if (game(to).isEmpty())
               Field(player)
             else (game(to) + 1))
    )
    player match {
      case Player.White => copy(changes, game.barWhite - 1, game.barBlack)
      case Player.Black => copy(changes, game.barWhite, game.barBlack - 1)
      case Player.None  => game
    }
  }
}

class DefaultAttackStrategy(
    game: Game,
    val from: Int,
    val to: Int
) extends MoveStrategy(game) {
  override def execute(): Game =
    val changes = Map(
      (from -> (game(from) - 1)),
      (to -> Field(game(from).occupier))
    )
    game(to).occupier.match {
      case Player.White => copy(changes, game.barWhite + 1, game.barBlack)
      case Player.Black => copy(changes, game.barWhite, game.barBlack + 1)
      case Player.None  => game
    }
}

class BearOffAttackStrategy(
    game: Game,
    val player: Player,
    val to: Int
) extends MoveStrategy(game) {
  override def execute(): Game = {
    val changes = Map(to -> Field(player))
    game(to).occupier.match {
      case Player.White => copy(changes, game.barWhite + 1, game.barBlack - 1)
      case Player.Black => copy(changes, game.barWhite - 1, game.barBlack + 1)
      case Player.None  => game
    }
  }
}
