package de.htwg.se.backgammon.view.component

import scalafx.scene.canvas.GraphicsContext
import javax.swing.plaf.basic.BasicBorders.MarginBorder
import de.htwg.se.backgammon.view.CustomColor
import de.htwg.se.backgammon.model.Game

class Board(
    gc: GraphicsContext,
    x: Int,
    y: Int,
    size: Size,
    lineWidth: Int,
    points: List[Point] = List()
) {

  def draw: Unit = {
    drawBackground
    drawPoints
    drawGrid
  }

  private def drawBackground = {
    gc.setFill(CustomColor.c5)
    gc.fillRect(x, y, size.width, size.height)
  }

  private def drawPoints = this.points.foreach(_.draw)

  private def drawGrid = {
    gc.setStroke(CustomColor.c4)
    gc.setLineWidth(this.lineWidth)

    val lineWidth = 1
    val y = this.y - lineWidth
    gc.strokeRect(
      x - lineWidth,
      y,
      size.width + lineWidth * 2,
      size.height + lineWidth * 2
    )
    val middleLineX = x + size.width / 2
    gc.strokeLine(middleLineX, y, middleLineX, y + size.height)
  }

  def initPoints(number: Int, height: Double): Board = {
    val marginX = 5
    def margin(index: Int) = (if index == 0 then 0 else marginX * index)
    val numberOnSide = (number / 2)
    val width = (size.width - ((numberOnSide - 1) * marginX)) / numberOnSide
    val baseY = y + size.height
    val topSideY =
      (0 to (numberOnSide - 1))
        .map(x => Array(y, y, y + height): Array[Double])
        .toList
    val bottomSideY = (0 to (numberOnSide - 1))
      .map(x => Array(baseY, baseY, baseY - height))
      .toList
    val points = (0 to 1).flatMap { side =>
      (if (side == 1) (0 to ((number / 2) - 1))
       else
         ((((number / 2) - 1) to 0) by -1)).map(index =>
        Point(
          gc,
          Array(
            x + (index * width),
            x + ((index + 1) * width),
            x + ((index + 1) * width) - (width / 2)
          ).map(_ + margin(index)),
          if side == 1 then topSideY(index) else bottomSideY(index),
          if side == 1 then
            (if (index % 2 == 0) then CustomColor.c2 else CustomColor.c3
          )
          else (if (index % 2 == 0) then CustomColor.c3
                else CustomColor.c2)
        )
      )
    }.toList
    Board(gc, x, y, size, lineWidth, points)
  }

  def setPointsFromGame(game: Game) = {
    var points = this.points
    game.fields.zipWithIndex
      .map { case (v, i) =>
        (i, v)
      }
      .foreach((index, field) => points(index).field = field)
    Board(gc, x, y, size, lineWidth, points)
  }
}
