package de.htwg.se.backgammon.controller

import de.htwg.se.backgammon.util.Observable
import de.htwg.se.backgammon.util.Event
import de.htwg.se.backgammon.util.Manager
import de.htwg.se.backgammon.model.Model
import de.htwg.se.backgammon.model.Move
import de.htwg.se.backgammon.model.Game
import de.htwg.se.backgammon.model.NotYourFieldException
import de.htwg.se.backgammon.model.WrongDirectionException
import scala.util.Try
import scala.util.Failure
import scala.util.Success

case class Controller(private val model: Model) extends Observable {
  def game = model.game
  def currentPlayer = model.player

  val manager = new Manager[Game]
  def doAndPublish(doThis: Move => Try[Game], move: Move): Unit = {
    if checkMove(move) then
      doThis(move).match {
        case Success(game: Game) => {
          this.game = game
          nextTurn()
        }
        case Failure(exception) =>
          notifyObservers(Event.InvalidMove, Some(exception))
      }
  }
  def put(move: Move): Try[Game] = manager.doStep(game, PutCommand(move))
  def quit: Unit = notifyObservers(Event.Quit)
  override def toString = game.toString

  private def game_=(game: Game) = {
    if model.game != game then
      model.game = game; notifyObservers(Event.Move)
  }

  private def nextTurn() = {
    model.next; notifyObservers(Event.PlayerChanged)
  }

  private def checkMove(move: Move): Boolean = {
    game.get(move.from) match {
      case field if (field.occupier != currentPlayer) =>
        NotYourFieldException(
          move.from,
          field.occupier,
          currentPlayer
        )
      case field if (move.isWrongDirection(currentPlayer)) =>
        WrongDirectionException(
          currentPlayer
        )
      case _ => None
    } match {
      case ex: Exception => notifyObservers(Event.InvalidMove, Some(ex)); false
      case _             => true
    }
  }

}
