package de.htwg.se.backgammon.controller

import de.htwg.se.backgammon.util.*
import de.htwg.se.backgammon.model.*
import scala.util.Try
import scala.util.Failure
import scala.util.Success

case class Controller(var game: Game) extends Observable {
  val manager = new Manager[Game]
  def doAndPublish(doThis: Move => Try[Game], move: Move): Unit = {
    if checkMove(move) then
      doThis(move).match {
        case Success(value: Game) => {
          game = value
          PlayerState.next
          notifyObservers(Event.PlayerChanged)
          notifyObservers(Event.Move)
        }
        case Failure(exception) =>
          notifyObservers(Event.InvalidMove, Some(exception))
      }
  }
  def put(move: Move): Try[Game] = manager.doStep(game, PutCommand(move))
  def quit: Unit = notifyObservers(Event.Quit)
  override def toString = game.toString

  def checkMove(move: Move): Boolean = {
    game.get(move.from) match {
      case field if (field.occupier != PlayerState.player) =>
        NotYourFieldException(
          move.from,
          field.occupier,
          PlayerState.player
        )
      case field if (move.isWrongDirection(PlayerState.player)) =>
        WrongDirectionException(
          PlayerState.player
        )
      case _ => None
    } match {
      case ex: Exception => notifyObservers(Event.InvalidMove, Some(ex)); false
      case _             => true
    }
  }

}
object PlayerState:
  var player = Player.White
  def next = if player == Player.White then player = Player.Black
  else player = Player.White
