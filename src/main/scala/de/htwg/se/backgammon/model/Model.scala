package de.htwg.se.backgammon.model

val MOVES_PER_ROUND = 2

class Model(private var _game: Game, var player: Player) {
  def this(game: Game) = this(game, Player.White)

  def next = {
    if player == Player.White then player = Player.Black
    else player = Player.White
    movesThisRound = Nil; player
  }

  var dice = Dice.roll(MOVES_PER_ROUND)

  var previousGame = _game

  def game_=(game: Game) = {
    previousGame = _game
    _game = game
    movesThisRound = movesThisRound.::(game)
  }

  def game = _game

  var movesThisRound: List[Game] = Nil
}
