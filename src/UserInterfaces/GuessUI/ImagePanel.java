package UserInterfaces.GuessUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ImagePanel extends JPanel {
    private Image img;

    /*
     * reads an image and stores it as an image
     */
    ImagePanel(String FilePath) {
        try {
            img = ImageIO.read(new File(FilePath));
        } catch (Exception error) {
            System.err.println(error);
        }
    }

    /*
     * draws the image initialised by the constructor
     */
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.black);
        g.fillRect(30, 30, 400, 470);
        g.drawImage(img, 465, 30, null);
    }
}
