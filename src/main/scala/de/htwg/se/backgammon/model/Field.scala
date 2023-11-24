package de.htwg.se.backgammon.model

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

  def +(that: Int): Field = copy(number + that)

  def -(that: Int): Field = copy(number - that)

  def isOccupied() = pieces != 0

  def isEmpty() = pieces == 0

  def hasSameOccupierAs(that: Field) = hasOccupier(that.occupier)

  def hasDifferentOccupierThen(that: Field) = !(hasOccupier(that.occupier))

  def hasOccupier(player: Player) : Boolean =
    occupier == Player.None || player == Player.None || occupier == player

  def hasNotOccupier(player: Player) = !(hasOccupier(player))

  def copy(number: Int): Field =
    if (pieces < 0) Field(-number) else Field(number)

  val number = pieces.abs

  val occupier =
    if (pieces == 0) Player.None
    else if (pieces > 0) Player.White
    else Player.Black
}
