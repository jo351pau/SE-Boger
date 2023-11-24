package de.htwg.se.backgammon.model.strategy

import de.htwg.se.backgammon.model.Game
import de.htwg.se.backgammon.model.Field
import de.htwg.se.backgammon.model.Player

abstract class AbstractMoveStrategy(game: Game, occupier: Player, to: Int)
    extends MoveCheckersStrategy(game) {
  override def placeCheckers: Game =
    set(
      to -> (if (game(to).isEmpty()) Field(occupier) else (game(to) + 1))
    )
}

class PeacefulMoveStrategy(game: Game, from: Int, to: Int)
    extends AbstractMoveStrategy(game, game(from).occupier, to) {
  override def pickUpCheckers: Game = set(from -> (game(from) - 1))
}

class BearOffMoveStrategy(game: Game, player: Player, to: Int)
    extends AbstractMoveStrategy(game, player, to) {
  override def pickUpCheckers: Game = bar(player).--
}