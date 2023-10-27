import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class GameSpec extends AnyWordSpec {
  "Game" should {
    "return value between between 1 and 6" in {
      Game.rollDice() should be > 0
      Game.rollDice() should be < 7
    }
    "have default layout '|5| |/| |/| |/| |-3| |/| |-5| |/| |/| |/| |/| |2| " +
      "|-2| |/| |/| |/| |/| |5| |/| |3| |/| |/| |/| |-5|' " in {
        Game(24, 15).fields should equal(
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
        Game(24,15).winner shouldBe Option.empty
    }
  }
}
