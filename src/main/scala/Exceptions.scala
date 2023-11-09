class MoveException extends Exception {}

class EmptyFieldException(val position: Int) extends MoveException {
  override def getMessage() = s"Field $position is empty!"
}

class AttackNotPossibleException(val from: Int, val to: Int, val pieces: Int)
    extends MoveException {
  override def getMessage() =
    s"Different players on fields $from and $to {$pieces pieces on field}."
}
