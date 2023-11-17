package de.htwg.se.backgammon.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class ModelSpec extends AnyWordSpec {
  "Model" should {
    "that it's the next player's turn" in {
      model.next shouldBe Player.Black
    }
    "that it's the next player's turn" in {
      val t = model;
      val game2 = new Game(24, 15)
      t.game = game2
      val game3 = new Game(24, 15)
      t.game = game3
      t.previousGame shouldBe game2
    }
  }
  private def model = Model(new Game(24, 15), Player.White)
}
