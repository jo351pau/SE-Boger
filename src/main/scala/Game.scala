class Game(val fields: List[Field]) {

  def this(fields: Int, pieces: Int) =
    this(Game.create(DefaultInitialSetup(fields, pieces)))

  def this(setup: InitialSetup) = this(Game.create(setup))

  def copy(from: Int, to: Int, pieces: Int) : Game = {
    //TODO: Implement logic to move pieces
    Game(fields)
  }

  def winner : Option[Player] = Option.empty // TODO

  override def toString: String = fields.mkString("  ")

}

private object Game {
  def create(
      setup: InitialSetup
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
}

enum Player {
  case One
  case Two
}
