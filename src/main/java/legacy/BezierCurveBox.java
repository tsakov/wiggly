/*
 * Автор: Евгени Цаков
 */

/*
 * компонент, който съдържа крива на Безие
 */
package legacy;

import java.awt.*;

class BezierCurveBox extends Component {

    private static final int CONTROL_POINT_RADIUS = 5;

    Point current = null;
    BezierCurve curve = new BezierCurve();

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (curve.getControlPoints().isEmpty()) {
            return;
        }

        curve.evaluate();

        paintControlLines(g);
        paintCurve(g);
        paintControlPoints(g);

        if (!curve.ensure()) {
            paintWarning(g);
        }
    }

    private void paintControlPoints(Graphics g) {
        g.setColor(Color.BLACK);

        for (Point p : curve.getControlPoints()) {
            g.fillOval(
                (int) p.x - CONTROL_POINT_RADIUS,
                (int) p.y - CONTROL_POINT_RADIUS,
                CONTROL_POINT_RADIUS * 2,
                CONTROL_POINT_RADIUS * 2
            );
        }
    }

    private void paintControlLines(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);

        Point p1 = curve.getControlPoints().get(0), p2;
        for (int i = 1; i < curve.getControlPoints().size(); i++) {
            p2 = curve.getControlPoints().get(i);
            g.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
            p1 = p2;
        }
    }

    private void paintCurve(Graphics g) {
        if (!curve.getEvaluatedCurvePoints().isEmpty()) {
            g.setColor(Color.RED);
            Point p1, p2;
            p1 = curve.getControlPoints().get(0);

            for (int i = 1; i < curve.getEvaluatedCurvePoints().size(); i++) {
                p2 = curve.getEvaluatedCurvePoints().get(i);
                g.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
                p1 = p2;
            }
        }
    }

    private static void paintWarning(Graphics g) {
        g.setColor(Color.RED);
        g.drawString("BAD CURVE!", 5, 15);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 300);
    }
}
