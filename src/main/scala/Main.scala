import scala.io.StdIn._

val NUMBER_OF_FIELDS = 24
val NUMBER_OF_FIGURES = 15
@main def run(): Unit = {
  startNewRound(Game(NUMBER_OF_FIELDS, NUMBER_OF_FIGURES))
}

def startNewRound(previousGame: Game): Game = {
  val game = rollAndMoveTwice(previousGame)

  game.winner
    .map(value => {
      // TODO: finish game
      game
    })
    .getOrElse {
      // TODO: pasch
      startNewRound(game)
    }
}

def rollAndMoveTwice(game: Game) : Game = {
  (Game.rollDice(), Game.rollDice()).match { case (res0, res1) =>
    println(s"You rolled $res0 and $res1.")
    askPositonAndMovePieces(game, res0, readInt)
    askPositonAndMovePieces(game, res1, readInt)
  }
}

def askPositonAndMovePieces(game: Game, pips: Int, readPosition: => Int): Game = {
  println(s"What u wanna do? Pips on dice: $pips")
  val position = readPosition
  //TODO check if movable figure on field
  game.copy(position, position + pips)
}