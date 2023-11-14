package de.htwg.se.backgammon.view

import scala.io.StdIn.readLine
import de.htwg.se.backgammon.model.MoveException
import de.htwg.se.backgammon.model.Move
import de.htwg.se.backgammon.util.Event
import de.htwg.se.backgammon.util.Observer
import de.htwg.se.backgammon.controller.Controller
import scala.util.Try
import de.htwg.se.backgammon.util.PrettyPrint.MarkDifferencesBetweenGames
import de.htwg.se.backgammon.util.PrettyPrint.PrintDiceResults
import de.htwg.se.backgammon.util.PrettyPrint.PrintBold
import de.htwg.se.backgammon.util.PrettyPrint.printNew

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
        println(s"Not possible! ${ex.getOrElse(MoveException()).getMessage()}")
      case Event.PlayerChanged =>
        println(s"${s"${controller.currentPlayer}".bold} it's your move!")
      case Event.DiceRolled =>
        println(
          s"You rolled the dice twice: ${controller.dice.toPrettyString}"
        )

  def inputLoop(): Unit =
    analyseInput(readLine) match
      case None             => println("Invalid input! Use: <from> <to>")
      case Some(move: Move) => controller.doAndPublish(controller.put, move)

    if continue then inputLoop()

  def analyseInput(input: String): Option[Move] =
    input match
      case "q" => controller.quit; None
      case _ => {
        Try({
          val positions = input.split(" ")
          val from = Integer.parseInt(positions(0))
          val to = Integer.parseInt(positions(1))
          Some(Move(from, to))
        }).getOrElse(None)
      }
