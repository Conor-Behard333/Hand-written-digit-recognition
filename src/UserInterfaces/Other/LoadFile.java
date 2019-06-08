package UserInterfaces.Other;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class LoadFile {
    public double[] load(String fileName) {
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

}
