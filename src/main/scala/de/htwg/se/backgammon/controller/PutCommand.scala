package de.htwg.se.backgammon.controller

import de.htwg.se.backgammon.util.*
import scala.util.Try
import de.htwg.se.backgammon.model.Move
import de.htwg.se.backgammon.model.Game
import de.htwg.se.backgammon.model.BearOffMove

class PutCommand(move: Move) extends Command[Game]:
  override def noStep(game: Game): Game = game
  override def doStep(game: Game): Try[Game] = move match {
    case move: BearOffMove => game.move(move.player, move.steps)
    case _                 => game.move(move.from, move.steps)
  }
