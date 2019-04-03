package ProcessingData;

import NeuralNetwork.Function;

import java.io.*;
import java.util.Scanner;

public class DataSet {
    private final int imageSize = 784;
    private int[][] trainingSet;
    private Function f = new Function();

    /*
     * Initialises the int 2d array trainingSet
     */
    public DataSet(int batchSize, String filePath) throws IOException {
        trainingSet = loadData(filePath, batchSize);
    }

    /*
     * Reads the training values which are stored in a csv (comma separated values)
     * and returns the values in the file as a 2d Array
     *
     * Each row is a different image
     */
    private int[][] loadData(String fileName, int batchSize) throws IOException {
        int[][] x = new int[batchSize][imageSize];
        int row = 0;
        Scanner n = new Scanner(new File(fileName));
        n.useDelimiter(",");
        while (row < batchSize) {
            for (int i = 0; i < imageSize; i++) {
                x[row][i] = (Integer.parseInt(n.next()));
            }
            row++;
            n.nextLine();
        }
        return x;
    }

    /*
     * Returns a specified row of the training set data
     */
    public double[] getInputData(int n) {
        double[] x = new double[imageSize];
        for (int i = 1; i < imageSize; i++) {
            x[i] = trainingSet[n][i];
        }
        x = f.normalise(x);
        return x;
    }

    public int getLabel(int n) {
        return trainingSet[n][0];
    }

}
