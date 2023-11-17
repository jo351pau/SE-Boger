package de.htwg.se.backgammon.model

abstract class Setup(val fields: Int, val pieces: Int) {
  def toMap: Map[Int, Int]
  protected def quarter(q: Int) = ((fields / 4) * q)
}

class DefaultSetup(fields: Int, pieces: Int) extends Setup(fields, pieces) {
  assert(
    fields % 4 == 0,
    s"number of fields must be divisible by 4. {$fields} is not!"
  )
  assert(fields >= 12, s"number of fields must be bigger than {$fields}!")

  override def toMap: Map[Int, Int] =
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

class CustomSetup(fields: Int, pieces: Int) extends Setup(fields, pieces) {

  var fieldsList: List[Int] = _

  def this(list: List[Int]) = {
    this(list.length * 2, list.map(_.abs).sum)
    this.fieldsList = list
  }

  override def toMap: Map[Int, Int] = fieldsList.zipWithIndex.map {
    case (v, i) => (i, v)
  }.toMap

}
