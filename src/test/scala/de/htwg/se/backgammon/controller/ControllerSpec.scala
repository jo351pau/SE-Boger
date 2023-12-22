package de.htwg.se.backgammon.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.backgammon.model.base.Move
import de.htwg.se.backgammon.model.Player
import base.Controller
import de.htwg.se.backgammon.model.base.Model
import de.htwg.se.backgammon.model.base.Game
import de.htwg.se.backgammon.model.base.CustomSetup
import de.htwg.se.backgammon.model.DiceStub
import de.htwg.se.backgammon.model.base.BearInMove
import de.htwg.se.backgammon.model.base.BearOffMove

class ControllerSpec extends AnyWordSpec {
  "Controller" should {
    "that it init right" in {
      val c = controller
      c.dice.length shouldBe 2
      val previousGame = c.game
      c.doAndPublish(c.put, Move(0, 1))
      c.previousGame shouldBe previousGame
      c.die shouldBe 2

      c.doAndPublish(c.put, Move(0, 5))
      c.doAndPublish(c.put, Move(0, 2))
      c.currentPlayer shouldBe Player.Black

    }

    "a whole game should work " in {
      val controller = Controller(m)

      // White moves (1,2)
      controller.doAndPublish(controller.put, Move(0, 1))
      controller.game.fields shouldBe List(1, 1, 0, -1, 1, 0, 0, -2)

      controller.doAndPublish(controller.put, Move(0, 5)) // nicht gültig
      controller.doAndPublish(controller.put, Move(0, 2))
      controller.game.fields shouldBe List(0, 1, 1, -1, 1, 0, 0, -2)

      // Black moves(5,5)
      controller.doAndPublish(controller.put, Move(7, 5))
      controller.doAndPublish(controller.put, Move(7, 5))
      controller.game.fields shouldBe List(0, 1, -2, -1, 1, 0, 0, 0)
      controller.game.barWhite shouldBe 1

      // White moves(3,2)
      controller.doAndPublish(controller.put, BearInMove(Player.White, 2))
      controller.game.barWhite shouldBe 0
      controller.skip(3)
      controller.game.fields shouldBe List(0, 2, -2, -1, 1, 0, 0, 0)

      // Black moves (3,2)
      controller.doAndPublish(controller.put, Move(3, 3))
      controller.game.fields shouldBe List(-1, 2, -2, 0, 1, 0, 0, 0)
      controller.doAndPublish(controller.put, Move(2, 2))
      controller.game.fields shouldBe List(-2, 2, -1, 0, 1, 0, 0, 0)

      // White moves(4,4)
      controller.doAndPublish(controller.put, Move(1, 4))
      controller.game.fields shouldBe List(-2, 1, -1, 0, 1, 1, 0, 0)
      controller.doAndPublish(controller.redo)
      controller.game.fields shouldBe List(-2, 0, -1, 0, 1, 2, 0, 0)

      // Black moves(1,2)
      controller.doAndPublish(controller.put, Move(2, 1))
      controller.game.fields shouldBe List(-2, -1, 0, 0, 1, 2, 0, 0)
      controller.doAndPublish(controller.put, BearOffMove(1, 2))

      // White moves(3,5)
      controller.doAndPublish(controller.put, Move(2, 1))
      controller.undoAndPublish(controller.undo)
    }
  }

  private def m = {
    Model(
      new Game(CustomSetup(List(2, 0, 0, -1))),
      DiceStub(1, 2, 5, 5, 3, 2, 3, 2, 4, 4, 1, 2, 3, 5)
    )
  }

  private def controller = Controller(m)

}
