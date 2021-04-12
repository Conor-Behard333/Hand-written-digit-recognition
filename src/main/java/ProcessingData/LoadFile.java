package ProcessingData;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class LoadFile {
    /*
     * Reads a text file and stores it as a single string
     */
    public String loadTextFile(String fileDir) {
        StringBuilder text = new StringBuilder();
        File tmpFile = null;
        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileDir);
            tmpFile = File.createTempFile("file", "temp");
            assert is != null;
            FileUtils.copyInputStreamToFile(is, tmpFile);

            try (Scanner sc = new Scanner(tmpFile)) {
                while ((sc.hasNext())) {
                    text.append(sc.nextLine()).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            tmpFile.delete();
        }

        return text.toString();
    }
}