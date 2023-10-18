class Field(val pieces: Int) {

  def this() = this(0)

  override def toString: String =
    if (pieces != 0) s"|$pieces|" else "|/|"

}
