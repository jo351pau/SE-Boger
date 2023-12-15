package de.htwg.se.backgammon.view.component.util

import scalafx.scene.Group
import scalafx.scene.shape.Rectangle
import scalafx.scene.paint.Color
import scalafx.scene.text.Text
import scalafx.scene.text.Font
import de.htwg.se.backgammon.model.Player

val MARGIN_TOP = 25
val MARGIN_RIGHT = 25
val BOX_WIDTH = 50
val CORNER_RADIUS = 5

class PlayerState(parentWidth: Double, parentHeight: Double) extends Group {
  case class Entry(player: Player, y: Double, color: Color)
  var emojis: Map[Player, Text] = Map()

  val mid = parentWidth / 2 - BOX_WIDTH / 2

  val xCoord =
    parentWidth - MARGIN_RIGHT - BOX_WIDTH

  val yCoordWhite = parentHeight - MARGIN_TOP - BOX_WIDTH

  {
    val players = List(
      Entry(Player.Black, MARGIN_TOP, Color.rgb(188, 138, 95)),
      Entry(Player.White, yCoordWhite, Color.WHITESMOKE)
    )

    players.foreach(entry => {
      create(entry.y, entry.color); createEmoji(entry.y, entry.player)
    })

    set(Player.White)
  }

  def set(player: Player) =
    emojis.foreach(emoji => emoji._2.setVisible(emoji._1 == player))

  def add(children: Seq[Rectangle]) =
    children.foreach(c => this.children.add(c))

  def createEmoji(_y: Double, player: Player) = {
    val emoji = new Text {
      text = "🎲"
      font = Font.font("Arial", 30)
      x = xCoord + 10
      y = _y + +BOX_WIDTH / 2 + 10
      smooth = true
    }
    children.add(emoji)
    emojis = emojis ++ Map(player -> emoji)
  }

  def create(_y: Double, color: Color) = {
    val rectangle = new Rectangle {
      fill = color
      x = xCoord
      y = _y
      width = BOX_WIDTH
      height = BOX_WIDTH
      arcWidth = CORNER_RADIUS
      arcHeight = CORNER_RADIUS
    }
    val border = new Rectangle {
      stroke = CustomColor.c1
      fill = null
      x = xCoord
      y = _y
      width = BOX_WIDTH
      height = BOX_WIDTH
      arcWidth = CORNER_RADIUS
      arcHeight = CORNER_RADIUS
    }
    add(Seq(rectangle, border))
  }
}
