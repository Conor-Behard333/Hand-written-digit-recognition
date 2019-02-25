import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class DataSet {
    private static int label;
    private static int[] inputs = new int[784];

    DataSet() {
        ArrayList<Integer> trainingSet = readFile("C:\\Users\\conor\\IdeaProjects\\Files\\mnist_train.csv");
        this.label = trainingSet.get(0);
        inputs[inputs.length - 1] = 0;
        for (int i = 0; i < inputs.length - 1; i++) {
            inputs[i] = trainingSet.get(i + 1);
        }
    }

    private static ArrayList readFile(String fileName) {
        ArrayList<Integer> x = new ArrayList();
        try {
            Scanner n = new Scanner(new File(fileName));
            n.useDelimiter(",");
            while (n.hasNextInt()) {
                x.add(Integer.parseInt(n.next()));
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



