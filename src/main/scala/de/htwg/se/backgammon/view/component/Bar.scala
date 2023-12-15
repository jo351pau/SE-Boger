package de.htwg.se.backgammon.view.component

import scalafx.scene.Group
import de.htwg.se.backgammon.model.Player

case class Bar(player: Player, xCoord: Double, yCoord: Double) extends Group {
  private val checkers: GUIList[Checker] = GUIList()

  {
    children = Seq(checkers)
  }

  def createChecker(position: Int) = {
    checkers.add(
      Checker(
        player,
        getX(position),
        yCoord,
        false,
        null
      )
    )
  }

  def getX(position: Double) = xCoord - (position * (CHECKER_SIZE + PADDING))

  def set(checkers: Int) = {
    this.checkers.clear()
    if (checkers > 0) {
      (checkers to 0).foreach(position => createChecker(position))
    }
  }

}
