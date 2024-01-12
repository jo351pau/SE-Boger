package de.htwg.se.backgammon

import de.htwg.se.backgammon.controller.base.Controller
import de.htwg.se.backgammon.view.TUI
import de.htwg.se.backgammon.model.base.Model
import de.htwg.se.backgammon.model.base.Game
import de.htwg.se.backgammon.view.GUI
import scalafx.application.JFXApp3
import scala.concurrent.Future
import scala.concurrent.Await
import de.htwg.se.backgammon.model.base.Dice
import de.htwg.se.backgammon.model.base.CustomSetup
import de.htwg.se.backgammon.model.base.DefaultSetup
import de.htwg.se.backgammon.model.storage.Parser
import de.htwg.se.backgammon.util.Observer
import de.htwg.se.backgammon.util.Event
import de.htwg.se.backgammon.model.storage.XmlStorage
import scala.util.Success
import scala.util.Failure
import de.htwg.se.backgammon.model.storage.JsonStorage
import de.htwg.se.backgammon.model.IGame
import de.htwg.se.backgammon.model.storage.Storage
import de.htwg.se.backgammon.model.storage.JsonStorage.{given}

private val NUMBER_OF_FIELDS = 24
private val NUMBER_OF_FIGURES = 15

object Main {
  val controller: Controller = configurate();
  val tui: TUI = new TUI(controller)
  val gui: GUI = new GUI(controller)

  @main def run(): Unit = {
    implicit val context = scala.concurrent.ExecutionContext.global
    val f = Future {
      gui.main(Array[String]())
    }
    tui.run
    Await.ready(f, scala.concurrent.duration.Duration.Inf)
  }

  def configurate()(using storage: Storage): Controller = {
    val model = new Model(
      storage.load[IGame]("game") match {
        case Success(game: Game) => game
        case _ => new Game(DefaultSetup(NUMBER_OF_FIELDS, NUMBER_OF_FIGURES))
      },
      Dice()
    )
    val controller = Controller(model)
    controller.add(new Observer {
      override def update(e: Event, exception: Option[Throwable]): Unit =
        if e == Event.Move then storage.save(controller.data, "game")
    }); controller
  }
}
