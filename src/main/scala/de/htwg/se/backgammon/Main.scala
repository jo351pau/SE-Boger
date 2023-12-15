package de.htwg.se.backgammon

import de.htwg.se.backgammon.controller.base.Controller
import de.htwg.se.backgammon.view.TUI
import de.htwg.se.backgammon.model.base.Model
import de.htwg.se.backgammon.model.base.Game
import de.htwg.se.backgammon.view.GUI
import scalafx.application.JFXApp3
import scala.concurrent.Future
import scala.concurrent.Await

private val NUMBER_OF_FIELDS = 24
private val NUMBER_OF_FIGURES = 15

object Main {
  val controller = new Controller(new Model(new Game(NUMBER_OF_FIELDS, NUMBER_OF_FIGURES)))
  val tui = new TUI(controller)
  val gui = new GUI(controller)

  @main def run(): Unit = {
    implicit val context = scala.concurrent.ExecutionContext.global
    val f = Future {
      gui.main(Array[String]())
    }
    tui.run
    Await.ready(f, scala.concurrent.duration.Duration.Inf)
  }

}
