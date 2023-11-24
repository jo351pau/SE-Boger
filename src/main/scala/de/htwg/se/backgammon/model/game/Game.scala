package de.htwg.se.backgammon.model

import scala.util.Random
import scala.util.Try
import scala.util.Success
import java.util.Map.Entry
import scala.util.Failure

trait GameSeq(private val fields: List[Field]) extends IndexedSeq[Field] {
  override def apply(i: Int): Field = fields(i)
  override def length: Int = fields.length
}

class Game(
    val fields: List[Field],
    val barWhite: Int = 0,
    val barBlack: Int = 0
) extends GameSeq(fields) {

  def this(fields: Int, pieces: Int) =
    this(Game.create(DefaultSetup(fields, pieces)))

  def this(setup: Setup) = this(Game.create(setup))

  def move(move: Move): Try[Game] = {
    val to = whereToGo(move)
    val validateStrategy = move match {
      case move: BearOffMove =>
        ValidateBearOffMoveStrategy(this, move.player, to)
      case Move(from, steps) =>
        DefaultValidateMoveStrategy(this, move.from, to)
    }
    validateStrategy.execute() match {
      case Some(value) => Failure(value)
      case None        => Success(MoveStrategy(this, move, to).execute())
    }
  }

  def whereToGo(move: Move) = move match {
    case move: BearOffMove =>
      if (move.player == Player.White)
        move.steps - 1
      else length - move.steps
    case Move(from, steps) =>
      if (fields(move.from).occupier == Player.White)
        from + steps
      else from - steps
  }

  def get(position: Int) = fields(position)

  def winner: Option[Player] =
    if !fields.exists(_.occupier == Player.White) then Some(Player.White)
    else if !fields.exists(_.occupier == Player.Black) then Some(Player.Black)
    else Option.empty

  def homeBoards = Map(
    Player.White -> fields.dropRight(fields.length * (3 / 4)),
    Player.Black -> fields.drop(fields.length * (3 / 4))
  )

  val numberOfPieces: Int = fields.map(_.number).sum / 2

  override def toString: String =
    s"$barWhite : ${fields.mkString(" ")} : $barBlack"

  def ==(that: Game): Boolean = fields.equals(that.fields)

  def !=(that: Game): Boolean = !(this == that)

}

private object Game {
  def create(
      setup: Setup
  ): List[Field] = {
    val side = List.tabulate(setup.fields / 2)(index =>
      Field(setup.toMap.getOrElse(index, 0))
    )
    side ++ side.map(f => Field(-f.pieces)).reverse
  }
}
