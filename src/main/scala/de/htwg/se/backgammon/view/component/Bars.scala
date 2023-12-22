package de.htwg.se.backgammon.view.component

import de.htwg.se.backgammon.model.IGame
import de.htwg.se.backgammon.view.component.util.MARGIN_TOP
import de.htwg.se.backgammon.model.Player
import de.htwg.se.backgammon.view.component.util.PLAYER_BOX_WIDTH
import scalafx.scene.input.MouseEvent
import de.htwg.se.backgammon.view.component.configuration.ColorPalette

class Bars(bars: Bar*) extends GUIList[Bar] {
  {
    bars.foreach(add(_))
  }

  def setGame(game: IGame) = {
    asList.foreach(bar => bar.set(game.bar(bar.player)))
  }

  def handleMouseEvent(
      event: MouseEvent,
      doHovering: GUIElement => Boolean = ((e) => true),
      onClicked: GUIElement => Unit = (e => None)
  ) = bars.foreach(p => p.handleMouseEvent(event, doHovering, onClicked))
}

object Bars {
  def createDefault(x: Double, y2: Double)(using colors: ColorPalette) = {
    Bars(
      Bar(colors, Player.Black, x - 5, MARGIN_TOP + PLAYER_BOX_WIDTH / 2),
      Bar(colors, Player.White, x - 5, y2)
    )
  }
}
