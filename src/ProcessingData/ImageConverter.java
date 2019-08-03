package ProcessingData;

import NeuralNetwork.Function;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageConverter extends Function {
    private BufferedImage image;

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
            image = getScaledImage(20, 20);//scales the image to a 20 by 20 png
            image = getCenteredImage(28, 28);//centres the image onto a 28 by 28 image
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    input[i] = image.getRGB(x, y) & 0xff;//stores the brightness of each pixel into a double array
                    i++;
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        try {
            ImageIO.write(image, "png", new File("Resources\\ImageTest.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return normalise(input);//normalises the input data
    }

    /*
     * Scales the drawing into a 20 by 20 image by drawing the scaled image
     * into a buffered image and then returning it
     */
    private BufferedImage getScaledImage(int width, int height) {
        Image scaledImg = image.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
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
    private BufferedImage getCenteredImage(int width, int height) {
        BufferedImage centeredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = centeredImage.createGraphics();
        g2.setColor(Color.black);
        g2.fillRect(0, 0, centeredImage.getWidth(), centeredImage.getHeight());
        int[] centerOfMass = getCenterOfMass(image);//calculates the center of mass
        g2.drawImage(image, width / 2 - centerOfMass[0], height / 2 - centerOfMass[1], null);
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
}