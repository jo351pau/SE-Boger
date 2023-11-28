package de.htwg.se.backgammon.model.strategy

import de.htwg.se.backgammon.model.Game
import de.htwg.se.backgammon.model.Move
import de.htwg.se.backgammon.model.BearOffMove
import de.htwg.se.backgammon.model.Field
import de.htwg.se.backgammon.model.Player

object MoveStrategy {
  def apply(game: Game, move: Move, to: Int) = move match {
    case move: BearOffMove =>
      if game(to) isNotOccupiedBy move.player then
        new BearOffMoveStrategy(game, move.player, to)
          with AttackMoveStrategy(game, move.player, to)
      else
        new BearOffMoveStrategy(game, move.player, to)
          with PeacefulMoveStrategy(game, move.player, to)
    case Move(from, steps) =>
      if game(from) hasDifferentOccupierThen game(to) then
        new DefaultMoveStrategy(game, from, to)
          with AttackMoveStrategy(game, game(from).occupier, to)
      else
        new DefaultMoveStrategy(game, from, to)
          with PeacefulMoveStrategy(game, game(from).occupier, to)
  }
}

trait MoveStrategy(var game: Game) {
  def execute(): Game

  def set(change: (Int, Field)): Game = set(Map(change))

  def set(changes: Map[Int, Field]): Game = {
    game = Game(
      List.tabulate(game.length)(i => changes.getOrElse(i, game(i)))
    ); game
  }

  def set(
      barWhite: Int = game.barWhite,
      barBlack: Int = game.barBlack
  ): Game = {
    game = Game(game.fields, barWhite, barBlack); game
  }
}

abstract class MoveCheckersStrategy(game: Game) extends MoveStrategy(game) {

  def execute(): Game = placeCheckers; pickUpCheckers

  def placeCheckers: Game

  def pickUpCheckers: Game

  trait Bar { def ++ : Game; def -- : Game }
  def bar(player: Player): Bar = new Bar {
    private def add(num: Int) = player match {
      case Player.White => set(barWhite = game.barWhite + num)
      case _            => set(barBlack = game.barBlack + num)
    }
    def ++ : Game = add(1); def -- : Game = add(-1)
  }
}
