package de.htwg.se.backgammon.model

class MoveException extends Exception {}

case class EmptyFieldException(val position: Int) extends MoveException {
  override def getMessage() = s"Field $position is empty!"
}

case class AttackNotPossibleException(
    val from: Int,
    val to: Int,
    val pieces: Int
) extends MoveException {
  override def getMessage() =
    s"Different players on fields $from and $to {$pieces pieces on field}."
}

case class NotYourFieldException(
    val from: Int,
    val playerFrom: Player,
    val currentPlayer: Player
) extends MoveException {
  override def getMessage() =
    s"You are $currentPlayer, but $playerFrom is on field $from."
}

case class WrongDirectionException(val player: Player) extends MoveException {
  override def getMessage() =
    s"It's not possible to move to the $wrongDirection, because you are $player."

  def wrongDirection = if (player == Player.White) "left" else "right"
}

case class DieNotExistException(val steps: Int, val dice: List[Int])
    extends MoveException {
  override def getMessage() =
    s"You can't move $steps steps. You rolled${dice.map(die => s" a $die").mkString(" and")}"
}

case class FieldDoesNotExistException(
    val position: Int,
    val steps: Int,
    val notExistingPosition: Int
) extends MoveException {
  override def getMessage() =
    s"You can't move $steps steps from $position, because field $notExistingPosition doesn't exist."
}
