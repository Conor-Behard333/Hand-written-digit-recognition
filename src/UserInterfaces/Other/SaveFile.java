package UserInterfaces.Other;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SaveFile {
    public void save(String fileName, double[] weights) {
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
