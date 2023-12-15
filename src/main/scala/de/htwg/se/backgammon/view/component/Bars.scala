package de.htwg.se.backgammon.view.component

import de.htwg.se.backgammon.model.Game
import de.htwg.se.backgammon.view.component.util.MARGIN_TOP
import de.htwg.se.backgammon.model.Player

class Bars(bars: Bar*) extends GUIList[Bar] {
  {
    bars.foreach(add(_))
  }

  def setGame(game: Game) = {
    asList.foreach(bar => bar.set(game.bar(bar.player)))
  }
}

object Bars {
  def createDefault(x: Double, y2: Double) = {
    Bars(Bar(Player.Black, x, MARGIN_TOP), Bar(Player.White, x, y2))
  }
}
