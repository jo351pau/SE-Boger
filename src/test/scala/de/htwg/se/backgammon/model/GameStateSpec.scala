package de.htwg.se.backgammon.model
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.backgammon.model.base.Field
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.backgammon.model.base.Move
import de.htwg.se.backgammon.model.base.Game

class GameStateSpec extends AnyWordSpec {
  "GameState" should {
    "invalid" in {
      val state = GameState.invalid
      state.isValid shouldBe false

      GameState(new Game(24, 15), Move(0, 5)).isValid shouldBe true
    }
  }
}
