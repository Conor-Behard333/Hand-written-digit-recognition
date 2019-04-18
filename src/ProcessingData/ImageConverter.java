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

    /*
     * Reads the image that the user drew and scales the image
     * to a 20 by 20 png, it then centres the image onto a 28 by 28 image
     * then it stores the brightness of each pixel into a double array
     * which is then normalised (put between the values of 0 and 1) and finally
     * returned to then be used to be put through the neural network
     */
    public double[] getInput() {
        double[] input = new double[784];
        int i = 0;
        try {
            image = ImageIO.read(new File("Resources\\image.png"));//reads the image the user drew
            image = getScaledImage();//scales the image to a 20 by 20 png
            image = getCenteredImage();//centres the image onto a 28 by 28 image
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    input[i] = getBrightness(x, y);//stores the brightness of each pixel into a double array
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

    /*
     * Scales the drawing into a 20 by 20 image by drawing the scaled image
     * into a buffered image and then returning it
     */
    private BufferedImage getScaledImage() {
        Image scaledImg = image.getScaledInstance(20, 20, image.SCALE_SMOOTH);
        BufferedImage img = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.drawImage(scaledImg, 0, 0, null);
        g2.dispose();
        return img;
    }

    /*
     * Converts the 20 by 20 image into a 28 by 28 image while also
     * centering the image by drawing the image onto a 28 by 28 image
     * and calculating the center of mass of the image to ensure that
     * the new image is centered
     */
    private BufferedImage getCenteredImage() {
        BufferedImage centeredImage = new BufferedImage(28, 28, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = centeredImage.createGraphics();
        g2.setColor(Color.black);
        g2.fillRect(0, 0, centeredImage.getWidth(), centeredImage.getHeight());
        int[] centerOfMass = getCenterOfMass(image);//calculates the center of mass
        g2.drawImage(image, 28 / 2 - centerOfMass[0], 28 / 2 - centerOfMass[1], null);
        g2.dispose();
        return centeredImage;
    }

    /*
     * Calculates the centre of mass by summing the multiplication of the pixels red value
     * and the x and y position of the pixel and then dividing by the sum of the red values
     *
     * weight = red value of pixel
     * center of mass (x value) = Σ(x * weight) / Σ weights
     * center of mass (y value) = Σ(y * weight) / Σ weights
     */
    private int[] getCenterOfMass(BufferedImage image) {
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

    /*
     * Returns the brightness of an individual pixel
     */
    private double getBrightness(int x, int y) {
        int color = image.getRGB(x, y);

        double red = (color >>> 16) & 0xFF;
        double green = (color >>> 8) & 0xFF;
        double blue = (color) & 0xFF;

        return (red * 0.2126d + green * 0.7152d + blue * 0.0722d);
    }
}
