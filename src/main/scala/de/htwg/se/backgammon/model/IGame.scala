package de.htwg.se.backgammon.model

import de.htwg.se.backgammon.model.storage.Storable
import scala.util.Try
import play.api.libs.json.Reads
import play.api.libs.json.Json
import play.api.libs.json.Writes
import de.htwg.se.backgammon.model.base.Game
import play.api.libs.json.JsValue
import play.api.libs.json.JsResult

trait IGame extends IndexedSeq[IField] with Storable {
  def fields: List[IField]
  def barWhite: Int
  def barBlack: Int

  def move(move: IMove): Try[IGame]

  def get(position: Int): IField

  def winner: Option[Player]

  def homeBoard: Map[Player, List[IField]]

  def numberOfPieces: Map[Player, Int]

  def bar: Map[Player, Int]

  def ==(that: IGame): Boolean

  def !=(that: IGame): Boolean

  override def asXml = {
    <game> 
        <barWhite>{barWhite}</barWhite>
        <barBlack>{barBlack}</barBlack>
        <fields>
        {fields.map(value => <field>{value.pieces}</field>)}
        </fields>
    </game>
  }
}

object IGame {
  implicit val gameReads: Reads[IGame] = Game.gameReads.map(identity[IGame])
  implicit val gameWrites: Writes[IGame] = (game: IGame) =>
    Game.gameWrites.writes(game.asInstanceOf[Game])
}
