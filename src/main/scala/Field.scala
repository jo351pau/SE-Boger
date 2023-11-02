class Field(val pieces: Int) {

  def this() = this(0)

  override def equals(field: Any): Boolean = field.match {
    case f: Field => f.pieces == pieces
    case i: Int   => i == pieces
    case _        => false
  }

  override def toString: String =
    if (pieces != 0) s"|$pieces|" else "|/|"

  def +(that: Int): Field = Field(pieces + that)

  def -(that: Int): Field = Field(pieces - that)

  def toPositive() = if (pieces < 0) -pieces else pieces

  def isNegative() = pieces < 0

  def isOccupied() = pieces != 0

  def isEmpty() = pieces == 0

  def hasSameSignAs(that: Field) = this.pieces * that.pieces < 0

  def hasEnoughPieces(pieces: Int) = toPositive() - pieces >= 0
}
