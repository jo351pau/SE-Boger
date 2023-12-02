package de.htwg.se.backgammon.view.component

import scalafx.scene.canvas.GraphicsContext

abstract class Drawable(gc: GraphicsContext) {
  def draw : Unit
}
