package de.htwg.se.backgammon.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class GameSpec extends AnyWordSpec {
  "Game" should {
    "return value between between 1 and 6" in {
      Dice.roll should be > 0
      Dice.roll should be < 7
    }
    "have default layout '|5| |/| |/| |/| |-3| |/| |-5| |/| |/| |/| |/| |2| " +
      "|-2| |/| |/| |/| |/| |5| |/| |3| |/| |/| |/| |-5|' " in {
        Game(24, 15).fields should equal(
          List(5, 0, 0, 0, -3, 0, -5, 0, 0, 0, 0, 2, -2, 0, 0, 0, 0, 5, 0, 3, 0,
            0, 0, -5).map(i => Field(i))
        )
      }
    "have default layout" in {
      Game.create(DefaultSetup(24, 15)) should equal(
        List(5, 0, 0, 0, -3, 0, -5, 0, 0, 0, 0, 2, -2, 0, 0, 0, 0, 5, 0, 3, 0,
          0, 0, -5).map(i => Field(i))
      )
    }
    "have custom layout" in {
      Game(CustomSetup(List(4, 0, 0, -2))).fields should equal(
        List(4, 0, 0, -2, 2, 0, 0, -4).map(i => Field(i))
      )
    }
    "return winner empty" in {
      Game(24, 15).winner shouldBe Option.empty
    }

    "look like |5| |/| |/| |-2| |2| |/| |/| |-5|" in {
      Game(
        CustomSetup(List(5, 0, 0, -2))
      ).toString shouldBe "0 : |5| |/| |/| |-2| |2| |/| |/| |-5| : 0"
    }
    "be |3|, |-3|, |/|, |-5|, |2|, |2|, |-2|, |/|, |5|, |/|, |3|, |-5| when moved a piece from 0 to 4" in {
      Game(12, 15).move(0, 4).get.fields should equal(
        List(4, -3, 0, -5, 1, 2, -2, 0, 5, 0, 3, -5).map(i => Field(i))
      )
    }
    "attacking opponent, send checker to bar and back" in {
      var game = Game(CustomSetup(List(5, 0, 0, -2)))
      game = game.move(0, 1).get.move(3, 2).get
      game.fields should equal(
        List(4, -1, 0, -1, 2, 0, 0, -5).map(i => Field(i))
      )
      game.barWhite shouldBe 1

      game = game.move(Player.White, 1).get
      game.fields should equal(
        List(5, -1, 0, -1, 2, 0, 0, -5).map(i => Field(i))
      )
      game.barWhite shouldBe 0

      game = game.move(0, 1).get
      game.fields should equal(
        List(4, 1, 0, -1, 2, 0, 0, -5).map(i => Field(i))
      )
      game.barBlack shouldBe 1

      game = game.move(Player.Black, 7).get
      game.fields should equal(
        List(4, -1, 0, -1, 2, 0, 0, -5).map(i => Field(i))
      )
      game.barWhite shouldBe 1
      game != Game(16, 24) shouldBe true
      game == game shouldBe true

      game.move(2, 1).failed.get.getMessage() shouldBe EmptyFieldException(2)
        .getMessage()
      game
        .move(3, 3)
        .failed
        .get
        .getMessage() shouldBe AttackNotPossibleException(3, 0, 4).getMessage()
      game
        .move(3, 15)
        .failed
        .get
        .getMessage() shouldBe FieldDoesNotExistException(3, 15, 3 - 15)
        .getMessage()
      DieNotExistException(2, List(1, 3)).getMessage()
      WrongDirectionException(Player.White).getMessage()
      NotYourFieldException(1, Player.White, Player.Black).getMessage()
    }
  }
}
