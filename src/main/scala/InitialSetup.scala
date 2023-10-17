abstract class InitialSetup(val fields: Int, val pieces: Int) {
  def get: Map[Int, Int]
  protected def quarter(q: Int) = ((fields / 4) * q)
}

class DefaultInitialSetup(fields: Int, pieces: Int)
    extends InitialSetup(fields, pieces) {
  override def get: Map[Int, Int] =
    Map(
      0 -> NumberOfPieces.Most.value(pieces),
      quarter(1) -> -NumberOfPieces.Most.value(pieces),
      quarter(1) - 2 -> -NumberOfPieces.End.value(pieces),
      quarter(2) - 1 -> -NumberOfPieces.Mid.value(pieces)
    )

  private def remaining(n: Int) = n - (NumberOfPieces.Most.value(n) * 2)
  private enum NumberOfPieces(calculate: Int => Int) {
    def value(number: Int) = calculate(number)

    case Most extends NumberOfPieces((n: Int) => (n / 3).toInt)
    case Mid extends NumberOfPieces((n: Int) => remaining(n) / 2)
    case End
        extends NumberOfPieces((n: Int) => remaining(n) - (remaining(n) / 2))
  }
}
