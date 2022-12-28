/*
 * Автор: Евгени Цаков
 */

/*
 * основният клас на програмата
 */
package legacy;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    private JFileChooser imageFileChooser = new JFileChooser("D:\\Coding\\Java\\OKG");
    private BezierCurveBox curveBox = new BezierCurveBox();
    private ImageBox originalImageBox = new ImageBox(null, true);
    private ImageBox distortedImageBox = new ImageBox(null);

    public void draw() {
        final JFrame mainFrame = new JFrame("Distortion Mirror");
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        JPanel imagesPanel = new JPanel(new BorderLayout(10, 10));

        JButton helpButton = new JButton("Help");
        JButton openButton = new JButton("Open Image");
        JButton saveButton = new JButton("Save Image");
        JButton exitButton = new JButton("Exit");
        JButton aboutButton = new JButton("About");

        openButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                imageFileChooser.showOpenDialog(null);
                File chosen = imageFileChooser.getSelectedFile();
                if (chosen != null) {
                    try {
                        originalImageBox.image = ImageIO.read(chosen);
                        originalImageBox.repaint();
                        distortedImageBox.image = new BufferedImage(originalImageBox.image.getWidth(), originalImageBox.image.getHeight(), BufferedImage.TYPE_INT_RGB);
                        if (!curveBox.curve.getEvaluatedCurvePoints().isEmpty()) {
                            ImageDistorter.distort(originalImageBox.image, distortedImageBox, curveBox.curve.getEvaluatedCurvePoints());
                        }

                        mainFrame.repaint();
                        mainFrame.pack();
                    } catch (Exception ex) {
                        originalImageBox.image = null;
                        JOptionPane.showMessageDialog(mainFrame, "Error opening file!", "Error!", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (originalImageBox.image == null) {
                    return;
                }
                imageFileChooser.showSaveDialog(null);
                File chosen = imageFileChooser.getSelectedFile();
                try {
                    if (ImageIO.write(distortedImageBox.image, "jpg", chosen)) {
                        JOptionPane.showMessageDialog(imageFileChooser, "Image saved!", "Done!", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(imageFileChooser, "Error saving image!", "Error!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        exitButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(imageFileChooser, "Do you really want to exit?", "Exit", JOptionPane.YES_NO_OPTION)
                    == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        aboutButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainFrame, "Тази програма бе създадена от Евгени Цаков, "
                    + "не защото изгаряше от желание\nда я напише, а защото му бе възложена като курсов проект.\n"
                    + "P.S. Всички права запазени \u00ae", "За програмата...", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        helpButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainFrame, "Точките могат да се местят с левия бутон на мишката\n"
                    + "и да се трият с десния. Нови точки се добавят\n"
                    + "с левия бутон.", "Помощ!", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        buttonsPanel.add(helpButton);
        buttonsPanel.add(openButton);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(aboutButton);
        buttonsPanel.add(exitButton);
        imagesPanel.add(originalImageBox, BorderLayout.LINE_START);
        imagesPanel.add(distortedImageBox, BorderLayout.LINE_END);
        mainPanel.add(buttonsPanel, BorderLayout.PAGE_START);
        mainPanel.add(imagesPanel, BorderLayout.PAGE_END);
        mainFrame.add(mainPanel);

        mainFrame.pack();


        JFrame curveFrame = new JFrame("Curve");
        curveFrame.setSize(300, 300);
        curveFrame.setVisible(true);
        curveFrame.setLocation(mainFrame.getWidth() + 20, 0);
        //curveFrame.setResizable(false);
        //curveFrame.setAlwaysOnTop(true);
        curveFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        curveFrame.add(curveBox);

        curveBox.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {
                mainFrame.repaint();
                mainFrame.pack();
            }

            public void mousePressed(MouseEvent e) {
                for (Point p : curveBox.curve.getControlPoints()) {
                    if (p.toAwtPoint().distance(e.getPoint()) <= 5) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            curveBox.current = p;
                        } else {
                            curveBox.curve.getControlPoints().remove(p);
                            curveBox.repaint();
                        }
                        if (originalImageBox.image != null && curveBox.curve.ensure()) {
                            ImageDistorter.distort(originalImageBox.image, distortedImageBox, curveBox.curve.getEvaluatedCurvePoints());
                            //ImageDistorter.blur(distortedImageBox.image);
                            distortedImageBox.repaint();
                            mainFrame.pack();
                        }
                        return;
                    }
                }

                curveBox.curve.getControlPoints().add(new Point(e.getPoint()));
                curveBox.repaint();
                if (originalImageBox.image != null && curveBox.curve.ensure()) {
                    ImageDistorter.distort(originalImageBox.image, distortedImageBox, curveBox.curve.getEvaluatedCurvePoints());
                    //ImageDistorter.blur(distortedImageBox.image);
                    distortedImageBox.repaint();
                    mainFrame.pack();
                }
            }

            public void mouseReleased(MouseEvent e) {
                curveBox.current = null;
                mainFrame.repaint();
                mainFrame.pack();
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
                curveBox.current = null;
            }
        });


        curveBox.addMouseMotionListener(new MouseMotionListener() {

            public void mouseDragged(MouseEvent e) {
                if (curveBox.current != null) {
                    curveBox.current.setLocation(e.getPoint());
                    curveBox.repaint();

                    if (originalImageBox.image != null && curveBox.curve.ensure()) {
                        ImageDistorter.distort(originalImageBox.image, distortedImageBox, curveBox.curve.getEvaluatedCurvePoints());
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

    public static void main(String[] args) throws IOException {
        new Main().draw();
    }
}
