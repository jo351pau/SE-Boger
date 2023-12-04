package de.htwg.se.backgammon.view.component

import scalafx.scene.layout.Pane
import de.htwg.se.backgammon.model.Field
import de.htwg.se.backgammon.model.Player
import scalafx.scene.shape.Ellipse
import scalafx.scene.effect.DropShadow
import scalafx.scene.paint.Color
import scalafx.scene.shape.Circle
import javafx.scene.input.MouseEvent
import de.htwg.se.backgammon.view.CustomColor
import scalafx.Includes.jfxMouseEvent2sfx
import scalafx.scene.Group
import scala.collection.mutable.ListBuffer

val CHECKER_RADIUS = 17

class Checkers extends Group {
  private var elements: ListBuffer[Checker] = ListBuffer()
  def add(checker: Checker) = {
    elements += checker
    children.add(checker)
  }
  def clear() = elements = ListBuffer(); children = Seq()
  def asList = elements.toList
}

class Checker(
    var player: Player,
    val xCoord: Double,
    val yCoord: Double,
    private var _activated: Boolean = false
) extends Circle {
  def activated = _activated
  def activated_=(state: Boolean) = {
    this._activated = state
  }

  var isDragging = false
  var offsetX = 0.0
  var offsetY = 0.0

  init
  def init = {
    centerX = xCoord
    centerY = yCoord
    radius = CHECKER_RADIUS
    fill = player match {
      case Player.White => Color.WHITESMOKE
      case _            => Color.rgb(188, 138, 95)
    }
    effect = new DropShadow(3.0, Color.BLACK)
  }
  def onHovering() = {
    if activated then this.setEffect(new DropShadow(5.0, Color.BLACK))
    else this.setEffect(new DropShadow(3.0, Color.BLACK))
  }

  def onExited() = {
    this.setEffect(new DropShadow(3.0, Color.BLACK))
  }

  def mouseEntered(e: MouseEvent) = {
    if isOn(e) then onHovering() else onExited()
  }

  def isOn(e: MouseEvent): Boolean = {
    val size = CHECKER_RADIUS * 2

    if (
      e.getX() >= (xCoord - CHECKER_RADIUS) &&
      e.getX() <= (xCoord + CHECKER_RADIUS) &&
      e.getY() >= (yCoord - CHECKER_RADIUS) &&
      e.getY() <= (yCoord + CHECKER_RADIUS)
    ) {
      offsetX = (e.sceneX - centerX.toDouble)
      offsetY = (e.sceneY - centerY.toDouble)
      print("Set offset")
      true
    } else
      false
  }

  def move(e: MouseEvent) = {
    centerX = e.sceneX - offsetX
    centerY = e.sceneY - offsetY
  }

  override def clone(): Checker =
    new Checker(player, xCoord, yCoord, _activated)
}
