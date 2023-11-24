package de.htwg.se.backgammon

import de.htwg.se.backgammon.controller.Controller
import de.htwg.se.backgammon.view.TUI
import de.htwg.se.backgammon.model.Model
import de.htwg.se.backgammon.model.Game
import model.strategy.game.Game

private val NUMBER_OF_FIELDS = 24
private val NUMBER_OF_FIGURES = 15

@main def run(): Unit = {
  val game = new Game(NUMBER_OF_FIELDS, NUMBER_OF_FIGURES)
  val controller = Controller(Model(game))
  val tui = TUI(controller)
  tui.run
}
