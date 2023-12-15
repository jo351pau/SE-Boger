package de.htwg.se.backgammon.view.component.util.configuration

import scalafx.scene.paint.Color
import scalafx.scene.effect.DropShadow

trait Configuration

class ColorConfiguration extends Configuration {
  def c1 = Color.rgb(1, 25, 54)
  def c2 = Color.rgb(70, 83, 98)
  def c3 = Color.rgb(130, 163, 161)
  def c4 = Color.rgb(159, 196, 144)
  def c5 = Color.rgb(192, 223, 161)

  def player = PlayerColorCf()
  def checker = CheckerColorCf()
}

class PlayerColorCf extends Configuration {
  def white = Color.WHITESMOKE
  def black = Color.rgb(188, 138, 95)
}

class CheckerColorCf extends Configuration {
  def shadow = new DropShadow(3.0, Color.BLACK)
  def hoverShadow = new DropShadow(5.0, Color.BLACK)
}
