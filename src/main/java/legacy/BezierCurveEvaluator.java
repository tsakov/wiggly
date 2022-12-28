/*
 * Автор: Евгени Цаков
 */

/*
 * клас с методи за изчисляване на крива на Безие
 */
package legacy;

import java.util.Arrays;

public class BezierCurveEvaluator {

    // linear interpolation
    private static Point lerp(Point a, Point b, float t) {
        return new Point((1 - t) * a.x + t * b.x, (1 - t) * a.y + t * b.y);
    }

    public static Point evaluate(Point[] controlPoints, float t) {
        Point[] tmp = Arrays.copyOf(controlPoints, controlPoints.length);

        for (int j = tmp.length - 1; j > 0; j--) {
            for (int i = 0; i < j; i++) {
                tmp[i] = lerp(tmp[i], tmp[i + 1], t);
            }
        }

        return tmp[0];
    }
}
