import scala.util.Random
private val MAX_PIECES_ON_FIELD_WHEN_ATTACKING = 1

class Game(
    val fields: List[Field],
    val barWhite: Int = 0,
    val barBlack: Int = 0
) {

  def this(fields: Int, pieces: Int) =
    this(Game.create(DefaultSetup(fields, pieces)))

  def this(setup: Setup) = this(Game.create(setup))

  def move(from: Int, to: Int, pieces: Int = 1): Game = {
    if !(fields(from) hasEnoughPieces pieces) then
      println(
        s"Not possible! ${fields(from).toPositive()} pieces on field $from, tried to move $pieces."
      )
    else if fields(to).isOccupied() && !(fields(from) hasSameSignAs fields(to))
    then
      if (fields(to).toPositive() <= MAX_PIECES_ON_FIELD_WHEN_ATTACKING) then
        return attack(from, to)
      else println(s"Not possible! Different players on fields $from and $to.")
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

  private def attack(from: Int, to: Int): Game = {
    val value = if (fields(from).isNegative()) -1 else 1
    fields(from).getPlayer() match {
      case Player.White =>
        println("removed black stone")
        copy(
          Map(
            from -> (fields(from) - 1),
            to -> Field(1)
          ),
          barBlack = this.barBlack + 1
        )
      case Player.Black =>
        println("removed white stone")
        copy(
          Map(
            from -> (fields(from) + 1),
            to -> Field(-1)
          ),
          barWhite = this.barWhite + 1
        )
      case Player.None =>
        game
    }
  }

  private def copy(
      changes: Map[Int, Field],
      barWhite: Int = this.barWhite,
      barBlack: Int = this.barBlack
  ): Game =
    Game(
      List.tabulate(fields.length)(index =>
        changes.getOrElse(index, fields(index)),
      ),
      barWhite = barWhite,
      barBlack = barBlack
    )

  def winner: Option[Player] =
    if !fields.exists(_.getPlayer() == Player.White) then Some(Player.White)
    else if !fields.exists(_.getPlayer() == Player.Black) then
      Some(Player.Black)
    else Option.empty

  override def toString: String =
    s"$barWhite : ${fields.mkString(" ")} : $barBlack"

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
  case White
  case Black
  case None
}
