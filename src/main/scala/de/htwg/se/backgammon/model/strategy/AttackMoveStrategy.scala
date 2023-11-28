package de.htwg.se.backgammon.model.strategy

import de.htwg.se.backgammon.model.Game
import de.htwg.se.backgammon.model.Player
import de.htwg.se.backgammon.model.Field

trait AttackMoveStrategy(game: Game, attacker: Player, to: Int)
    extends MoveCheckersStrategy {
  override def placeCheckers: Game = {
    set(to -> Field(attacker))
    bar(defender).++
  }

  def defender = attacker.other
}