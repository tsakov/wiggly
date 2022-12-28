import io.kotest.core.spec.style.StringSpec
import io.kotest.inspectors.shouldForAll
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import legacy.BezierCurve
import legacy.Point
import java.math.RoundingMode
import java.text.DecimalFormat

class BezierCurveTest : StringSpec({
    "empty curve can be evaluated" {
        val curve = BezierCurve()
        curve.evaluate()

        curve.evaluatedCurvePoints.shouldBeEmpty()
    }

    "control points with the same X evaluate to a vertical straight line" {
        val curve = BezierCurve()
        curve.controlPoints = listOf(
            Point(100, 50),
            Point(100, 60),
            Point(100, 80),
            Point(100, 100)
        )
        curve.evaluate()

        val decimalFormat = DecimalFormat("#.##").also { it.roundingMode = RoundingMode.HALF_UP }

        curve.evaluatedCurvePoints.shouldForAll {
            decimalFormat.format(it.x).toFloat() shouldBe 100
        }
    }

    "ensure returns false for a non-monotone curve" {
        val curve = BezierCurve()
        curve.controlPoints = listOf(
            Point(0, 0),
            Point(0, 80),
            Point(0, 100),
            Point(0, 30)
        )
        curve.evaluate()

        curve.ensure().shouldBeFalse()
    }
})
