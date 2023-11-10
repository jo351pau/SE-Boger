package de.htwg.se.backgammon.controller

import de.htwg.se.backgammon.util.*
import de.htwg.se.backgammon.model.* 
import scala.util.Try

class PutCommand(move: Move) extends Command[Game]:
  override def noStep(game: Game): Game = game
  override def doStep(game: Game): Try[Game] = game.move(move.from, move.to)