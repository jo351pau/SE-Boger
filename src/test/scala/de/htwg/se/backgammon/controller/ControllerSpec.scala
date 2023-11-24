package de.htwg.se.backgammon.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.backgammon.model.Model
import de.htwg.se.backgammon.model.Game
import de.htwg.se.backgammon.model.Move
import de.htwg.se.backgammon.model.CustomSetup
import de.htwg.se.backgammon.model.Field
import de.htwg.se.backgammon.model.Player
import de.htwg.se.backgammon.model.strategy.game.Game

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
  }

  private def m = {
    var model = Model(new Game(CustomSetup(List(5, 0, 0, -2))))
    model.dice = List(1, 2); model
  }
  private def controller = Controller(m)

}
