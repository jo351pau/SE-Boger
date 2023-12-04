package de.htwg.se.backgammon.view.component

import scalafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import de.htwg.se.backgammon.model.Field
import de.htwg.se.backgammon.model.Player
import scalafx.scene.effect.DropShadow
import scalafx.Includes.jfxColor2sfx
import de.htwg.se.backgammon.view.CustomColor
import scalafx.scene.shape.Rectangle
import scalafx.scene.shape.Polygon
import scalafx.scene.layout.Pane
import scalafx.Includes.eventClosureWrapperWithParam
import javafx.scene.input.MouseEvent
import scalafx.scene.Group

val MARGIN_BOTTOM = 5
val PADDING = 2
val CHECKER_SIZE = 34

class Point(
    x: Array[Double],
    y: Array[Double],
    size: Size,
    color: Color
) extends Pane {
  private var _field: Field = Field()
  private var checkers: Checkers = Checkers()
  def getCheckers() = checkers.asList

  private def xCoord = x(0)
  private def yCoord = y(0) - (if isUpsideDown then 0 else size.height)
  private var polygon: Polygon = null

  init
  def init = {
    val rectangle = new Rectangle {
      width = size.width
      height = size.height
      fill = Color.TRANSPARENT
      this.setX(xCoord)
      this.setY(yCoord)
    }

    polygon = new Polygon {
      points.addAll(x(0), y(0), x(1), y(1), x(2), y(2))
      fill = color
    }

    children = Seq(polygon, rectangle, checkers)
  }

  def onHovering() = {
    polygon.setEffect(new DropShadow(5.0, Color.BLACK))
  }

  def onExited() = {
    polygon.setEffect(null)
  }

  def isOn(e: MouseEvent) = {
    e.getX() >= xCoord && e.getX() <= (xCoord + size.width) &&
    e.getY() >= yCoord && e.getY() <= (yCoord + size.height)
  }

  def mouseEntered(e: MouseEvent) = {
    if (isOn(e)) then onHovering() else onExited()
  }

  def field: Field = _field
  def field_=(field: Field) = {
    draw(_field, field)
    _field = field
  }

  def activate(player: Player) = {
    checkers.asList.foreach(c => c.activated = (player == field.occupier))
  }

  def draw(previous: Field, field: Field): Unit = {
    if (
      (previous.number == field.number &&
      previous.occupier == field.occupier)
    ) then return

    checkers.clear()

    if (field.number == 0) then return

    for (i <- (field.number - 1) to 0 by -1) {
      createChecker(field.occupier, i)
    }
  }

  def createChecker(player: Player, postion: Int) = {
    checkers.add(Checker(player, x(2), getCheckerY(postion)))
  }

  def getCheckerY(index: Int) = {
    getCheckerBaseY + index * (if isUpsideDown then CHECKER_SIZE + PADDING
                               else -(CHECKER_SIZE + PADDING))
  }

  def getCheckerBaseY = {
    CHECKER_RADIUS + (if isUpsideDown then y(0) + MARGIN_BOTTOM
                      else y(0) - CHECKER_SIZE - MARGIN_BOTTOM)
  }

  def isUpsideDown = y(0) < y(2)

  override def toString(): String =
    s"Point (${x.mkString(",")}/${y.mkString(",")})"

}
