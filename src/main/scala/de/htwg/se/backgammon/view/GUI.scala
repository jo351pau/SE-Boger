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
import scalafx.scene.shape.DrawMode
import de.htwg.se.backgammon.model.Player
import de.htwg.se.backgammon.model.Move
import de.htwg.se.backgammon.controller.PutCommand
import de.htwg.se.backgammon.util.Command
import de.htwg.se.backgammon.exception.MoveException

val WIDTH = 800
val HEIGHT = 600

val BORD_WIDTH = 700
val BORD_HEIGHT = 400
object GUI extends JFXApp3 with Observer {

  var pane: Pane = null
  var board: Board = null
  var controller: Controller = null
  var checker: DraggedChecker = DraggedChecker.empty
  var isDragging = false

  override def update(event: Event, exception: Option[Throwable]): Unit =
    event match {
      case Event.Move =>
        board.setGame(controller.game, Configuration.pointHeight)
    case Event.PlayerChanged => 
        board.activateCheckers(controller.currentPlayer)
      case Event.InvalidMove =>
        println(exception.getOrElse(MoveException()).getMessage())
      case _ =>
    }
  override def start(): Unit = {
    val game = new Game(24, 15)
    controller = new Controller(game)
    controller.add(this)

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
          children = Seq(board, canvas)
        }

        onMouseClicked = (event: MouseEvent) => {
          board.handleMouseEvent(event, onClicked = onBoardClicked)
        }

        onMouseMoved = (event: MouseEvent) => {
          board.handleMouseEvent(event, doHovering = doHovering)
          if (checker.isDefined) then checker.move(event)
        }

        content = pane
      }
    }
    board.activateCheckers(controller.currentPlayer)
  }

  def doHovering(element: GUIElement): Boolean = element match {
    case point: Point     => this.checker.isDefined
    case checker: Checker => this.checker.isEmpty
    case _                => true
  }

  def onBoardClicked(element: GUIElement): Unit = element match {
    case point: Point if this.checker.isDefined => {
      val from = board.indexOf(this.checker.point)
      val to = board.indexOf(point)

      val steps = controller.currentPlayer match {
        case Player.White => to - from
        case _            => from - to
      }
      controller.doAndPublish(controller.put, Move(from, steps))

      pane.children.remove(this.checker)
      this.checker.reset()
    }
    case checker: Checker
        if this.checker.isEmpty
          && checker.player == controller.currentPlayer => {
      this.checker = new DraggedChecker(checker)
      pane.children.add(this.checker)
    }
    case _ =>
  }

  def drawPlayer(gc: GraphicsContext) = {
    val MARGIN_TOP = 25
    val MARGIN_LEFT = 25
    val PADDING = 40
    val BOX_WIDTH = 50
    val CORNER_RADIUS = 5
    val mid = WIDTH / 2 - BOX_WIDTH / 2
    gc.setFill(Color.WHITESMOKE)
    gc.fillRoundRect(
      MARGIN_LEFT,
      MARGIN_TOP,
      BOX_WIDTH,
      BOX_WIDTH,
      CORNER_RADIUS,
      CORNER_RADIUS
    )
    gc.setStroke(CustomColor.c1)
    gc.strokeRoundRect(
      MARGIN_LEFT,
      MARGIN_TOP,
      BOX_WIDTH,
      BOX_WIDTH,
      CORNER_RADIUS,
      CORNER_RADIUS
    )
    gc.setFill(Color.rgb(188, 138, 95))
    gc.fillRoundRect(
      MARGIN_LEFT,
      HEIGHT - MARGIN_TOP - BOX_WIDTH,
      BOX_WIDTH,
      BOX_WIDTH,
      CORNER_RADIUS,
      CORNER_RADIUS
    )
    gc.strokeRoundRect(
      MARGIN_LEFT,
      HEIGHT - MARGIN_TOP - BOX_WIDTH,
      BOX_WIDTH,
      BOX_WIDTH,
      CORNER_RADIUS,
      CORNER_RADIUS
    )
  }

}
