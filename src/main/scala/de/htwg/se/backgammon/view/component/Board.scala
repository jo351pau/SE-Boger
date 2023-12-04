package de.htwg.se.backgammon.view.component

import scalafx.scene.canvas.GraphicsContext
import javax.swing.plaf.basic.BasicBorders.MarginBorder
import de.htwg.se.backgammon.view.CustomColor
import de.htwg.se.backgammon.model.Game
import scalafx.scene.layout.Pane
import scalafx.scene.shape.Rectangle
import scalafx.scene.shape.Line
import scalafx.scene.shape.Shape
import de.htwg.se.backgammon.model.Player
import javafx.scene.input.MouseEvent

class Board(
    x: Double,
    y: Double,
    size: Size,
    lineWidth: Int,
    var points: List[Point] = List()
) extends Pane {
  def xCoord = x
  def yCoord = y

  def activateCheckers(player: Player) = {
    points.foreach(p => p.activate(player))
  }

  def getCheckers(): List[Checker] = points.flatMap(p => p.getCheckers())

  def find(p: Point => Boolean) = points.find(p)
  def indexOf(p: Point => Boolean) = points.indexOf(find(p).getOrElse(None))
  def indexOfPointAt(event: MouseEvent) = indexOf(_.isOn(event))

  def init = {
    children = Seq()
    children.add(drawBackground)
    points.foreach(children.add(_))
    drawGrid.foreach(children.add(_))
  }

  private def drawBackground: Rectangle = {
    new Rectangle {
      width = size.width
      height = size.height
      this.setX(xCoord)
      this.setY(yCoord)
      this.setFill(CustomColor.c5)
      mouseTransparent = true
    }
  }

  private def drawGrid: Seq[Shape] = {
    val lineWidthSmall = 1
    val grid = new Rectangle {
      width = size.width + lineWidthSmall * 2
      height = size.height + lineWidthSmall * 2
      this.setStroke(CustomColor.c4)
      this.setFill(null)
      this.setStrokeWidth(lineWidth)
      this.setX(xCoord - lineWidthSmall)
      this.setY(yCoord - lineWidthSmall)
      mouseTransparent = true
    }

    val middleLineX = x + size.width / 2
    val middleLine = new Line {
      this.setStartX(middleLineX)
      this.setEndX(middleLineX)
      this.setStartY(yCoord)
      this.setEndY(yCoord + size.height)
      this.setStroke(CustomColor.c4)
      this.setFill(null)
      this.setStrokeWidth(lineWidth)
      mouseTransparent = true
    }
    Seq(grid, middleLine)
  }

  private def createPoints(number: Int, height: Double): List[Point] = {
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
    (0 to 1).flatMap { side =>
      (if (side == 1) (0 to ((number / 2) - 1))
       else
         ((((number / 2) - 1) to 0) by -1)).map(index =>
        Point(
          Array(
            x + (index * width),
            x + ((index + 1) * width),
            x + ((index + 1) * width) - (width / 2)
          ).map(_ + margin(index)),
          if side == 1 then topSideY(index) else bottomSideY(index),
          size = Size(width, height),
          if side == 1 then
            (if (index % 2 == 0) then CustomColor.c2 else CustomColor.c3
          )
          else (if (index % 2 == 0) then CustomColor.c3
                else CustomColor.c2)
        )
      )
    }.toList
  }

  def setGame(game: Game, pointHeight: Double) = {
    this.points = createPoints(game.length, pointHeight)
    init

    game.fields.zipWithIndex
      .map { case (v, i) =>
        (i, v)
      }
      .foreach((index, field) => points(index).field = field)
  }
}
