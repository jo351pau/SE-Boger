package de.htwg.se.backgammon.view.component.util

import scalafx.scene.Group
import scalafx.scene.shape.Rectangle
import scalafx.scene.paint.Color
import de.htwg.se.backgammon.view.CustomColor
import scalafx.scene.text.Text
import scalafx.scene.text.Font
import de.htwg.se.backgammon.model.Player

val MARGIN_TOP = 25
val MARGIN_LEFT = 25
val PADDING = 40
val BOX_WIDTH = 50
val CORNER_RADIUS = 5

class PlayerState(parentWidth: Double, parentHeight: Double) extends Group {
  val mid = parentWidth / 2 - BOX_WIDTH / 2

  val xCoord = parentWidth - MARGIN_LEFT - BOX_WIDTH

  val black = new Rectangle {
    fill = Color.rgb(188, 138, 95)
    x = xCoord
    y = MARGIN_TOP
    width = BOX_WIDTH
    height = BOX_WIDTH
    arcWidth = CORNER_RADIUS
    arcHeight = CORNER_RADIUS
  }
  val blackBorder = new Rectangle {
    stroke = CustomColor.c1
    fill = null
    x = xCoord
    y = MARGIN_TOP
    width = BOX_WIDTH
    height = BOX_WIDTH
    arcWidth = CORNER_RADIUS
    arcHeight = CORNER_RADIUS
  }

  val emojiBlack: Text = new Text {
    text = "🎲"
    font = Font.font("Arial", 30)
    x = xCoord + 10
    y = MARGIN_TOP + BOX_WIDTH / 2 + 10
    smooth = true
    visible = false
  }

  val emojiWhite: Text = new Text {
    text = "🎲"
    font = Font.font("Arial", 30)
    x = xCoord + 10
    y = parentHeight - MARGIN_TOP - BOX_WIDTH + BOX_WIDTH / 2 + 10
    smooth = true
  }
  val white = new Rectangle {
    fill = Color.WHITESMOKE
    x = xCoord
    y = parentHeight - MARGIN_TOP - BOX_WIDTH
    width = BOX_WIDTH
    height = BOX_WIDTH
    arcWidth = CORNER_RADIUS
    arcHeight = CORNER_RADIUS
  }
  val whiteBorder = new Rectangle {
    stroke = CustomColor.c1
    fill = null
    x = xCoord
    y = parentHeight - MARGIN_TOP - BOX_WIDTH
    width = BOX_WIDTH
    height = BOX_WIDTH
    arcWidth = CORNER_RADIUS
    arcHeight = CORNER_RADIUS
  }
  children = Seq(white, whiteBorder, black, blackBorder, emojiWhite, emojiBlack)

  def set(player: Player) = player match {
    case Player.White =>
      emojiWhite.setVisible(true); emojiBlack.setVisible(false)
    case _ => emojiWhite.setVisible(false); emojiBlack.setVisible(true)
  }
}
