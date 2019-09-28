package ProcessingData;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class LoadFile {
    public double[] loadWeights(String fileName) {
        double[] weights = new double[0];
        int index = -1;
        try {
            try (Scanner sc = new Scanner(new File(fileName))) {
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
            e.printStackTrace();
        }
        return weights;
    }

    public String loadTextFile(String fileDir) {
        String text = "";
        try {
            try (Scanner sc = new Scanner(new File(fileDir))) {
                while ((sc.hasNext())) {
                    text += sc.nextLine() + "\n";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }
}