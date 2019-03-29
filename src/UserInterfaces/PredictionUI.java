package UserInterfaces;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class PredictionUI extends JFrame {
    private String[] confidence = new String[10];
    private JLabel[] predictionsLabel = new JLabel[10];

    public PredictionUI() {
        setTitle("Neural Network - Digit Classifier - Predictions");
        setSize(200, 600);
        setBackground(Color.black);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(1410, 220);
        setResizable(false);
        createUI();
        setVisible(true);
    }

    private void createUI() {
        int yPos = 10;
        for (int i = 0; i < 10; i++) {
            predictionsLabel[i] = new JLabel("Guess " + i + ": 0.00%");
            predictionsLabel[i].setSize(200, 100);
            predictionsLabel[i].setFont(new Font(getName(), Font.PLAIN, 20));
            predictionsLabel[i].setLocation(10, yPos);
            yPos += 50;
            getContentPane().add(predictionsLabel[i]);
        }
        getContentPane().add(createPanel(500, 600));
    }

    private JPanel createPanel(int x, int y) {
        JPanel temp = new JPanel();
        temp.setLocation(x, y);
        return temp;
    }

    public void setPredictions(double[] outputs) {
        DecimalFormat df = new DecimalFormat("#.##%");
        for (int i = 0; i < 10; i++) {
            confidence[i] = df.format(outputs[i]);
        }
        String[] guess = {"Guess 0: " + confidence[0], "Guess 1: " + confidence[1], "Guess 2: " + confidence[2], "Guess 3: " + confidence[3], "Guess 4: " + confidence[4], "Guess 5: " + confidence[5], "Guess 6: " + confidence[6], "Guess 7: " + confidence[7], "Guess 8: " + confidence[8], "Guess 9: " + confidence[9]};
        for (int i = 0; i < 10; i++) {
            predictionsLabel[i].setText(guess[i]);
        }
    }
}