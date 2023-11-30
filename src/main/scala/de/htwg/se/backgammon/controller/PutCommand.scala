package de.htwg.se.backgammon.controller

import de.htwg.se.backgammon.util.*
import scala.util.Try
import de.htwg.se.backgammon.model.Move
import de.htwg.se.backgammon.model.Game
import de.htwg.se.backgammon.model.GameState

case class PutCommand(move: Move, private var _game: Game = null)
    extends Command[Game, GameState]:
  override def noStep(game: Game): Game = game
  override def doStep(game: Game): Try[Game] =
    _game = game; game.move(move)
  override def undoStep(): GameState = GameState(_game, move)

