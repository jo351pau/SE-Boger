package de.htwg.se.backgammon.view.component

import scalafx.scene.canvas.GraphicsContext
import javax.swing.plaf.basic.BasicBorders.MarginBorder
import de.htwg.se.backgammon.model.Game
import scalafx.scene.layout.Pane
import scalafx.scene.shape.Rectangle
import scalafx.scene.shape.Line
import scalafx.scene.shape.Shape
import de.htwg.se.backgammon.model.Player
import javafx.scene.input.MouseEvent
import de.htwg.se.backgammon.view.GUI
import scalafx.scene.Group
import de.htwg.se.backgammon.view.component.util.CustomColor

class Board(
    val xCoord: Double,
    val yCoord: Double,
    val size: Size,
    lineWidth: Int
) extends Pane {
  def this(cf: BoardConfiguration) = this(cf.x, cf.y, cf.size, cf.lineWidht)

  var points: GUIList[Point] = GUIList()

  {
    children = Seq(Background(), points, Grid())
  }

  def activateCheckers(player: Player) = {
    points.foreach(p => p.activate(player))
  }

  def handleMouseEvent(
      event: MouseEvent,
      doHovering: GUIElement => Boolean = ((e) => true),
      onClicked: GUIElement => Unit = (e => None)
  ) = points.foreach(p => p.handleMouseEvent(event, doHovering, onClicked))

  def indexOf(p: Point) = points.indexOf(p)

  def setGame(game: Game, pointHeight: Double) = {
    if (game.length != points.length) {
      points.set(Board.createPoints(this, game.length, pointHeight))
    }
    game.fields.zipWithIndex
      .map { case (v, i) => (i, v) }
      .foreach((index, field) => points(index).set(field))
  }

  private class Background extends Rectangle {
    {
      width = size.width
      height = size.height
      x = xCoord
      y = yCoord
      fill = CustomColor.c5
      mouseTransparent = true
    }
  }

  private class Grid extends Group {
    {
      val lineWidthSmall = 1
      val middleLineX = xCoord + size.width / 2

      children = Seq(
        new Rectangle {
          width = size.width + lineWidthSmall * 2
          height = size.height + lineWidthSmall * 2
          this.x = xCoord - lineWidthSmall
          this.y = yCoord - lineWidthSmall
        },
        new Line {
          startX = middleLineX
          endX = middleLineX
          startY = yCoord
          endY = yCoord + size.height
        }
      ).map(s => {
        s.fill = null
        s.mouseTransparent = true
        s.stroke = CustomColor.c4
        s.strokeWidth = lineWidth; s
      })
    }
  }
}

object Board {
  def margin(index: Int) =
    if index == 0 then 0 else Configuration.pointMargin * index

  def totalMargin(pointsPerSide: Int) =
    (pointsPerSide - 1) * Configuration.pointMargin

  def determineWidth(width: Double, pointsPerSide: Int): Double =
    (width - totalMargin(pointsPerSide)) / pointsPerSide

  def color(side: Int, position: Int) = {
    if (side == 1) {
      if (position % 2 == 0) then CustomColor.c2 else CustomColor.c3
    } else if (position % 2 == 0) then CustomColor.c3
    else CustomColor.c2
  }

  def createXArray(boardX: Double, position: Int, width: Double) = {
    val positionOffset = (position * width)
    val x = boardX + positionOffset

    Array(x, x + width, x + (width / 2))
  }

  def createSideRange(side: Int, pointsPerSide: Int): Range = {
    if (side == 1) then (0 to (pointsPerSide - 1))
    else ((pointsPerSide - 1) to 0) by -1
  }

  def createPoints(board: Board, number: Int, height: Double) = {
    val (size, xCoord, yCoord) = (board.size, board.xCoord, board.yCoord)

    val pointsPerSide = (number / 2)
    val width = determineWidth(size.width, pointsPerSide)

    val baseY = yCoord + size.height
    val topSideY = Array(yCoord, yCoord, yCoord + height)
    val bottomSideY = Array(baseY, baseY, baseY - height)

    (0 to 1).flatMap { side =>
      createSideRange(side, pointsPerSide).map(index =>
        Point(
          createXArray(xCoord, index, width).map(_ + margin(index)),
          if side == 1 then topSideY else bottomSideY,
          size = Size(width, height),
          color(side, index)
        )
      )
    }.toList
  }
}
