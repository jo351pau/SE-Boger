class Game(val fields: List[Field]) {

  def this(fields: Int, pieces: Int) =
    this(Game.createFields(DefaultInitialSetup(fields, pieces)))

  def this(setup: InitialSetup) = this(Game.createFields(setup))

  override def toString: String = fields.mkString("  ")

}

private object Game {
  def createFields(
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
