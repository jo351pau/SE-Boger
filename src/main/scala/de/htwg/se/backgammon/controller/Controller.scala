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
import de.htwg.se.backgammon.model.GameState
import de.htwg.se.backgammon.model.NoMove
import de.htwg.se.backgammon.exception.NoMoveException

case class Controller(private val model: Model) extends Observable {
  def this(game: Game) = this(Model(game))

  def game = model.game
  def previousGame = model.previousGame
  def currentPlayer = model.player
  def dice = model.dice
  def die: Int = dice(0)

  val manager = new Manager[Game, GameState]
  def doAndPublish(doThis: Move => Try[Game], move: Move): Unit = {
    if checkMove(move) then
      doThis(move).match {
        case Success(game: Game) => handle(game, move.steps)
        case Failure(exception) =>
          notifyObservers(Event.InvalidMove, Some(exception))
      }
  }

  def doAndPublish(doThis: Move => Try[Game]): Unit =
    manager.stackCommand match {
      case Some(command: PutCommand) => doAndPublish(doThis, command.move)
      case _ => notifyObservers(Event.InvalidMove, Some(NoMoveException()))
    }

  def undoAndPublish(doThis: => Option[GameState]): Unit = {
    val (game, move) = doThis match {
      case None                        => return
      case Some(GameState(game, move)) => (game, move)
    }
    model.dice =
      if (model.dice.length == MOVES_PER_ROUND)
      then List(move.steps)
      else model.dice.::(move.steps)

    this.game = game

    if (model.dice.length == 1) nextTurn()
  }

  def handle(game: Game, steps: Int) = {
    this used steps
    this.game = game
    if (model.dice.isEmpty) {
      nextTurn()
      roll()
    }
    if (game.winner.isDefined) then notifyObservers(Event.GameOver)
  }

  def skip(steps: Int): Game = {
    handle(game, steps); game
  }
  def put(move: Move): Try[Game] = manager.doStep(game, PutCommand(move))
  def redo(move: Move): Try[Game] = manager.redoStep(game)
  def undo: Option[GameState] = manager.undoStep()
  def quit: Unit = notifyObservers(Event.Quit)

  override def toString = game.toString

  private def game_=(game: Game) = {
    model.game = game; notifyObservers(Event.Move)
  }

  private def nextTurn() = {
    model.next; notifyObservers(Event.PlayerChanged)
  }

  private def roll(): List[Int] = {
    model.dice = Dice.roll(MOVES_PER_ROUND)
    notifyObservers(Event.DiceRolled)
    if checkersInBar then notifyObservers(Event.BarIsNotEmpty)
    else if hasToBearOff then notifyObservers(Event.AllCheckersInTheHomeBoard)
    model.dice
  }

  def checkersInBar =
    if (currentPlayer == Player.White) game.barWhite > 0 else game.barBlack > 0

  def hasToBearOff =
    game.numberOfPieces(currentPlayer) == game
      .homeBoard(currentPlayer)
      .filter(_.occupier == currentPlayer)
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
