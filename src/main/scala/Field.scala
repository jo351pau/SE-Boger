class Field(val pieces: Int){

  def this() = this(0)

  def copy(newPieces: Integer): Field = Field(pieces)

  override def toString: String = if (pieces > 0) s"|$pieces|" else "|/|"
}
