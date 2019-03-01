import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class ImageConverter {
    private BufferedImage image;
    private Function f = new Function();

    double[] getInput() {
        double[] input = new double[784];
        try {
            int i = 0;
            image = ImageIO.read(new File("C:\\Users\\conor\\OneDrive\\My Documents\\image.png"));
            scale();
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    input[i] = getLuminance(x, y);
                    i++;
                }
            }
            input = f.normalise(input);
        } catch (IOException e) {
            System.out.println(e);
        }
        return input;
    }

    private void scale() {
        Image scaledImg = image.getScaledInstance(28, 28, image.SCALE_SMOOTH);
        image = new BufferedImage(28, 28, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.drawImage(scaledImg, 0, 0, null);
        g2.dispose();
    }

    private double getLuminance(int x, int y) {
        int color = image.getRGB(x, y);

        double red = (color >>> 16) & 0xFF;
        double green = (color >>> 8) & 0xFF;
        double blue = (color) & 0xFF;

        return (red * 0.2126d + green * 0.7152d + blue * 0.0722d);
    }


}
