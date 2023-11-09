import scala.io.StdIn.readLine
import scala.util.Success
import scala.util.Failure

val NUMBER_OF_FIELDS = 24
val NUMBER_OF_FIGURES = 15
val game: Game = Game(NUMBER_OF_FIELDS, NUMBER_OF_FIGURES)

@main def run(): Unit = {
  startNewRound(game)
}

def startNewRound(previousGame: Game): Any = {
  println(previousGame)

  val move =
    previousGame.move(Integer.parseInt(readLine), Integer.parseInt(readLine), 1)
  startNewRound(move match {
    case Success(value: Game) => value
    case Failure(exception) =>
      println(s"Not possible!: ${exception.getMessage}")
      previousGame
  })
}
