package ProcessingData;

import NeuralNetwork.Function;

import java.io.*;
import java.util.Scanner;

public class DataSet {
    private final int imageSize = 784;
    private int[][] trainingSet;
    private Function f = new Function();

    public DataSet(int batchSize, String filePath) throws IOException {
        trainingSet = loadData(filePath, batchSize);
    }

    private int[][] loadData(String fileName, int batchSize) throws IOException {
        int[][] x = new int[batchSize][imageSize];
        int row = 0;
        Scanner n = new Scanner(new File(fileName));
        n.useDelimiter(",");
        while (row < batchSize) {
            while (n.hasNextInt()) {
                for (int i = 0; i < imageSize; i++) {
                    x[row][i] = (Integer.parseInt(n.next()));
                }
                break;
            }
            row++;
            n.nextLine();
        }
        return x;
    }

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
