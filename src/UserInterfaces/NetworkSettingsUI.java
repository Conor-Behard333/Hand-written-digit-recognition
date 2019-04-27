package UserInterfaces;

import javax.swing.*;

public class NetworkSettingsUI {
    private int epochs;
    private int batchSize;

    public NetworkSettingsUI() {
        JFrame frame = new JFrame();
        String[] epochValues = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};//available epoch values
        String[] batchSizeValues = {"10000", "20000", "30000", "40000", "50000", "60000"};//available batch size values 60000 is the max

        //asks the user how many epochs they want the network to do
        String epochs = (String) JOptionPane.showInputDialog(frame, "How many epochs do you want?", "Neural Network Settings", JOptionPane.QUESTION_MESSAGE, null, epochValues, epochValues[0]);

        //the size of the training data used
        String batchSize = (String) JOptionPane.showInputDialog(frame, "How many training value do you want to use?", "Neural Network Settings", JOptionPane.QUESTION_MESSAGE, null, batchSizeValues, batchSizeValues[0]);

        checkValidEntry(epochs, batchSize);
    }

    private void checkValidEntry(String epochs, String batchSize) {
        if (epochs == null || batchSize == null) {
            System.exit(0);
        } else {
            this.epochs = Integer.parseInt(epochs);
            this.batchSize = Integer.parseInt(batchSize);
        }
    }

    public int getEpochs() {
        return epochs;
    }

    public int getBatchSize() {
        return batchSize;
    }
}
