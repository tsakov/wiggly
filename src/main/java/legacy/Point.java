/*
 * Автор: Евгени Цаков
 */

/*
 * клас Point, но за разлика от java.awt.Point този е с координати от тип float,
 * за по-голяма прецизност при изчисляване на кривите на Безие
 */

package legacy;


public class Point {

    public float x;
    public float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(java.awt.Point p) {
        x = p.x;
        y = p.y;
    }

    public void setLocation(java.awt.Point p) {
        x = p.x;
        y = p.y;
    }

    public java.awt.Point toAwtPoint() {
        return new java.awt.Point((int) x, (int) y);
    }
}
