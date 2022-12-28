/*
 * Автор: Евгени Цаков
 */

/*
 * компонент, който съдържа крива на Безие
 */
package legacy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

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

    public void addMouseListeners(JFrame mainFrame, ImageBox originalImageBox, ImageBox distortedImageBox) {
        this.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {
                mainFrame.repaint();
                mainFrame.pack();
            }

            public void mousePressed(MouseEvent e) {
                for (Point p : curve.getControlPoints()) {
                    if (p.toAwtPoint().distance(e.getPoint()) <= 5) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            current = p;
                        } else {
                            curve.getControlPoints().remove(p);
                            repaint();
                        }
                        if (originalImageBox.image != null && curve.ensure()) {
                            ImageDistorter.distort(originalImageBox.image, distortedImageBox, curve.getEvaluatedCurvePoints());
                            //ImageDistorter.blur(distortedImageBox.image);
                            distortedImageBox.repaint();
                            mainFrame.pack();
                        }
                        return;
                    }
                }

                curve.getControlPoints().add(new Point(e.getPoint()));
                repaint();
                if (originalImageBox.image != null && curve.ensure()) {
                    ImageDistorter.distort(originalImageBox.image, distortedImageBox, curve.getEvaluatedCurvePoints());
                    //ImageDistorter.blur(distortedImageBox.image);
                    distortedImageBox.repaint();
                    mainFrame.pack();
                }
            }

            public void mouseReleased(MouseEvent e) {
                current = null;
                mainFrame.repaint();
                mainFrame.pack();
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
                current = null;
            }
        });

        this.addMouseMotionListener(new MouseMotionListener() {

            public void mouseDragged(MouseEvent e) {
                if (current != null) {
                    current.setLocation(e.getPoint());
                    repaint();

                    if (originalImageBox.image != null && curve.ensure()) {
                        ImageDistorter.distort(originalImageBox.image, distortedImageBox, curve.getEvaluatedCurvePoints());
                        //ImageDistorter.blur(distortedImageBox.image);
                        distortedImageBox.repaint();
                        mainFrame.pack();
                    }
                }
            }

            public void mouseMoved(MouseEvent e) {
            }
        });
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
