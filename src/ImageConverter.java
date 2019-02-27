import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ImageConverter {
    static BufferedImage image;

    public static void main(String[] args) {
        double[] input = {};
        try {
            image = ImageIO.read(new File("C:\\Users\\conor\\OneDrive\\My Documents\\image.png"));
            scale();
            input = new double[image.getHeight() * image.getWidth()];
            for (int i = 0; i < input.length; i++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    for (int x = 0; x < image.getWidth(); x++) {
                        input[i] = getLuminance(x, y);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        System.out.println(Arrays.toString(input));
    }

    private static void scale() {
        Image scaledImg = image.getScaledInstance(28, 28, image.SCALE_SMOOTH);
        image = new BufferedImage(28, 28, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.drawImage(scaledImg, 0, 0, null);
        g2.dispose();
    }

    private static double getLuminance(int x, int y) {
        int color = image.getRGB(x, y);

        int red = (color >>> 16) & 0xFF;
        int green = (color >>> 8) & 0xFF;
        int blue = (color >>> 0) & 0xFF;

        double luminance = (red * 0.2126d + green * 0.7152d + blue * 0.0722d);
        return luminance;
    }
}
