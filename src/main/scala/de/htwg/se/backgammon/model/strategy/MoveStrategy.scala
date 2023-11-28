package de.htwg.se.backgammon.model.strategy

import de.htwg.se.backgammon.model.Game
import de.htwg.se.backgammon.model.Player

abstract class BearOffMoveStrategy(game: Game, player: Player, to: Int)
    extends MoveCheckersStrategy(game) {
  override def pickUpCheckers: Game = bar(player).--
}

abstract class DefaultMoveStrategy(game: Game, from: Int, to: Int)
    extends MoveCheckersStrategy(game) {
  override def pickUpCheckers: Game = set(from -> (game(from) - 1))
}
