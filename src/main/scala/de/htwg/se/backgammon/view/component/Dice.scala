package de.htwg.se.backgammon.view.component

import scalafx.scene.image.ImageView
import scalafx.scene.image.Image
import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random
import scala.io.Source
import de.htwg.se.backgammon.view.GUI.board
import javafx.scene.input.MouseEvent
import scalafx.animation.Timeline
import scalafx.animation.KeyFrame
import scalafx.util.Duration

import scala.concurrent.duration._
import java.util.concurrent._
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import java.util.concurrent.ScheduledThreadPoolExecutor

val DICE_SIZE = 40

class Dice(
    var _dots: Int,
    val boardX: Double,
    val boardY: Double,
    val boardSize: Size
) extends ImageView {
  def dots = _dots
  def dots_=(d: Int) = {
    image = imageWithDots(d)
    _dots = d
  }
  {
    val padding = 100
    val boardMiddleY = boardY + (boardSize.height / 2) - DICE_SIZE / 2
    image = imageWithDots(_dots)
    x = boardX + padding + Random.between(0, boardX + boardSize.width - padding)
    y = boardMiddleY + Random.between(-20, 20)
    fitHeight = 40
    fitWidth = 40
  }

  def roll(number: Int) = {
    val padding = 100
    val boardMiddleY = boardY + (boardSize.height / 2) - DICE_SIZE / 2
    image = imageWithDots(_dots)
    x = boardX + padding + Random.between(0, boardSize.width - (padding * 2))
    y = boardMiddleY + Random.between(-20, 20)
    rotate = Random.between(-25, 25)

    val ex = new ScheduledThreadPoolExecutor(1)
    var future: ScheduledFuture[_] = null
    val task: Runnable = new Runnable {

      val repetitions = 10
      var done = 0
      def run(): Unit = {
        val random = Random.between(1, 7)
        dots = if random == dots then Random.between(1, 7) else { random }
        done = done + 1
        if (done >= repetitions) {
          dots = number
          future.cancel(true)
        }
      }
    }
    future = ex.scheduleAtFixedRate(task, 1, 100, TimeUnit.MILLISECONDS)
  }

  private def imageWithDots(dots: Int) = new Image(
    s"file:src/main/resources/$dots.png"
  )

  private def randomImage: Image = imageWithDots(Random.between(1, 7))
}
