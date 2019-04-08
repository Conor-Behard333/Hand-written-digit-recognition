package ProcessingData;

import NeuralNetwork.Function;

import java.io.*;
import java.util.Scanner;

public class DataSet {
    private final int imageSize = 784;
    private int[][] trainingData;
    private Function f = new Function();
    private int batchSize;

    /*
     * Initialises the int 2d array trainingSet
     */
    public DataSet(int batchSize, String filePath) throws IOException {
        this.batchSize = batchSize;
        trainingData = loadData(filePath);

    }

    /*
     * Reads the training values which are stored in a csv (comma separated values)
     * and returns the values in the file as a 2d Array
     *
     * Each row is a different image
     */
    private int[][] loadData(String fileName) throws IOException {
        int[][] trainingData = new int[batchSize][imageSize];
        int row = 0;
        Scanner n = new Scanner(new File(fileName));
        n.useDelimiter(",");
        while (row < batchSize) {
            for (int i = 0; i < imageSize; i++) {
                trainingData[row][i] = (Integer.parseInt(n.next()));
            }
            row++;
            n.nextLine();
        }
        return trainingData;
    }

    public void randomiseTrainingData() {
        for (int i = 0; i < batchSize; i++) {
            int rand = (int) (Math.random() * batchSize);
            int[] temp = trainingData[i];
            trainingData[i] = trainingData[rand];
            trainingData[rand] = temp;
        }

    }

    /*
     * Returns a normalised specified row of the training set data
     *
     * Index starts at 1 because the first value in the array is the label
     * (the number in which the values equal)
     */
    public double[] getInputData(int n) {
        double[] x = new double[imageSize];
        for (int i = 1; i < imageSize; i++) {
            x[i] = trainingData[n][i];
        }
        x = f.normalise(x);
        return x;
    }

    /*
     * Returns the label of the values at a specified line
     */
    public int getLabel(int n) {
        return trainingData[n][0];
    }
}
