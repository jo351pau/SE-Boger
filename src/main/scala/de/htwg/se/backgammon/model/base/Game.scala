package de.htwg.se.backgammon.model.base

import de.htwg.se.backgammon.model.strategy.MoveStrategy
import de.htwg.se.backgammon.model.strategy.ValidateBearInMoveStrategy
import de.htwg.se.backgammon.model.strategy.DefaultValidateMoveStrategy

import scala.util.Random
import scala.util.Try
import scala.util.Success
import java.util.Map.Entry
import scala.util.Failure
import org.scalactic.Fail
import de.htwg.se.backgammon.model.strategy.ValidateBearOffMoveStrategy
import base.Field
import de.htwg.se.backgammon.model.strategy.{DefaultValidateMoveStrategy, MoveStrategy, ValidateBearInMoveStrategy}

case class Game(fields: List[Field], barWhite: Int = 0, barBlack: Int = 0)
    extends GameSeq(fields) {

  def this(fields: Int, pieces: Int) =
    this(Game.create(DefaultSetup(fields, pieces)))

  def this(setup: Setup) = this(Game.create(setup))

  def move(move: Move): Try[Game] = {
    val to = move.whereToGo(this)
    val validateStrategy = move match {
      case move: BearInMove =>
        ValidateBearInMoveStrategy(this, move.player, to)
      case _: BearOffMove    => ValidateBearOffMoveStrategy(this, move.from, to)
      case Move(from, steps) => DefaultValidateMoveStrategy(this, from, to)
    }
    validateStrategy.execute() match {
      case Failure(ex) => Failure(ex)
      case _           => Success(MoveStrategy(this, move, to).execute())
    }
  }

  def get(position: Int) = fields(position)

  def winner: Option[Player] =
    if !fields.exists(_.occupier == Player.White) then Some(Player.White)
    else if !fields.exists(_.occupier == Player.Black) then Some(Player.Black)
    else Option.empty

  def homeBoard = Map(
    Player.White -> fields.drop((fields.length / 4) * 3),
    Player.Black -> fields.dropRight((fields.length / 4) * 3)
  )

  def numberOfPieces = Map(
    Player.White -> fields.filter(_.occupier == Player.White).map(_.number).sum,
    Player.Black -> fields.filter(_.occupier == Player.Black).map(_.number).sum
  )

  def bar = Map(Player.White -> barWhite, Player.Black -> barBlack)

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

case class GameState(game: Game, move: Move) {
  def isValid = !move.isInstanceOf[NoMove]
}

object GameState {
  def invalid = GameState(Game(List()), NoMove())
}

trait GameSeq(private val fields: List[Field]) extends IndexedSeq[Field] {
  override def apply(i: Int): Field = fields(i)
  override def length: Int = fields.length
}
