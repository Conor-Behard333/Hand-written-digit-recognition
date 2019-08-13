package ProcessingData;

import NeuralNetwork.Function;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageConverter extends Function {
    /*
     * Reads the image that the user drew and scales the image
     * to a 20 by 20 png, it then centres the image onto a 28 by 28 image
     * then it stores the brightness of each pixel into a double array
     * which is then normalised (put between the values of 0 and 1) and finally
     * returned to then be used to be put through the neural network
     */
    public double[] getInput() {
        int[] input = new int[784];
        BufferedImage testImage = null;
        BufferedImage image;
        try {
            image = ImageIO.read(new File("Resources\\image.png"));//reads the image the user drew
            image = getScaledImage(20, 20, image);//scales the image to a 20 by 20 png
            image = getCenteredImage(image);//centres the image onto a 28 by 28 image
            //temporary
            int[][] temp = new int[28][28];
            getPixelValues(input, image);
            int k = 0;
            for (int i = 0; i < 28; i++) {
                for (int j = 0; j < 28; j++) {
                    temp[i][j] = input[k];
                    k++;
                }
            }

            int[][] newImage = createNewImage(temp);

            testImage = setPixelValues(newImage);

            testImage = getScaledImage(28, 28, testImage);

            getPixelValues(input, testImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ImageIO.write(testImage, "png", new File("Resources\\ImageTest.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        double[] inputDouble = new double[input.length];
        for (int i = 0; i < input.length; i++) {
            inputDouble[i] = input[i];
        }
        return normalise(inputDouble);//normalises the input data
    }

    private BufferedImage setPixelValues(int[][] newImage) {
        BufferedImage testImage;
        testImage = new BufferedImage(newImage[0].length, newImage.length, BufferedImage.TYPE_INT_ARGB);
        //set the pixel value
        for (int i = 0; i < testImage.getHeight(); i++) {
            for (int j = 0; j < testImage.getWidth(); j++) {
                int alpha = 255;
                int value = newImage[i][j];
                int p = (alpha << 24) | (value << 16) | (value << 8) | value;
                testImage.setRGB(j, i, p);
            }
        }
        return testImage;
    }

    private int[][] createNewImage(int[][] temp) {
        int upMost = getLeftAndUpMost(temp, true);
        int downMost = getRightAndDownMost(temp, true);
        int leftMost = getLeftAndUpMost(temp, false);
        int rightMost = getRightAndDownMost(temp, false);

        int height = (downMost - upMost) + 1;
        int width = (rightMost - leftMost) + 1;
        int padding_x = 10;
        int padding_y = 8;
        int[][] newImage = new int[height + padding_x][width + padding_y];

        fillMatrix(newImage);

        int x = padding_x / 2;
        int y = padding_y / 2;
        for (int i = upMost; i <= downMost; i++) {
            for (int j = leftMost; j <= rightMost; j++) {
                newImage[x][y] = temp[i][j];
                y++;
            }
            x++;
            y = padding_y / 2;
        }
        return newImage;
    }

    private void fillMatrix(int[][] newImage) {
        for (int i = 0; i < newImage.length; i++) {
            for (int j = 0; j < newImage[0].length; j++) {
                newImage[i][j] = 0;
            }
        }
    }

    private int getRightAndDownMost(int[][] temp2, boolean downMost) {
        for (int i = temp2.length - 1; i >= 0; i--) {
            for (int j = temp2.length - 1; j >= 0; j--) {
                if (temp2[i][j] > 0 && downMost) {
                    return i;
                } else {
                    if (temp2[j][i] > 0) {
                        return i;
                    }
                }
            }
        }
        return 0;
    }

    private int getLeftAndUpMost(int[][] temp2, boolean upMost) {
        for (int i = 0; i < temp2.length; i++) {
            for (int j = 0; j < temp2.length; j++) {
                if (temp2[i][j] > 0 && upMost) {
                    return i;
                } else {
                    if (temp2[j][i] > 0) {
                        return i;
                    }
                }
            }
        }
        return 0;
    }

    private void getPixelValues(int[] input, BufferedImage image) {
        int i = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixelBrightness = image.getRGB(x, y) & 0xff;
                input[i] = pixelBrightness;//stores the brightness of each pixel into a double array
                i++;
            }
        }
    }


    private BufferedImage getScaledImage(int width, int height, BufferedImage image) {
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
    private BufferedImage getCenteredImage(BufferedImage image) {
        BufferedImage centeredImage = new BufferedImage(28, 28, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = centeredImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
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
}