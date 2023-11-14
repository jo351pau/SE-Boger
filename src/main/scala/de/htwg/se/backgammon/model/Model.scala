package de.htwg.se.backgammon.model

class Model(private var _game: Game, var player: Player) {
  def this(game: Game) = this(game, Player.White)

  def next = if player == Player.White then player = Player.Black
  else player = Player.White

  var diceResults = (Dice.roll(), Dice.roll())

  var previousGame = _game

  def game_=(game: Game) = {
    previousGame = _game
    _game = game
  }

  def game = _game
}
