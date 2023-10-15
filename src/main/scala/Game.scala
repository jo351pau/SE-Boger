class Game(val fields: List[Field]) {

  def this(numberOfFields: Int, numberOfPieces: Int) = {
    this(Game.createFieldsWithInitialSetup(numberOfFields, numberOfPieces))
  }

  override def toString: String = fields.mkString("  ")

}

private object Game {
  def createFieldsWithInitialSetup(
      fields: Int,
      pieces: Int
  ): List[Field] = {

    def createField(index: Int): Field = {
      def quarter(q: Int) = ((fields / 4) * q)

      val number = index match {
        case 0 => NumberOfPieces.Begin.value(pieces)
        case secondQ if secondQ == quarter(1) =>
          -NumberOfPieces.Begin.value(pieces)
        case endTwo if endTwo == quarter(1) - 2 =>
          -NumberOfPieces.End.value(pieces)
        case midTwo if midTwo == quarter(2) - 1 =>
          NumberOfPieces.Mid.value(pieces)
        case _: Int => 0
      }
      Field(number)
    }
    val side = List.tabulate(fields / 2)(index => createField(index))
    side ++ side.map(f => Field(-f.pieces)).reverse
  }

  private def remaining(n: Int) = n - (NumberOfPieces.Begin.value(n) * 2)
  private enum NumberOfPieces(calculate: Int => Int) {
    def value(number: Int) = calculate(number)

    case Begin extends NumberOfPieces((n: Int) => (n / 3).toInt)
    case Mid extends NumberOfPieces((n: Int) => remaining(n) / 2)
    case EndQ2 extends NumberOfPieces((n: Int) => (n / 3).toInt)
    case End
        extends NumberOfPieces((n: Int) => remaining(n) - (remaining(n) / 2))
  }
}
