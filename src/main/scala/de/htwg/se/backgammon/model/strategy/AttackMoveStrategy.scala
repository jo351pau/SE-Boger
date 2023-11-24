package de.htwg.se.backgammon.model.strategy

import de.htwg.se.backgammon.model.Game
import de.htwg.se.backgammon.model.Player
import de.htwg.se.backgammon.model.Field

abstract class AbstractAttackStrategy(game: Game, attacker: Player, to: Int)
    extends MoveCheckersStrategy(game) {
  override def placeCheckers: Game = {
    set(to -> Field(attacker))
    bar(defender).++
  }

  def defender = attacker.other
}

class DefaultAttackStrategy(game: Game, from: Int, to: Int)
    extends AbstractAttackStrategy(game, game(from).occupier, to) {
  override def pickUpCheckers: Game = set(from -> (game(from) - 1))
}

class BearOffAttackStrategy(game: Game, attacker: Player, to: Int)
    extends AbstractAttackStrategy(game, attacker, to) {
  override def pickUpCheckers: Game = bar(attacker).--
}
