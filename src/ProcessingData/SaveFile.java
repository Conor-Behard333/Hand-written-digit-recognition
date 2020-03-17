package ProcessingData;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SaveFile {
    /*
     * Writes the weight values (stored in an array) to a text file
     */
    public void save(String fileName, double[] weights) {
        try {
            FileWriter write = new FileWriter(fileName + ".txt", false);
            try (PrintWriter printLine = new PrintWriter(write)) {
                for (int i = -1; i < weights.length; i++) {
                    if (i == -1) {                  /*first line contains the number of weights in the network*/
                        printLine.println(weights.length);
                    } else {
                        printLine.println(weights[i]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}