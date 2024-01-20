package de.htwg.se.backgammon.model.storage

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.backgammon.model.base.Game
import de.htwg.se.backgammon.model.base.Model
import de.htwg.se.backgammon.model.base.Dice
import de.htwg.se.backgammon.model.IModel
import scala.util.Success
import de.htwg.se.backgammon.model.storage.XmlStorage.{given}
import de.htwg.se.backgammon.model.storage.JsonStorage.{given}
import de.htwg.se.backgammon.model.IGame
import de.htwg.se.backgammon.model.IField
import de.htwg.se.backgammon.model.base.Field

class StorageSpec extends AnyWordSpec {
    "XmlStorage" should {
        "save Model the same as loading (Xml)" in {
            var storage: Storage = XmlStorage.given_Storage
            val model = new Model(new Game(24, 15), Dice())
            storage.save[IModel](model, "test")

            val loaded = storage.load[IModel]("test")
            val model_ = loaded.get
            model.dice shouldBe model_.dice
            model.doublets shouldBe model_.doublets
            model.player shouldBe model_.player
        }
        "save Model the same as loading (Json)" in {
            var storage: Storage = JsonStorage.given_Storage
            val model = new Model(new Game(24, 15), Dice())
            storage.save[IModel](model, "test")

            val loaded = storage.load[IModel]("test")
            val model_ = loaded.get
            model.dice shouldBe model_.dice
            model.doublets shouldBe model_.doublets
            model.player shouldBe model_.player
        }
        "save Game the same as loading (Xml)" in {
            var storage: Storage = XmlStorage.given_Storage
            val game = new Game(24, 15)
            storage.save[IGame](game, "test_game")

            val loaded = storage.load[IGame]("test_game")
            val game_ = loaded.get
            game.barBlack shouldBe game_.barBlack
            game.barWhite shouldBe game_.barWhite
            game.fields shouldBe game_.fields
        }
        "save Game the same as loading (Json)" in {
            var storage: Storage = JsonStorage.given_Storage
            val game = new Game(24, 15)
            storage.save[IGame](game, "test_game")

            val loaded = storage.load[IGame]("test_game")
            val game_ = loaded.get
            game.barBlack shouldBe game_.barBlack
            game.barWhite shouldBe game_.barWhite
            game.fields shouldBe game_.fields
        }
        "save Game without explicit defining class (Json)" in {
            var storage: Storage = JsonStorage.given_Storage
            val game: IGame = new Game(24, 15)
            storage.save(game, "test_game")
        }
    }
}
