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

val WIDTH = 800
val HEIGHT = 600

val BORD_WIDTH = 700
val BORD_HEIGHT = 400
object GUI extends JFXApp3 with Observer {

  var board: Board = null
  var controller: Controller = null
  var selected: Checker = null
  var checker: Option[Checker] = None
  var isDragging = false

  override def update(e: Event, exception: Option[Throwable]): Unit = println(
    ""
  )

  override def start(): Unit = {
    val game = new Game(24, 15)
    val controller = new Controller(game)

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
        val gc = canvas.graphicsContext2D
        val pane = new Pane {
          board = new Board(
            Configuration.boardX,
            Configuration.boardY,
            Configuration.boardSize,
            4
          )
          board.setGame(new Game(24, 15), Configuration.pointHeight)
          children = Seq(board, canvas)
        }

        onMouseMoved = (event: MouseEvent) => {
          checker match {
            case Some(value) => {
              board.points.foreach(_.mouseEntered(event))
              value.move(event)
            }
            case _ =>
          }
          board.points.find(_.isOn(event)) match {
            case Some(point) =>
              point.getCheckers().foreach(_.mouseEntered(event))
            case _ =>
          }
        }

        onMouseClicked = (event: MouseEvent) => {
          checker match {
            case Some(_) => handleCheckerMoved(event, pane)
            case None =>
              board.getCheckers().find(c => c.isOn(event)) match {
                case Some(c: Checker) if c.activated => {
                  val c2 = c.clone()
                  checker = Some(c2)
                  selected = c
                  c.setVisible(false)
                  pane.children.add(c2)
                }
                case _ => {}
              }
          }
        }
        content = pane
      }
    }
    board.activateCheckers(controller.currentPlayer)
  }

  def handleCheckerMoved(event: MouseEvent, pane: Pane) = {
    val index = pane.children.indexOf(checker.get)
    if (index > 0) {
      pane.children.remove(index)
      checker = None
      selected.setVisible(true)

      val position = board.indexOfPointAt(event)
      board.find(_.isOn(event)) match {
        case Some(value) => value.field = (value.field + 1)
        case None        =>
      }
    }
  }

  private var offsetX: Double = 0
  private var offsetY: Double = 0

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
