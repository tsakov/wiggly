/*
 * Автор: Евгени Цаков
 */

/*
 * клас, който предоставя метод за
 * разкривяване на изображение
 */
package legacy;

import java.awt.image.BufferedImage;
import java.util.List;

public class ImageDistorter {

    private static int getRGB(int x, int y, BufferedImage img) {
        if (x < 0 || x >= img.getWidth() || y < 0 || y >= img.getHeight()) {
            return 0xeeeeee;    // light grey
        }
        return img.getRGB(x, y);
    }

    // разкривява изображение по крива на Безие
    static void distort(BufferedImage src, ImageBox dest, List<Point> evaluatedCurvePoints) {
        if (evaluatedCurvePoints.isEmpty()) {
            return;
        }

        Point p1, p2 = evaluatedCurvePoints.get(0);
        double up = evaluatedCurvePoints.get(0).y; // горна граница на кривата
        double heightRatio = src.getHeight() / (evaluatedCurvePoints.get(evaluatedCurvePoints.size() - 1).y - up);

        double right;   // лява и дясна граници на кривата
        double left = right = evaluatedCurvePoints.get(0).x;

        for (Point p : evaluatedCurvePoints) {
            if (left > p.x) {
                left = p.x;
            } else if (right < p.x) {
                right = p.x;
            }
        }

        dest.image = new BufferedImage((int) (src.getWidth() + (right - left) * heightRatio), src.getHeight(), src.getType());

        for (int i = 1; i < evaluatedCurvePoints.size(); i++) {
            p1 = p2;
            p2 = evaluatedCurvePoints.get(i);
            int offset = (int) ((p2.x - left) * heightRatio);
            int[] offsets = getIndividualOffsets((int) ((p1.x - left) * heightRatio), (int) ((p1.y - up) * heightRatio), (int) ((p2.x - left) * heightRatio), (int) ((p2.y - up) * heightRatio));
            int k = 0;

            for (int y = (int) ((p1.y - up) * heightRatio); y < (p2.y - up) * heightRatio && y < dest.image.getHeight(); y++) {
                for (int x = 0; x < dest.image.getWidth(); x++) {
                    dest.image.setRGB(x, y, getRGB(x - offset - offsets[k], y, src));
                }
                k++;
            }
        }
    }

    // Brezenham's line
    static int[] getIndividualOffsets(int x0, int y0, int x1, int y1) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);

        int[] offsets = new int[dy + 1];
        int i = 0;

        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        double err = dx - dy, e2;

        int x0_old = x0;
        offsets[i++] = 0;

        while (x0 != x1 || y0 != y1) {
            e2 = 2 * err;
            if (e2 > -dy) {
                err = err - dy;
                x0 = x0 + sx;
            }
            if (e2 < dx) {
                err = err + dx;
                y0 = y0 + sy;
                offsets[i++] = x0 - x0_old;
            }
        }

        return offsets;
    }

    // размазващ филтър, който използва маска 3х3 със средно аритметично
    static void blur(BufferedImage img) {
        BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        int r, g, b, imgRGB, resRGB;

        for (int i = 1; i < img.getWidth() - 1; i++) {
            for (int j = 1; j < img.getHeight() - 1; j++) {
                r = g = b = 0;
                for (int k = i - 1; k < i + 2; k++) {
                    for (int l = j - 1; l < j + 2; l++) {
                        imgRGB = img.getRGB(k, l);
                        r += (imgRGB >> 16) & 0xff;
                        g += (imgRGB >> 8) & 0xff;
                        b += (imgRGB) & 0xff;
                    }

                }
                resRGB = ((r / 9) << 16) + ((g / 9) << 8) + b / 9;
                res.setRGB(i, j, resRGB);
            }
        }

        for (int x = 1; x < res.getWidth() - 1; x++) {
            for (int y = 1; y < res.getHeight() - 1; y++) {
                img.setRGB(x, y, res.getRGB(x, y));
            }
        }
    }
}
