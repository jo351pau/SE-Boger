package de.htwg.se.backgammon.view.component

object Configuration {
  val frameSize = Size(800, 600)

  val boardSize = Size(700, 400)
  def boardX = (frameSize.width - boardSize.width) / 2
  def boardY = (frameSize.height - boardSize.height) / 2
  def boardLineWidth = 4

  def pointHeight = (boardSize.height / 2) * 0.9

}
