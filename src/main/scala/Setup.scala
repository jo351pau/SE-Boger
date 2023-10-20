abstract class Setup(val fields: Int, val pieces: Int) {
  assert(fields % 2 == 0, s"number of fields {$fields} is odd!")

  def get: Map[Int, Int]
  protected def quarter(q: Int) = ((fields / 4) * q)
}

class DefaultSetup(fields: Int, pieces: Int) extends Setup(fields, pieces) {
  override def get: Map[Int, Int] =
    Map(
      0 -> Pieces.Most.value,
      quarter(1) -> -Pieces.Most.value,
      quarter(1) - 2 -> -Pieces.End.value,
      quarter(2) - 1 -> Pieces.Mid.value
    )

  private enum Pieces(calculate: Int => Int) {
    def value = calculate(pieces)

    case Most extends Pieces((n: Int) => (n / 3).toInt)
    case Mid extends Pieces((n: Int) => Calculation.remaining / 2)
    case End
        extends Pieces((n: Int) =>
          Calculation.remaining - (Calculation.remaining / 2)
        )
  }

  private object Calculation {
    def remaining = pieces - (Pieces.Most.value * 2)
  }
}
