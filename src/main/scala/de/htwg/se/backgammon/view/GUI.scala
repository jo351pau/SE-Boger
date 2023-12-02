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

val WIDTH = 800
val HEIGHT = 600
object GUI extends JFXApp3 with Observer {

  var board: Board = null

  override def update(e: Event, exception: Option[Throwable]): Unit = println(
    ""
  )

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "Backgammon"
      scene = new Scene(WIDTH, HEIGHT) {
        val canvas = new Canvas(WIDTH, HEIGHT)
        val gc = canvas.graphicsContext2D
        initBoard(gc).draw
        drawPlayer(gc)
        content = canvas
      }
    }
  }

  def initBoard(gc: GraphicsContext) = {
    val BORD_WIDTH = 700
    val BORD_HEIGHT = 400
    val x = (WIDTH - BORD_WIDTH) / 2
    val y = (HEIGHT - BORD_HEIGHT) / 2

    board = new Board(gc, x, y, Size(BORD_WIDTH, BORD_HEIGHT), 4)
    board = board.initPoints(24, (BORD_HEIGHT / 2) * 0.9)
    board = board.setPointsFromGame(new Game(DefaultSetup(24, 15)))
    board
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
