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

    public double[] getInput() {
        double[] input = new double[784];
        int i = 0;
        try {
            image = ImageIO.read(new File("C:\\Users\\conor\\OneDrive\\My Documents\\image.png"));//reads the image the user drew
            image = getScaledImage();//scales the image to a 20 by 20 png
            image = getCenteredImage();//centres the image onto a 28 by 28 image
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    input[i] = getLuminance(x, y);
                    i++;
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
//        try {
//            ImageIO.write(image, "png", new File("C:\\Users\\conor\\OneDrive\\My Documents\\imageTest.png"));
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
        input = f.normalise(input);//normalises the input data
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

    private BufferedImage getCenteredImage() {
        BufferedImage result = new BufferedImage(28, 28, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = result.createGraphics();
        g2.setColor(Color.black);
        g2.fillRect(0, 0, result.getWidth(), result.getHeight());
        int[] centerOfMass = getCentreOfMass(image);
        g2.drawImage(image, 28 / 2 - centerOfMass[0], 28 / 2 - centerOfMass[1], null);
        g2.dispose();
        return result;
    }

    private int[] getCentreOfMass(BufferedImage image) {
        int[] centreOfMass = new int[2];
        double xSum = 0;
        double ySum = 0;
        long n = 0;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int weight = new Color(image.getRGB(x, y)).getRed();
                xSum += x * weight;
                ySum += y * weight;
                n += weight;
            }
        }
        centreOfMass[0] = (int) (xSum / n);
        centreOfMass[1] = (int) (ySum / n);
        return centreOfMass;
    }

    private double getLuminance(int x, int y) {
        int color = image.getRGB(x, y);

        double red = (color >>> 16) & 0xFF;
        double green = (color >>> 8) & 0xFF;
        double blue = (color) & 0xFF;

        return (red * 0.2126d + green * 0.7152d + blue * 0.0722d);
    }
}
