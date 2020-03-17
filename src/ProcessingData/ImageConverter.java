package ProcessingData;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageConverter extends NeuralNetwork.Function {
    /*
     * Reads the image that the user drew and scales the image
     * to a 20 by 20 png, it then centres the image onto a 28 by 28 image
     * then it stores the brightness of each pixel into a double array
     * which is then normalised (put between the values of 0 and 1) and finally
     * returned to then be used to be put through the neural network
     */
    public double[] getInput() {
        int[] input = new int[784];
        try {
            //Stage 1
            BufferedImage image = ImageIO.read(new File("Resources\\Images\\image.png"));   /*reads the image the user drew*/
            image = getScaledImage(20, 20, image);         /*scales the image to 20 by 20*/
            image = getCenteredImage(image);        /*centres the image onto a 28 by 28 image*/

            //Stage 2
            int[][] copyOfImage = convertImageInto2dArray(input, image);

            //Stage 3
            int[][] newImage = createNewImage(copyOfImage);

            //Stage 4
            BufferedImage convertedImage = setPixelValues(newImage);

            //Stage 5
            convertedImage = getScaledImage(28, 28, convertedImage);    /*scales the image to 28 by 28*/

            //Stage 6
            getPixelValues(input, convertedImage);
            ImageIO.write(convertedImage, "png", new File("Resources\\Images\\ConvertedImage.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        double[] inputDouble = new double[input.length];
        for (int i = 0; i < input.length; i++) {
            inputDouble[i] = input[i];
        }
        return normalise(inputDouble);//normalises the input data
    }

    /*
     * Converts a 1 dimensional array into a 2d array containing the pixel values
     * of the image
     */
    private int[][] convertImageInto2dArray(int[] input, BufferedImage image) {
        int[][] copyOfImage = new int[28][28];
        getPixelValues(input, image);
        int k = 0;
        for (int i = 0; i < 28; i++) {
            for (int j = 0; j < 28; j++) {
                copyOfImage[i][j] = input[k];
                k++;
            }
        }
        return copyOfImage;
    }

    private BufferedImage setPixelValues(int[][] newImage) {
        BufferedImage convertedImage;
        convertedImage = new BufferedImage(newImage[0].length, newImage.length, BufferedImage.TYPE_INT_ARGB);
        //set the pixel value
        for (int i = 0; i < convertedImage.getHeight(); i++) {
            for (int j = 0; j < convertedImage.getWidth(); j++) {
                int alpha = 255;
                int value = newImage[i][j];
                int p = (alpha << 24) | (value << 16) | (value << 8) | value;
                convertedImage.setRGB(j, i, p);
            }
        }
        return convertedImage;
    }

    /*
     * Object recognition:
     * Finds the up-most, left-most, right-most and down-most pixel of the image that the user
     * drew and crops the image down
     */
    private int[][] createNewImage(int[][] temp) {
        int upMost = getLeftAndUpMost(temp, true);//finds up most pixel
        int downMost = getRightAndDownMost(temp, true);//finds down most pixel
        int leftMost = getLeftAndUpMost(temp, false);//finds left most pixel
        int rightMost = getRightAndDownMost(temp, false);//finds right most pixel

        int height = (downMost - upMost);
        int width = (rightMost - leftMost);
        
        //adds padding on the x and y to make the image slightly larger
        int padding_y = 8;
        int padding_x = 8;
        int[][] newImage = new int[height + 1 + padding_y][width + 1 + padding_x];

        fillMatrix(newImage);

        //re-creates image starting from up most and ending at the down most pixel
        int row = padding_y / 2;
        int column = padding_x / 2;
        for (int i = upMost; i <= downMost; i++) {
            for (int j = leftMost; j <= rightMost; j++) {
                newImage[row][column] = temp[i][j];
                column++;
            }
            row++;
            column = padding_x / 2;
        }
        return newImage;
    }

    /*
     * Fills a 2d array with 0's
     */
    private void fillMatrix(int[][] newImage) {
        for (int i = 0; i < newImage.length; i++) {
            for (int j = 0; j < newImage[0].length; j++) {
                newImage[i][j] = 0;
            }
        }
    }

    /*
     * Return the right and down most pixel of the image drawn by the user
     */
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

    /*
     * Return the left and up most pixel of the image drawn by the user
     */
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

    /*
     * Obtains the brightness of each pixel and stores it in the array 'input'
     */
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

    /*
     * Scales an image to the given width and height
     */
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