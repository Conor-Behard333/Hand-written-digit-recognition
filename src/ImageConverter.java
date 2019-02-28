import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ImageConverter {
    static BufferedImage image;

    private static void scale() {
        Image scaledImg = image.getScaledInstance(28, 28, image.SCALE_SMOOTH);
        image = new BufferedImage(28, 28, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.drawImage(scaledImg, 0, 0, null);
        g2.dispose();
    }

    private static double getLuminance(int x, int y) {
        int color = image.getRGB(x, y);

        double red = (color >>> 16) & 0xFF;
        double green = (color >>> 8) & 0xFF;
        double blue = (color >>> 0) & 0xFF;

        double luminance = (red * 0.2126d + green * 0.7152d + blue * 0.0722d);
        return luminance;
    }

    public double[] getInput() {
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
        } catch (IOException e) {
            System.out.println(e);
        }
        input = normalise(input);
        return input;
    }

    double[] normalise(double[] x) {
        int min = 0;
        double max = getMax(x);
        double[] y = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            y[i] = (x[i] - min) / (max - min);
        }
        return y;
    }

    double getMax(double[] x) {
        double max = 0;
        for (int i = 0; i < x.length; i++) {
            if (x[i] > max) {
                max = x[i];
            }
        }
        return max;
    }
}
