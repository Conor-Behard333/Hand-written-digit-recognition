import java.io.*;
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

    int[] getInputData(int n) {
        int[] x = new int[784];
        for (int i = 1; i < 784; i++) {
            x[i] = trainingSet[n][i];
        }
        return x;
    }

    int getLabel(int n) {
        return trainingSet[n][0];
    }

}



