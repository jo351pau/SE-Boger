package de.htwg.se.backgammon.view.component

object Configuration {
  val frameSize = Size(900, 700)

  val board = BoardConfiguration(boardSize, boardX, boardY, boardLineWidth)
  val boardSize = Size(800, 500)
  val boardX = (frameSize.width - boardSize.width) / 2
  val boardY = (frameSize.height - boardSize.height) / 2
  val boardLineWidth = 4

  val pointHeight = (boardSize.height / 2) * 0.9
  val pointMargin = 5

}

case class BoardConfiguration(size: Size, x: Double, y: Double, lineWidht: Int)
