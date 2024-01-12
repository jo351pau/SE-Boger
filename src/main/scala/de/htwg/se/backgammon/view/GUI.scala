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
import de.htwg.se.backgammon.model.IGame
import scalafx.scene.layout.Pane
import scalafx.scene.input.MouseEvent
import scalafx.scene.shape.Circle
import de.htwg.se.backgammon.view.component.Configuration
import de.htwg.se.backgammon.view.component.Checker
import de.htwg.se.backgammon.view.component.util.DraggedChecker
import scalafx.application.HostServices
import de.htwg.se.backgammon.view.component.*
import de.htwg.se.backgammon.view.component.util.PlayerState
import scalafx.scene.shape.DrawMode
import de.htwg.se.backgammon.model.Player
import de.htwg.se.backgammon.model.IMove
import de.htwg.se.backgammon.controller.PutCommand
import de.htwg.se.backgammon.util.Command
import de.htwg.se.backgammon.exception.MoveException
import de.htwg.se.backgammon.model.base.DefinedMove
import scalafx.scene.PerspectiveCamera
import scala.util.Try
import scala.util.Random
import scalafx.application.Platform
import component.Dice
import de.htwg.se.backgammon.util.PrettyPrint.PrintBold
import de.htwg.se.backgammon.controller.IController
import de.htwg.se.backgammon.model.base.Game
import de.htwg.se.backgammon.model.base.Move
import de.htwg.se.backgammon.view.component.configuration.Default.{given}

class GUI(controller: IController) extends JFXApp3 with Observer {
  controller.add(this)

  var pane: Pane = null
  var draggedChecker: DraggedChecker = DraggedChecker.empty

  val board: Board = Board()
  val playerState: PlayerState = PlayerState()
  val dice: Dice = Dice()

  var bars: Bars =
    Bars.createDefault(0 - 25, 0)

  override def update(event: Event, exception: Option[Throwable]): Unit = {
    Platform.runLater(onEvent(event, exception))
  }

  def onEvent(event: Event, exception: Option[Throwable]) = event match {
    case Event.Move          => onMove(controller.game)
    case Event.PlayerChanged => onPlayerChanged(controller.currentPlayer)
    case Event.DiceRolled    => dice.roll(controller.dice)
    case Event.InvalidMove =>
      println(exception.getOrElse(MoveException()).getMessage())
    case _ =>
  }

  def onMove(game: IGame) = {
    board.setGame(game); bars.setGame(game)
  }

  def onPlayerChanged(current: Player) = {
    playerState.set(current)
    board.activateCheckers(current)
    stage.title = s"Backgammon - $current it's your turn!"
  }

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "Backgammon"
      scene = new Scene(
        given_FrameConfiguration.width,
        given_FrameConfiguration.height
      ) {
        pane = new Pane {
          board.setGame(controller.game)
          dice.create(2)

          children = Seq(
            board,
            playerState,
            bars,
            dice
          )
        }

        onMouseClicked = (event: MouseEvent) => {
          board.handleMouseEvent(event, onClicked = onBoardClicked)
          bars.handleMouseEvent(event, onClicked = onBoardClicked)
        }

        onMouseMoved = (event: MouseEvent) => {
          board.handleMouseEvent(event, doHovering = doHovering)
          bars.handleMouseEvent(event, doHovering = doHovering)

          if (draggedChecker.isDefined) then draggedChecker.move(event)
        }

        onShown = () => {
          dice.roll(controller.dice); onPlayerChanged(controller.currentPlayer)
        }

        content = pane
      }
    }
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
        Move.create(controller.game, controller.currentPlayer, from, to)
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
}
