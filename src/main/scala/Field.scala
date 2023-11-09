class Field(val pieces: Int) {

  def this() = this(0)

  def this(occupier: Player) = this(
    occupier.match
      case Player.White => 1
      case Player.Black => -1
      case Player.None  => 0
  )

  override def equals(field: Any): Boolean = field.match {
    case f: Field => f.pieces == pieces
    case i: Int   => i == pieces
    case _        => false
  }

  override def toString: String =
    if (pieces != 0) s"|$pieces|" else "|/|"

  def +(that: Int): Field = withPieces(number + that)

  def -(that: Int): Field = withPieces(number - that)

  def isOccupied() = pieces != 0

  def isEmpty() = pieces == 0

  def hasSameOccupier(that: Field) =
    occupier == Player.None || that.occupier == Player.None || occupier == that.occupier

  def withPieces(number: Int): Field =
    if (pieces < 0) Field(-number) else Field(number)

  val number = pieces.abs

  val occupier =
    if (pieces == 0) Player.None
    else if (pieces > 0) Player.White
    else Player.Black
}
