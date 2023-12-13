package de.htwg.se.backgammon.view.component

import scalafx.scene.Group
import scala.util.Random

class Dice extends GUIList[Die] {
  def create(
      dice: Int,
      boardX: Double,
      boardY: Double,
      boardSize: Size
  ): Dice = {
    set(List.tabulate(dice)(_ => create(boardX, boardY, boardSize))); this
  }

  private def create(boardX: Double, boardY: Double, boardSize: Size): Die = {
    new Die(Random.between(1, 7), boardX, boardY, boardSize)
  }

  def roll(dots: List[Int]) = {
    asList.zipWithIndex.foreach((die: Die, index) => {
      if (dots.length > index) {
        die.visible = true
        die.roll(dots(index))
      } else { die.visible = false }
    })
  }

  private def randomizePosition(die: Die, shouldNotOverlapsWith: List[Die]) = {
    die.randomizePosition
    while (
      shouldNotOverlapsWith.find(other => other.overlaps(die).get()).isDefined
    ) {
      die.randomizePosition
    }
  }

  override def toString =
    if length > 0 then s"Dice: ${asList.mkString(",")}" else "Empty"
}
