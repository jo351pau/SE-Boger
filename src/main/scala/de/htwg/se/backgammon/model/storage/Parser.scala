package de.htwg.se.backgammon.model.storage

import scala.collection.mutable.Map
import scala.xml.Elem
import de.htwg.se.backgammon.model.base.Game
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import scala.reflect.ClassTag
import de.htwg.se.backgammon.model.IGame
import de.htwg.se.backgammon.model.IModel
import de.htwg.se.backgammon.model.base.Model
import scala.xml.Node

case class Parser[T <: Storable](
    fromXml: Node => T = ???,
    fromJson: JsValue => T = ???,
    toJson: Storable => JsValue = ???
)

object Parser {
  private val parser: Map[String, Parser[_ <: Storable]] = Map()

  def register[T <: Storable: ClassTag](
      parser: Parser[T]
  ): Unit = {
    val className = implicitly[ClassTag[T]].runtimeClass.getSimpleName
    this.parser(className) = parser
  }

  def initialize() = {
    register[IGame](
      new Parser(
        fromXml = Game.fromXml,
        fromJson = Game.fromJson,
        toJson = (game: Storable) => {
          Json.toJson(game.asInstanceOf[Game])
        }
      )
    )
    register[IModel](
      new Parser(
        fromXml = null,
        fromJson = Model.fromJson,
        toJson = (model: Storable) => {
          Json.toJson(model.asInstanceOf[Model])
        }
      )
    )
  }

  def get(name: String) = parser.get(name)

  def isDefined(name: String) = parser.get(name).isDefined
}
