package de.htwg.se.backgammon.model.strategy

import de.htwg.se.backgammon.model.Game
import de.htwg.se.backgammon.model.Field
import de.htwg.se.backgammon.model.Player

trait PeacefulMoveStrategy(game: Game, occupier: Player, to: Int)
    extends MoveCheckersStrategy {
  override def placeCheckers: Game =
    set(
      to -> (if (game(to).isEmpty()) Field(occupier) else (game(to) + 1))
    )
}
