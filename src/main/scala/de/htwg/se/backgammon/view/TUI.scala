package de.htwg.se.backgammon.view

import scala.util.Try
import de.htwg.se.backgammon.controller.Controller
import de.htwg.se.backgammon.exception.MoveException
import de.htwg.se.backgammon.model.Move
import de.htwg.se.backgammon.model.BearInMove
import de.htwg.se.backgammon.util.Event
import de.htwg.se.backgammon.util.Observer
import de.htwg.se.backgammon.util.PrettyPrint.MarkDifferencesBetweenGames
import de.htwg.se.backgammon.util.PrettyPrint.PrintDiceResults
import de.htwg.se.backgammon.util.PrettyPrint.PrintBold
import de.htwg.se.backgammon.util.PrettyPrint.printNew
import de.htwg.se.backgammon.util.PrettyPrint.printGameWithIndizies
import de.htwg.se.backgammon.model.Input
import de.htwg.se.backgammon.model.Quit
import de.htwg.se.backgammon.model.Undo
import de.htwg.se.backgammon.model.Redo

class TUI(controller: Controller) extends Observer:
  controller.add(this)
  var continue = true
  def run =
    printNew(controller.game); update(Event.DiceRolled)
    inputLoop()

  override def update(e: Event, ex: Option[Throwable] = None) =
    e match
      case Event.Quit => continue = false
      case Event.Move =>
        printNew(controller.game markDifferencesTo controller.previousGame)
      case Event.InvalidMove =>
        printErr(s"Not possible! ${ex.getOrElse(MoveException()).getMessage()}")
      case Event.PlayerChanged =>
        println(s"${s"${controller.currentPlayer}".bold} it's your move!")
      case Event.DiceRolled =>
        println(
          s"You rolled the dice twice: ${controller.dice.toPrettyString}"
        )
      case Event.BarIsNotEmpty =>
        printErr(
          s"You have at least one checker in your bar, which die do you wanna use?"
        )

  def inputLoop(): Unit =
    analyseInput(readLine) match
      case None if controller.checkersInBar =>
        printErr(s"Use: <steps from ${playerColor.bold} bar>")
      case None =>
        printErr(s"Use: <field with ${playerColor.bold} checkers>")
      case Some(move: Move) => controller.doAndPublish(controller.put, move)
      case Some(_: Redo)    => controller.doAndPublish(controller.redo)
      case Some(_: Undo)    => controller.undoAndPublish(controller.undo)
      case Some(_)          => controller.quit

    if continue then inputLoop()

  def analyseInput(input: String): Option[Input] =
    input match
      case "q"    => Input.Quit
      case "undo" => Input.Undo
      case "redo" => Input.Redo
      case string => {
        Try({
          val input = Integer.parseInt(string)
          Some(
            if (controller.checkersInBar) then
              BearInMove(controller.currentPlayer, input)
            else Move(input, controller.die)
          )
        }).getOrElse(None)
      }

  def playerColor = (controller.currentPlayer.toLowerCase)

  def printErr(error: String) =
    printGameWithIndizies(controller.game); println(error)

  def readLine: String =
    printInputFormat; scala.io.StdIn.readLine

  def printInputFormat = {
    if controller.checkersInBar then
      print(s"Dice (${controller.dice.mkString("|")}): ")
    else print(s"${s"${controller.die}".bold} step/s: ")
  }
