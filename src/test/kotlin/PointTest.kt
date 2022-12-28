import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import legacy.Point

class PointTest : StringSpec({
    "a new point should return its coordinates" {
        val point = Point(1f, 2f)

        point.x shouldBe 1
        point.y shouldBe 2
    }

    "setLocation should update the point coordinates" {
        val point = Point(0f, 0f)
        point.setLocation(java.awt.Point(4, 2))

        point.x shouldBe 4
        point.y shouldBe 2
    }
})
