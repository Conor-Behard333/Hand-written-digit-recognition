package UserInterfaces.Other;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class ConfidenceUI extends JFrame {
    private String[] confidence = new String[10];
    private JLabel[] guessLabels = new JLabel[10];
    private JProgressBar[] progressBars = new JProgressBar[10];

    /*
     * Creates the window which displays the confidence of the networks guess
     */
    public ConfidenceUI() {
        setTitle("Neural Network - Digit Classifier - Confidence");
        setBounds(1410, 220, 500, 600);
        setBackground(Color.black);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        createUI();
        setVisible(true);
    }

    /*
     * Adds a label and progress bar into the window
     */
    private void createUI() {
        int yPos = 10;
        for (int i = 0; i < 10; i++) {
            guessLabels[i] = createLabel(yPos, i);
            progressBars[i] = createProgressBar(yPos);
            yPos += 50;
            getContentPane().add(guessLabels[i]);
            getContentPane().add(progressBars[i]);
        }
        getContentPane().add(createPanel());
    }

    /*
     * creates a progress Bar
     */
    private JProgressBar createProgressBar(int yPos) {
        JProgressBar progressBar = new JProgressBar();
        progressBar.setBounds(140, yPos, 200, 20);
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setStringPainted(true);
        return progressBar;
    }

    /*
     * creates a label
     */
    private JLabel createLabel(int yPos, int i) {
        JLabel label = new JLabel(i + ": 0.00%");
        label.setBounds(10, yPos, 100, 20);
        label.setFont(new Font(getName(), Font.PLAIN, 20));
        return label;
    }

    /*
     * creates a JPanel
     */
    private JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.setLocation(500, 600);
        return panel;
    }

    /*
     * Sets the progress bar and guess label to the appropriate values (values that the network generated)
     */
    public void setConfidence(double[] outputs) {
        formatConfidenceAsPercentage(outputs);
        setValuesOfProgressBar();
    }

    private void setValuesOfProgressBar() {
        String[] guess = {"0: " + confidence[0] + "%", "1: " + confidence[1] + "%", "2: " + confidence[2] + "%", "3: "
                + confidence[3] + "%", "4: " + confidence[4] + "%", "5: " + confidence[5] + "%", "6: " + confidence[6]
                + "%", "7: " + confidence[7] + "%", "8: " + confidence[8] + "%", "9: " + confidence[9] + "%"};
        for (int i = 0; i < 10; i++) {
            int value = (int) (Double.parseDouble(confidence[i]));
            progressBars[i].setForeground(getPbColour(value));
            progressBars[i].setValue(value);
            guessLabels[i].setText(guess[i]);
        }
    }

    private void formatConfidenceAsPercentage(double[] outputs) {
        DecimalFormat df = new DecimalFormat("#.##");
        for (int i = 0; i < 10; i++) {
            confidence[i] = df.format(outputs[i] * 100);
        }
    }

    /*
     * Returns the colour of the progress bar based on what the confidence is
     */
    private Color getPbColour(int value) {
        if (value >= 0 && value < 20) {
            return new Color(0xFF1000);//red
        } else if (value >= 20 && value < 40) {
            return new Color(0xFF941E);//orange
        } else if (value >= 40 && value < 80) {
            return new Color(0xFFF41F);//yellow
        } else if (value >= 80 && value <= 100) {
            return new Color(0x36FF00);//green
        }
        return null;
    }
}