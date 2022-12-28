/*
 * Автор: Евгени Цаков
 */

/*
 * компонент, за рисуване на изображения
 */
package legacy;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

class ImageBox extends Component {

    BufferedImage image;
    private boolean scaled;

    public ImageBox(BufferedImage image) {
        this(image, false);
    }

    public ImageBox(BufferedImage image, boolean scaled) {
        this.image = image;
        this.scaled = scaled;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (image != null) {
            if (!scaled) {
                g.drawImage(image, 0, 0, null);
            } else {
                g.drawImage(image, 0, 0, image.getWidth() * 200 / image.getHeight(), 200, null);
                g.drawRect(0, 0, image.getWidth() * 200 / image.getHeight()-1, 200);
            }
        }
        else if(scaled){
            g.drawRect(0, 0, 100, 100);
            g.drawString("Load an image.", 10 , 50);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (image == null) {
            return new Dimension(200, 200);
        } else if (scaled) {
            return new Dimension(image.getWidth() * 200 / image.getHeight(), 200);
        } else {
            return new Dimension(image.getWidth(), image.getHeight());
        }
    }
}
