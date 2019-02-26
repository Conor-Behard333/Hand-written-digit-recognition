import javax.xml.crypto.Data;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class DataSet {
    private static int label;
    private static int[] inputs = new int[784];

    DataSet() {
        int[][] trainingSet = readFile("C:\\Users\\conor\\IdeaProjects\\Files\\mnist_train.csv", 5000);
        System.out.println(Arrays.deepToString(trainingSet));
        System.out.println(trainingSet[0].length);
        for (int i = 0; i < 784; i++) {
            inputs[i] = trainingSet[2][i];
        }
    }

    public static void main(String[] args) {
        DataSet d = new DataSet();
        System.out.println(Arrays.toString(d.getInput()));
    }

    private static int[][] readFile(String fileName, int batchSize) {
        int[][] x = new int[batchSize][784];
        int start = 0;
        try {
            Scanner n = new Scanner(new File(fileName));
            n.useDelimiter(",");

            while (start < batchSize) {
                while (n.hasNextInt()) {
                    for (int i = 0; i < 784; i++) {
                        x[start][i] = (Integer.parseInt(n.next()));
                    }
                    break;
                }
                start++;
                n.nextLine();
            }

        } catch (IOException e) {
            System.out.println(e);
        }
        return x;
    }

    int[] getInput() {
        return inputs;
    }

    int getLabel() {
        return label;
    }

}



