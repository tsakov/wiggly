/*
 * Автор: Евгени Цаков
 */

/*
 * компонент, който съдържа крива на Безие
 */
package legacy;

import java.awt.Component;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

class BezierCurveBox extends Component {

    Point current = null;
    BezierCurve curve = new BezierCurve();

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (curve.controlPoints.isEmpty()) {
            return;
        }

        curve.evaluate();

        Point p1 = curve.controlPoints.get(0), p2;

        g.setColor(Color.lightGray);
        for (int i = 1; i < curve.controlPoints.size(); i++) {
            p2 = curve.controlPoints.get(i);
            g.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
            p1 = p2;
        }

        if (!curve.evaluatedCurvePoints.isEmpty()) {
            g.setColor(Color.red);
            p1 = curve.controlPoints.get(0);

            for (int i = 1; i < curve.evaluatedCurvePoints.size(); i++) {
                p2 = curve.evaluatedCurvePoints.get(i);
                g.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
                p1 = p2;
            }
        }

        g.setColor(Color.black);
        for(Point p : curve.controlPoints)
            g.fillOval((int) p.x - 5, (int) p.y - 5, 10, 10);

        if (!curve.ensure()) {
            g.setColor(Color.RED);
            g.drawString("BAD CURVE!", 5, 15);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 300);
    }
}
