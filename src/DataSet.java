import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class DataSet {
    int[][] trainingSet;

    DataSet(int batchSize, String filePath) {
        trainingSet = loadData(filePath, batchSize);
    }

    private static int[][] loadData(String fileName, int batchSize) {
        int[][] x = new int[batchSize][784];
        int row = 0;
        try {
            Scanner n = new Scanner(new File(fileName));
            n.useDelimiter(",");
            while (row < batchSize) {
                while (n.hasNextInt()) {
                    for (int i = 0; i < 784; i++) {
                        x[row][i] = (Integer.parseInt(n.next()));
                    }
                    break;
                }
                row++;
                n.nextLine();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return x;
    }

    double[] getInputData(int n) {
        double[] x = new double[784];
        for (int i = 1; i < 784; i++) {
            x[i] = trainingSet[n][i];
        }
        x = normalise(x);
        return x;
    }

    double[] normalise(double[] x) {
        int min = 0;
        double max = getMax(x);
        double[] y = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            y[i] = (x[i] - min) / (max - min);
        }
        return y;
    }

    double getMax(double[] x) {
        double max = 0;
        for (int i = 0; i < x.length; i++) {
            if (x[i] > max) {
                max = x[i];
            }
        }
        return max;
    }


    int getLabel(int n) {
        return trainingSet[n][0];
    }

}



