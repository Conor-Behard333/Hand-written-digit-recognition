import javax.swing.*;

public class NetworkSettingsUI {
    private int epochs;
    private int batchSize;
    private int hiddenNeurons;

    NetworkSettingsUI() {
        JFrame frame = new JFrame("test");
        String[] epochNumbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        String[] dataAmounts = {"10000", "20000", "30000", "40000", "50000", "60000"};

        String hiddenNeurons = JOptionPane.showInputDialog(frame, "How many hidden neurons do you want?", "Neural Network Settings", JOptionPane.PLAIN_MESSAGE);

        String epochs = (String) JOptionPane.showInputDialog(frame, "How many epochs do you want?", "Neural Network Settings", JOptionPane.QUESTION_MESSAGE, null, epochNumbers, epochNumbers[0]);

        String batchSize = (String) JOptionPane.showInputDialog(frame, "How many training value do you want to use?", "Neural Network Settings", JOptionPane.QUESTION_MESSAGE, null, dataAmounts, dataAmounts[2]);

        if ((hiddenNeurons != null) && (hiddenNeurons.length() > 0) && (batchSize != null) && (batchSize.length() > 0) && (epochs != null) && (epochs.length() > 0)) {
            this.epochs = Integer.parseInt(epochs);
            this.batchSize = Integer.parseInt(batchSize);
            this.hiddenNeurons = Integer.parseInt(hiddenNeurons);
        } else {
            System.out.println("Insufficient inputs");
        }
    }

    int getEpochs() {
        return epochs;
    }

    int getBatchSize() {
        return batchSize;
    }

    int getHiddenNeurons() {
        return hiddenNeurons;
    }
}
