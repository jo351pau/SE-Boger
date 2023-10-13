class Row(val pieces: Int){

  def this() = this(0)

  def copy(newPieces: Integer): Row = new Row(pieces)

  override def toString: String = {
    if pieces > 0
        then pieces.toString()
    else
        "|"
  }
}
