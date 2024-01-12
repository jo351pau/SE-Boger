package de.htwg.se.backgammon.model.base

import de.htwg.se.backgammon.model.Player
import de.htwg.se.backgammon.model.IModel
import de.htwg.se.backgammon.model.IGame
import de.htwg.se.backgammon.model.IDice
import scala.xml.Elem
import play.api.libs.json.Reads
import play.api.libs.json.Writes
import play.api.libs.json.Json
import play.api.libs.json.JsValue
import scala.xml.Node

val MOVES_PER_ROUND = 2

case class Model(
    private var _game: IGame,
    var player: Player,
    val diceStrategy: IDice
) extends IModel {
  def this(game: Game, diceStrategy: IDice) =
    this(game, Player.White, diceStrategy)

  def next = {
    if player == Player.White then player = Player.Black
    else player = Player.White
    movesThisRound = Nil; player
  }

  var dice = diceStrategy.roll(MOVES_PER_ROUND)

  def roll = { dice = diceStrategy.roll(MOVES_PER_ROUND); dice }

  var previousGame = _game

  def game_=(game: IGame) = {
    previousGame = _game
    _game = game
    movesThisRound = movesThisRound.::(game)
  }

  def game = _game

  var movesThisRound: List[IGame] = Nil

  def clone() = {
    var model = new Model(game, player, diceStrategy)
    model.dice = dice; model
  }

}

object Model {

  implicit val modelReads: Reads[Model] = Json.reads[Model]
  implicit val modelWrites: Writes[Model] = Json.writes[Model]

  def fromJson(json: JsValue) = {
    json.asOpt[Model] match
      case Some(model: Model) => model
      case _ =>
        throw new IllegalArgumentException(
          s"Json can't be converted to a game!"
        )
  }

  def fromXml(xml: Node) : Model = {
    val game = Game.fromXml((xml \ "game")(0))
    val player = Player.withName((xml \ "current").text)
    val dice =
      (xml \ "dice" \ "die").map(node => node.text.toInt).toList

    var model = new Model(game, player, new Dice())
    model.dice = dice; model
  }
}
