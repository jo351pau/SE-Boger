package de.htwg.se.backgammon.view

import de.htwg.se.backgammon.util.Observer
import de.htwg.se.backgammon.util.Event
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.canvas.Canvas
import scalafx.scene.paint.Color
import scalafx.application.JFXApp3
import scalafx.scene.shape.Rectangle
import scalafx.beans.property.StringProperty
import scalafx.scene.layout.HBox
import scalafx.geometry.Insets
import scalafx.scene.text.Text
import scalafx.scene.paint.LinearGradient
import scalafx.scene.paint.Stops
import scalafx.scene.paint.Color._
import scalafx.scene.effect.DropShadow

import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.Includes._
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, Label, ScrollPane}
import scalafx.scene.layout.{Priority, VBox}
import scalafx.scene.web.HTMLEditor
import scalafx.scene.canvas.GraphicsContext
import de.htwg.se.backgammon.view.component.Board
import de.htwg.se.backgammon.view.component.Size
import de.htwg.se.backgammon.model.DefaultSetup
import de.htwg.se.backgammon.model.Game
import scalafx.scene.layout.Pane
import scalafx.scene.input.MouseEvent
import scalafx.scene.shape.Circle
import de.htwg.se.backgammon.view.component.Configuration
import de.htwg.se.backgammon.controller.Controller
import de.htwg.se.backgammon.view.component.Checker
import de.htwg.se.backgammon.view.component.util.DraggedChecker
import scalafx.application.HostServices
import de.htwg.se.backgammon.view.component.*
import de.htwg.se.backgammon.view.component.util.PlayerState
import scalafx.scene.shape.DrawMode
import de.htwg.se.backgammon.model.Player
import de.htwg.se.backgammon.model.Move
import de.htwg.se.backgammon.controller.PutCommand
import de.htwg.se.backgammon.util.Command
import de.htwg.se.backgammon.exception.MoveException
import de.htwg.se.backgammon.model.DefinedMove
import scalafx.scene.PerspectiveCamera
import scala.util.Try
import scala.util.Random
object GUI extends JFXApp3 with Observer {

  var pane: Pane = null
  var board: Board = null
  var controller: Controller = null
  var draggedChecker: DraggedChecker = DraggedChecker.empty

  val playerState: PlayerState = PlayerState(
    Configuration.frameSize.width,
    Configuration.frameSize.height
  )

  var dice: (Dice, Dice) = null

  override def update(event: Event, exception: Option[Throwable]): Unit =
    event match {
      case Event.Move =>
        board.setGame(controller.game, Configuration.pointHeight)
        board.activateCheckers(controller.currentPlayer)
      case Event.PlayerChanged =>
        playerState.set(controller.currentPlayer)
        board.activateCheckers(controller.currentPlayer)
      case Event.DiceRolled => {
        dice._1.roll(controller.dice(0))
        dice._2.roll(controller.dice(1))
      }
      case Event.InvalidMove =>
        println(exception.getOrElse(MoveException()).getMessage())
      case _ =>
    }
  override def start(): Unit = {
    val game = new Game(24, 15)
    controller = new Controller(game)

    stage = new JFXApp3.PrimaryStage {
      title = "Backgammon"
      scene = new Scene(
        Configuration.frameSize.width,
        Configuration.frameSize.height
      ) {
        val canvas = new Canvas {
          width = Configuration.frameSize.width
          height = Configuration.frameSize.height
          mouseTransparent = true
        }
        pane = new Pane {
          board = new Board(
            Configuration.boardX,
            Configuration.boardY,
            Configuration.boardSize,
            4
          )
          board.setGame(new Game(24, 15), Configuration.pointHeight)
          dice = (createDice(), createDice())

          children = Seq(
            board,
            playerState,
            dice._1,
            dice._2
          )
        }

        onMouseClicked = (event: MouseEvent) => {
          board.handleMouseEvent(event, onClicked = onBoardClicked)
        }

        onMouseMoved = (event: MouseEvent) => {
          board.handleMouseEvent(event, doHovering = doHovering)
          if (draggedChecker.isDefined) then draggedChecker.move(event)
        }

        onShown = () => {
          controller.add(GUI)
          dice._1.roll(controller.dice(0))
          dice._2.roll(controller.dice(1))
        }

        content = pane
      }
    }
    board.activateCheckers(controller.currentPlayer)
  }

  def doHovering(element: GUIElement): Boolean = element match {
    case point: Point     => draggedChecker.isDefined
    case checker: Checker => draggedChecker.isEmpty
    case _                => true
  }

  def onBoardClicked(element: GUIElement): Unit = element match {
    case point: Point if draggedChecker.isDefined => {
      val from = board.indexOf(draggedChecker.point)
      val to = board.indexOf(point)

      controller.doAndPublish(
        controller.put,
        DefinedMove(controller.currentPlayer, from, to)
      )

      pane.children.remove(this.draggedChecker)
      draggedChecker.reset()
    }
    case checker: Checker
        if draggedChecker.isEmpty
          && checker.player == controller.currentPlayer => {
      draggedChecker = new DraggedChecker(checker)
      pane.children.add(draggedChecker)
    }
    case _ =>
  }

  def createDice(): Dice = {
    Dice(
      Random.between(1, 7),
      Configuration.boardX,
      Configuration.boardY,
      Configuration.boardSize
    )
  }

}
