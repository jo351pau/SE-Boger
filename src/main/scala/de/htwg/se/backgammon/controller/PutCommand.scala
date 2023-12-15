package de.htwg.se.backgammon.controller

import de.htwg.se.backgammon.util.*
import scala.util.Try
import de.htwg.se.backgammon.model.IMove
import de.htwg.se.backgammon.model.IGame
import de.htwg.se.backgammon.model.GameState

case class PutCommand(move: IMove, private var _game: IGame = null)
    extends Command[IGame, GameState]:
  override def noStep(game: IGame): IGame = game
  override def doStep(game: IGame): Try[IGame] =
    _game = game; game.move(move)
  override def undoStep(): GameState = GameState(_game, move)

