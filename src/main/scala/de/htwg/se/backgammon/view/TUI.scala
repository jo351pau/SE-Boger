package de.htwg.se.backgammon.view

import scala.io.StdIn.readLine
import de.htwg.se.backgammon.model.* 
import de.htwg.se.backgammon.util.* 
import de.htwg.se.backgammon.controller.* 

class TUI(controller: Controller) extends Observer:
  controller.add(this)
  var continue = true
  def run =
    println(controller.game)
    inputLoop()

  override def update(e: Event) =
    e match
      case Event.Quit => continue = false
      case Event.Move => println(controller.game)

  def inputLoop(): Unit =
    analyseInput(readLine) match
      case None       =>
      case Some(move : Move) => controller.doAndPublish(controller.put, move)

    if continue then inputLoop()

  def analyseInput(input: String): Option[Move] =
    input match
      case "q" => controller.quit; None
      case _ => {
        val positions = input.split(" ")
        val from = Integer.parseInt(positions(0))
        val to = Integer.parseInt(positions(1))
        Some(Move(from, to))
      }
