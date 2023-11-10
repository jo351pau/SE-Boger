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

  def move(from: Int, to: Int, pieces: Int = 1): Try[Game] = {
    Try({
      if fields(from).isEmpty() then throw EmptyFieldException(from)

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

  private def attack(from: Int, to: Int): Game = {
    val changes = Map(
      from -> (fields(from) - 1),
      to -> Field(fields(from).occupier)
    )
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

  def rollDice(): Int = Random.nextInt(6) + 1
}