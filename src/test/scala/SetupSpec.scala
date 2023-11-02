import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import scala.util.hashing.Hashing.Default

class SetupSpec extends AnyWordSpec {
  "Setup" should {
    "not be allowed to define odd number of fields" in {
      an[AssertionError] should be thrownBy DefaultSetup(15, 4)
    }

    "not be allowed to define smaller number than 12 fields" in {
      an[AssertionError] should be thrownBy DefaultSetup(8, 4)
    }

    "calculate default layout as map: '0 -> 5, 6 -> -5, 4 -> -3, 11 -> 2' " in {
      DefaultSetup(24, 15).toMap should equal(
        Map(
          0 -> 5,
          6 -> -5,
          4 -> -3,
          11 -> 2
        )
      )
    }

    "be a map" in {
      DefaultSetup(24, 15).toMap shouldBe a[Map[Int, Int]]
    }

  }
}
