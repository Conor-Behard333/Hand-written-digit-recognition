package UserInterfaces.Other;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class SaveOrLoad {
    double[] readFile(String fileName) {
        double[] weights = new double[0];
        int index = -1;
        try {
            try (Scanner sc = new Scanner(new File(fileName + ".txt"))) {
                while ((sc.hasNext())) {
                    if (index == -1) {
                        int size = Integer.parseInt(sc.nextLine());
                        weights = new double[size];
                        index++;
                    } else {
                        double weight = Double.parseDouble(sc.nextLine());
                        weights[index] = weight;
                        index++;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return weights;
    }

    public void writeFile(String fileName, double[] weights) {
        FileWriter write;
        try {
            write = new FileWriter(fileName + ".txt");
            try (PrintWriter printLine = new PrintWriter(write)) {
                for (int i = -1; i < weights.length; i++) {
                    if (i == -1) {
                        printLine.println(weights.length);
                    } else {
                        printLine.println(weights[i]);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}

