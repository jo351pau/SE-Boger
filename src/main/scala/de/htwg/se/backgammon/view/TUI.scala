package de.htwg.se.backgammon.view

import scala.util.Try
import de.htwg.se.backgammon.controller.Controller
import de.htwg.se.backgammon.model.MoveException
import de.htwg.se.backgammon.model.Move
import de.htwg.se.backgammon.util.Event
import de.htwg.se.backgammon.util.Observer
import de.htwg.se.backgammon.util.PrettyPrint.MarkDifferencesBetweenGames
import de.htwg.se.backgammon.util.PrettyPrint.PrintDiceResults
import de.htwg.se.backgammon.util.PrettyPrint.PrintBold
import de.htwg.se.backgammon.util.PrettyPrint.printNew
import de.htwg.se.backgammon.util.PrettyPrint.printGameWithIndizies

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
      case None if controller.barIsNotEmpty =>
        printErr(s"Use: <steps from ${color.bold} bar>")
      case None =>
        printErr(s"Use: <field with ${color.bold} checkers>")
      case Some(move: Move) => controller.doAndPublish(controller.put, move)

    if continue then inputLoop()

  def analyseInput(input: String): Option[Move] =
    input match
      case "q" => controller.quit; None
      case string => {
        Try({
          val input = Integer.parseInt(string)
          Some(
            if (controller.barIsNotEmpty) then
              Move(player = controller.currentPlayer, steps = input)
            else Move(input, controller.die)
          )
        }).getOrElse(None)
      }

  def color = (controller.currentPlayer.toLowerCase)

  def printErr(error: String) =
    printGameWithIndizies(controller.game); println(error)

  def readLine: String =
    printInputFormat; scala.io.StdIn.readLine

  def printInputFormat = {
    if controller.barIsNotEmpty then
      print(s"Dice (${controller.dice.mkString("|")}): ")
    else print(s"${s"${controller.die}".bold} step/s: ")
  }