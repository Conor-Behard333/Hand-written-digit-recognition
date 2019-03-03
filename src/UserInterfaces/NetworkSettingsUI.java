package UserInterfaces;

import javax.swing.*;

public class NetworkSettingsUI {
    private int epochs;
    private int batchSize;
    private int hiddenNeurons;

    public NetworkSettingsUI() {
        JFrame frame = new JFrame();
        String[] epochValues = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};//available epoch values
        String[] batchSizeValues = {"10000", "20000", "30000", "40000", "50000", "60000"};//available batch size values

        String hiddenNeurons = JOptionPane.showInputDialog(frame, "How many hidden neurons do you want?", "Neural Network Settings", JOptionPane.PLAIN_MESSAGE);

        String epochs = (String) JOptionPane.showInputDialog(frame, "How many epochs do you want?", "Neural Network Settings", JOptionPane.QUESTION_MESSAGE, null, epochValues, epochValues[0]);

        String batchSize = (String) JOptionPane.showInputDialog(frame, "How many training value do you want to use?", "Neural Network Settings", JOptionPane.QUESTION_MESSAGE, null, batchSizeValues, batchSizeValues[0]);

        if ((epochs != null) && (epochs.length() > 0)) {
            this.epochs = Integer.parseInt(epochs);
        } else {
            this.epochs = 5;// set to DEFAULT
        }
        if ((hiddenNeurons != null) && (hiddenNeurons.length() > 0)) {
            this.hiddenNeurons = Integer.parseInt(hiddenNeurons);
        } else {
            this.hiddenNeurons = 74;// set to DEFAULT
        }
        if ((batchSize != null) && (batchSize.length() > 0)) {
            this.batchSize = Integer.parseInt(batchSize);
        } else {
            this.batchSize = 60000;//set to DEFAULT
        }
    }

    public int getEpochs() {
        return epochs;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public int getHiddenNeurons() {
        return hiddenNeurons;
    }
}
