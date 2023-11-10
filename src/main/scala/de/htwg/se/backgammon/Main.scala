package de.htwg.se.backgammon

import scala.io.StdIn.readLine
import scala.util.Success
import scala.util.Failure

import de.htwg.se.backgammon.model.Game
import de.htwg.se.backgammon.controller.Controller
import de.htwg.se.backgammon.view.TUI


val NUMBER_OF_FIELDS = 24
val NUMBER_OF_FIGURES = 15

@main def run(): Unit = {
  val game = new Game(NUMBER_OF_FIELDS, NUMBER_OF_FIGURES)
  val controller = Controller(game)
  val tui = TUI(controller)
  tui.run
}