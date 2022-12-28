/*
 * Автор: Евгени Цаков
 */


package legacy;

import java.util.ArrayList;
import java.util.List;

public class BezierCurve {

    private List<Point> controlPoints = new ArrayList<Point>();
    private List<Point> evaluatedCurvePoints = new ArrayList<Point>();

    public List<Point> getControlPoints() {
        return controlPoints;
    }

    public void setControlPoints(List<Point> controlPoints) {
        this.controlPoints = controlPoints;
    }

    public List<Point> getEvaluatedCurvePoints() {
        return evaluatedCurvePoints;
    }

    public void evaluate() {
        evaluatedCurvePoints.clear();
        Point[] points = new Point[4];

        for (int i = 0; i < controlPoints.size() - 3; i += 3) {
            controlPoints.subList(i, i + 4).toArray(points);
            for (float t = 0; t <= 1; t += 0.01) {
                evaluatedCurvePoints.add(BezierCurveEvaluator.evaluate(points, t));
            }
        }
    }

    // проверка дали кривата е монотонна по y
    public boolean ensure() {
        for (int i = 0; i < evaluatedCurvePoints.size() - 1; i++) {
            if (evaluatedCurvePoints.get(i).y >= evaluatedCurvePoints.get(i+1).y) {
                return false;
            }
        }

        return true;
    }
}
