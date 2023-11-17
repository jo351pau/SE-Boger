package de.htwg.se.backgammon.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.backgammon.model.Model
import de.htwg.se.backgammon.model.Game
import de.htwg.se.backgammon.model.Move
import de.htwg.se.backgammon.model.CustomSetup
import de.htwg.se.backgammon.model.Field

class ControllerSpec extends AnyWordSpec {
  "Controller" should {
    "that it init right" in {
      val c = controller
      c.dice.length shouldBe 2
      c.doAndPublish(c.put, Move(0, 1))
    }
  }

  private def controller = Controller(
    Model(new Game(CustomSetup(List(5, 0, 0, -2))))
  )
}
