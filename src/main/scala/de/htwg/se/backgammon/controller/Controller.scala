package de.htwg.se.backgammon.controller

import de.htwg.se.backgammon.util.Observable
import de.htwg.se.backgammon.util.Event
import de.htwg.se.backgammon.util.Manager
import de.htwg.se.backgammon.model.Model
import de.htwg.se.backgammon.model.Move
import de.htwg.se.backgammon.model.Player
import de.htwg.se.backgammon.model.Dice
import de.htwg.se.backgammon.exception.NotYourFieldException
import de.htwg.se.backgammon.exception.WrongDirectionException
import de.htwg.se.backgammon.exception.DieNotExistException
import de.htwg.se.backgammon.exception.FieldDoesNotExistException
import de.htwg.se.backgammon.model.MOVES_PER_ROUND
import de.htwg.se.backgammon.model.Game

import scala.util.Try
import scala.util.Failure
import scala.util.Success

import strategy.ValidateMoveStrategy

case class Controller(private val model: Model) extends Observable {
  def game = model.game
  def previousGame = model.previousGame
  def currentPlayer = model.player
  def dice = model.dice
  def die: Int = dice(
    if (model.movesThisRound.length <= model.dice.length - 1)
      model.movesThisRound.length
    else model.dice.length - 1
  )

  val manager = new Manager[Game]
  def doAndPublish(doThis: Move => Try[Game], move: Move): Unit = {
    if checkMove(move) then
      doThis(move).match {
        case Success(game: Game) => {
          this used move.steps
          this.game = game
          if (model.movesThisRound.length >= MOVES_PER_ROUND) {
            nextTurn()
            roll()
          }
        }
        case Failure(exception) =>
          notifyObservers(Event.InvalidMove, Some(exception))
      }
  }
  def put(move: Move): Try[Game] = manager.doStep(game, PutCommand(move))
  def quit: Unit = notifyObservers(Event.Quit)
  override def toString = game.toString

  private def game_=(game: Game) = {
    if (model.game != game) then
      model.game = game; notifyObservers(Event.Move)
  }

  private def nextTurn() = {
    model.next; notifyObservers(Event.PlayerChanged)
  }

  private def roll(): List[Int] = {
    model.dice = Dice.roll(MOVES_PER_ROUND)
    notifyObservers(Event.DiceRolled)
    if barIsNotEmpty then notifyObservers(Event.BarIsNotEmpty)
    model.dice
  }

  def barIsNotEmpty =
    if (currentPlayer == Player.White) game.barWhite > 0 else game.barBlack > 0

  private def hasToBearOff =
    game.numberOfPieces == game
      .homeBoards(currentPlayer)
      .filter(f => f.occupier == currentPlayer)
      .map(_.number)
      .sum

  private def used(dice: Int) = model.dice =
    model.dice.patch(model.dice.indexOf(dice), Nil, 1)

  private def checkMove(move: Move): Boolean =
    ValidateMoveStrategy(this, move).execute() match {
      case Failure(ex: Exception) =>
        notifyObservers(Event.InvalidMove, Some(ex)); false
      case _ => true
    }
}
