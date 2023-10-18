val fields = 24
val numberOfPieces = 15
val game: Game = Game(fields, numberOfPieces)

@main def run(): Unit = {
  println(game)
  // TODO: call startNewRound
}

def startNewRound(game: Game): Game = {
  // TODO: roll the dice,
  // move pieces
  game.winner
    .map(value => {
      // TODO: finish game
      game
    })
    .getOrElse {
      startNewRound(game)
    }
}
