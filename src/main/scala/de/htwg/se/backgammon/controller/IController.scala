package de.htwg.se.backgammon.controller

import de.htwg.se.backgammon.util.Observable
import de.htwg.se.backgammon.model.Game
import de.htwg.se.backgammon.model.Move
import de.htwg.se.backgammon.model.GameState
import de.htwg.se.backgammon.model.Player
import scala.util.Try

trait IController extends Observable {
  def game: Game
  def previousGame: Game
  def currentPlayer: Player
  def dice: List[Int]
  def die: Int
  def checkersInBar: Boolean
  def hasToBearOff: Boolean

  def doAndPublish(doThis: Move => Try[Game], move: Move): Unit
  def doAndPublish(doThis: Move => Try[Game]): Unit
  def undoAndPublish(doThis: => Option[GameState]): Unit
  def skip(steps: Int): Game
  def put(move: Move): Try[Game]
  def redo(move: Move): Try[Game]
  def undo: Option[GameState]
  def quit: Unit

}
