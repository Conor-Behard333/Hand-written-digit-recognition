package ProcessingData;

import NeuralNetwork.Function;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageConverter {
    private BufferedImage image;
    private Function f = new Function();

    private BufferedImage getCenteredImage() {
        BufferedImage result = new BufferedImage(28, 28, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = result.createGraphics();
        g2.setColor(Color.black);
        g2.fillRect(0, 0, result.getWidth(), result.getHeight());
        int[] centerOfMass = getCenterOfMass(image);
        g2.drawImage(image, 28 / 2 - centerOfMass[0], 28 / 2 - centerOfMass[1], null);
        g2.dispose();
        return result;

    }

    private int[] getCenterOfMass(BufferedImage image) {
        long xSum = 0;
        long ySum = 0;
        long n = 0;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int weight = new Color(image.getRGB(x, y)).getRed();
                xSum += x * weight;
                ySum += y * weight;
                n += weight;
            }
        }
        return new int[]{(int) ((double) xSum / n), (int) ((double) ySum / n)};
    }

    public double[] getInput() {
        double[] input = new double[784];
        try {
            int i = 0;
            image = ImageIO.read(new File("C:\\Users\\conor\\OneDrive\\My Documents\\image.png"));
            image = getScaledImage();
            image = getCenteredImage();
            try {
                ImageIO.write(image, "png", new File("C:\\Users\\conor\\OneDrive\\My Documents\\imageTest.png"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    input[i] = getLuminance(x, y);
                    i++;
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        input = f.normalise(input);
        return input;
    }

    private BufferedImage getScaledImage() {
        Image scaledImg = image.getScaledInstance(20, 20, image.SCALE_SMOOTH);
        BufferedImage img = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.drawImage(scaledImg, 0, 0, null);
        g2.dispose();
        return img;
    }

    private double getLuminance(int x, int y) {
        int color = image.getRGB(x, y);

        double red = (color >>> 16) & 0xFF;
        double green = (color >>> 8) & 0xFF;
        double blue = (color) & 0xFF;

        return (red * 0.2126d + green * 0.7152d + blue * 0.0722d);
    }

}
