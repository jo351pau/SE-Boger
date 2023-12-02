package de.htwg.se.backgammon.view.component

import scalafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import de.htwg.se.backgammon.model.Field
import de.htwg.se.backgammon.model.Player
import scalafx.scene.effect.DropShadow
import scalafx.Includes.jfxColor2sfx
import de.htwg.se.backgammon.view.CustomColor

val CHECKER_SIZE = 35
val MARGIN_BOTTOM = 5
val PADDING = 2

class Point(
    gc: GraphicsContext,
    x: Array[Double],
    y: Array[Double],
    color: Color
) extends Drawable(gc) {
  var field: Field = Field()
  override def draw: Unit = {
    gc.setFill(color)
    gc.fillPolygon(x, y, 3)
    if (field.number > 0) {
      for (i <- (field.number - 1) to 0 by -1) (drawChecker(i))
    }
  }

  def drawChecker(index: Int) = {
    field.occupier match {
      case Player.White => gc.setFill(Color.WHITESMOKE)
      case _            => gc.setFill(Color.rgb(188, 138, 95))
    }
    val width = x(1) - x(0)
    gc.setEffect(new DropShadow(5.0, Color.BLACK))
    gc.fillOval(
      x(0) + (width) - (width / 2) - CHECKER_SIZE / 2,
      getCheckerY(index),
      CHECKER_SIZE,
      CHECKER_SIZE
    )
    gc.setEffect(null)
  }

  def getCheckerY(index: Int) = {
    getCheckerBaseY + index * (if isUpsideDown then -(CHECKER_SIZE + PADDING)
                               else CHECKER_SIZE + PADDING)
  }

  def getCheckerBaseY = {
    if isUpsideDown then y(0) - CHECKER_SIZE - MARGIN_BOTTOM
    else y(0) + MARGIN_BOTTOM
  }

  def isUpsideDown = y(0) > y(2)

  override def toString(): String =
    s"Point (${x.mkString(",")}/${y.mkString(",")})"

}
