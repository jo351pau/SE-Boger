package de.htwg.se.backgammon.model

import scala.util.Random
import scala.util.Try
import scala.util.Success

class Game(
    val fields: List[Field],
    val barWhite: Int = 0,
    val barBlack: Int = 0
) {

  def this(fields: Int, pieces: Int) =
    this(Game.create(DefaultSetup(fields, pieces)))

  def this(setup: Setup) = this(Game.create(setup))

  def move(player: Player, steps: Int): Try[Game] = {
    val to =
      if (player == Player.White) steps - 1
      else fields.length - steps
    Try({
      if (player != fields(to).occupier && fields(to).occupier != Player.None) {
        if (fields(to).number > 1) then
          throw AttackNotPossibleException(-1, to, fields(to).number)
        else {
          attack(player, to)
        }
      } else {
        val changes = Map(
          to -> (fields(to) + 1)
        )
        player match {
          case Player.White => copy(changes, barWhite = this.barWhite - 1)
          case Player.Black => copy(changes, barBlack = this.barBlack - 1)
          case Player.None  => this
        }
      }
    })
  }

  def move(from: Int, steps: Int, pieces: Int = 1): Try[Game] = {
    val to =
      if (fields(from).occupier == Player.White) from + steps
      else from - steps
    Try({
      if fields(from).isEmpty() then throw EmptyFieldException(from)

      if (to >= fields.length || to < 0) then
        throw FieldDoesNotExistException(from, steps, to)

      if (from >= fields.length || from < 0) then
        throw FieldDoesNotExistException(from, steps, from)

      if (!(fields(from) hasSameOccupier fields(to))) {
        if (fields(to).number > 1) then
          throw AttackNotPossibleException(from, to, fields(to).number)
        else {
          attack(from, to)
        }
      } else {
        copy(
          Map(
            from -> (fields(from) - 1),
            to -> (if (fields(to).isEmpty())
                     Field(fields(from).occupier)
                   else (fields(to) + 1))
          )
        )
      }
    })
  }

  private def attack(from: Int | Player, to: Int): Game = {
    val changes = from match {
      case from: Int =>
        Map(
          (from -> (fields(from) - 1)),
          (to -> Field(fields(from).occupier))
        )
      case player: Player => Map(to -> Field(player))
    }
    fields(to).occupier.match {
      case Player.White => copy(changes, barWhite = this.barWhite + 1)
      case Player.Black => copy(changes, barBlack = this.barBlack + 1)
      case Player.None  => this
    }
  }

  def copy(
      changes: Map[Int, Field],
      barWhite: Int = this.barWhite,
      barBlack: Int = this.barBlack
  ): Game =
    Game(
      List.tabulate(fields.length)(i => changes.getOrElse(i, fields(i))),
      barWhite = barWhite,
      barBlack = barBlack
    )

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
