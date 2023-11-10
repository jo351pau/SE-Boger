package de.htwg.se.backgammon.model

class Model(var game: Game, var player: Player) {
  def this(game: Game) = this(game, Player.White)

  def next = if player == Player.White then player = Player.Black
  else player = Player.White
}
