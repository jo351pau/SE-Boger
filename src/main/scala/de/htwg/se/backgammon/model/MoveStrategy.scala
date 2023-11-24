package de.htwg.se.backgammon.model

import de.htwg.se.backgammon.validate.ValidateStrategy

class TryMove(
    val changes: Map[Int, Field] = Map(),
    val exception: Option[Exception] = None,
    val attack: Boolean = false
)

trait MoveStrategy {
  def execute(): TryMove
}

class BearOffMoveStrategy(val game: Game, val player: Player, val steps: Int){
  
}

class DefaultMoveStrategy(val game: Game, val from: Int, val steps: Int)
    extends MoveStrategy {
  val field_from = game.fields(from)
  val field_to = game.fields(to)
  val to =
    if (field_from.occupier == Player.White) from + steps
    else from - steps

  override def execute(): TryMove = {
    ValidateMove(game, from, steps, to).execute() match {
      case Some(value) => TryMove(exception = Some(value))
      case None        => move
    }
  }

  def move: TryMove = {
    if (field_from hasSameOccupier field_to) then TryMove(attack = true)
    else
      TryMove(
        changes = Map(
          from -> (field_from - 1),
          to -> (if (field_to.isEmpty())
                   Field(field_from.occupier)
                 else (field_to) + 1)
        )
      )
  }
}

class ValidateBearOffMove(
    val player: Player,
    val to: Field,
    val postion: Int
) extends ValidateStrategy {
  require(
    (player == to.occupier || to.occupier == Player.None || to.number <= 1),
    AttackNotPossibleException(-1, postion, to.number)
  )
}

class ValidateMove(val game: Game, val from: Int, val steps: Int, val to: Int)
    extends ValidateStrategy {
  val fields = game.fields

  require(fields(from).isOccupied(), EmptyFieldException(from))

  require(
    to < fields.length && to > 0,
    FieldDoesNotExistException(from, steps, to)
  )

  require(
    from < fields.length && from > 0,
    FieldDoesNotExistException(from, steps, from)
  )

  require(
    (fields(from) hasSameOccupier fields(to)) || fields(to).number <= 1,
    AttackNotPossibleException(from, to, fields(to).number)
  )
}
