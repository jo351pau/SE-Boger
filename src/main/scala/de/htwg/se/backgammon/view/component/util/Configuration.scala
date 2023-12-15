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

class PlayerStateConfiguration {
  val MARGIN_TOP = 25
  val MARGIN_RIGHT = 25
  val BOX_WIDTH = 50
  val CORNER_RADIUS = 5
}

class BarConfiguration {
  val PLAYER_STATE_CONFIGURATION = PlayerStateConfiguration()

  val MARGIN_TOP = PLAYER_STATE_CONFIGURATION.MARGIN_TOP
  val MARGIN_RIGHT = PLAYER_STATE_CONFIGURATION.MARGIN_RIGHT
  val BOX_WIDTH = PLAYER_STATE_CONFIGURATION.BOX_WIDTH
}

case class BoardConfiguration(size: Size, x: Double, y: Double, lineWidht: Int)
