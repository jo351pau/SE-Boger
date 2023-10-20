val NUMBER_OF_FIELDS = 12
val NUMBER_OF_FIGURES = 1
val game: Game = Game(NUMBER_OF_FIELDS, NUMBER_OF_FIGURES)

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
