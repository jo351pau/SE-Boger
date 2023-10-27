import scala.util.Random
class Game(val fields: List[Field]) {
  println(this)
  
  def this(fields: Int, pieces: Int) =
    this(Game.create(DefaultSetup(fields, pieces)))

  def this(setup: Setup) = this(Game.create(setup))

  def copy(from: Int, to: Int): Game = {
    // TODO: Implement logic to move pieces
    Game(fields)
  }

  def winner: Option[Player] = Option.empty // TODO

  override def toString: String = fields.mkString(" ")
}

private object Game {
  def create(
      setup: Setup
  ): List[Field] = {
    val side = List.tabulate(setup.fields / 2)(index =>
      Field(
        setup.get
          .collectFirst {
            case (position, pieces) if position == index => pieces
          }
          .getOrElse(0)
      )
    )
    side ++ side.map(f => Field(-f.pieces)).reverse
  }

  def rollDice(): Int = {
    return Random.nextInt(5) + 1
  }
}

enum Player {
  case One
  case Two
}
