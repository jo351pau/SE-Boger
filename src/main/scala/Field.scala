class Field(val pieces: Int) {

  def this() = this(0)
  
  override def equals(field: Any): Boolean = field.match {
    case f: Field => f.pieces == pieces
    case _        => false
  }

  override def toString: String =
    if (pieces != 0) s"|$pieces|" else "|/|"
}
