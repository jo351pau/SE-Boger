package de.htwg.se.backgammon.controller

import de.htwg.se.backgammon.util.* 
import de.htwg.se.backgammon.model.* 
import scala.util.Try

case class Controller(var game: Game) extends Observable:
  val undoManager = new UndoManager[Game]
  def doAndPublish(doThis: Move => Try[Game], move: Move) =
    game = doThis(move).get
    PlayerState.next
    notifyObservers(Event.Move)
  def doAndPublish(doThis: => Try[Game]) =
    game = doThis.get
    notifyObservers(Event.Move)
  def put(move: Move): Try[Game] = undoManager.doStep(game, PutCommand(move))
  def quit: Unit = notifyObservers(Event.Quit)
  //def get(x: Int, y: Int): String = field.get(x, y).toString
  override def toString = game.toString

  object PlayerState:
    var player = Player.White
    def next = if player == Player.White then player = Player.Black else player = Player.White
