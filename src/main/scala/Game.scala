import scala.util.Random

class Game(val fields: List[Field]) {

  def this(fields: Int, pieces: Int) =
    this(Game.create(DefaultSetup(fields, pieces)))

  def this(setup: Setup) = this(Game.create(setup))

  def copy(from: Int, to: Int, pieces: Int): Game = {
    if fields(from).isEmpty() then
      println(s"Not possible! Field $from is empty.")
    else if !(fields(from) hasEnoughPieces pieces) then
      println(
        s"Not possible! ${fields(from).toPositive()} pieces on field $from, tried to move $pieces."
      )
    else if fields(to).isOccupied() && !(fields(from) hasSameSignAs fields(to))
    then println(s"Not possible! Different players on fields $from and $to.")
    else
      val value = if (fields(from).isNegative()) -pieces else pieces
      return copy(
        Map(
          from -> (fields(from) - value),
          to -> (fields(to) + value)
        )
      )
    this
  }

  private def copy(changes: Map[Int, Field]): Game =
    Game(
      List.tabulate(fields.length)(index =>
        changes.getOrElse(index, fields(index))
      )
    )

  def winner: Option[Player] = Option.empty // TODO

  override def toString: String = fields.mkString(" ")

  def ==(that: Game): Boolean = fields.equals(that.fields)

  def !=(that: Game): Boolean = !(this == that)

}

private object Game {
  def create(
      setup: Setup
  ): List[Field] = {
    val side = List.tabulate(setup.fields / 2)(index =>
      Field(setup.toMap.getOrElse(index, 0))
    )
    side ++ side.map(f => Field(-f.pieces)).reverse
  }

  def rollDice(): Int = Random.nextInt(6) + 1
}

enum Player {
  case One
  case Two
}
