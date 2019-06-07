package UserInterfaces;

import javax.swing.*;

public class NetworkSettingsUI {
    private int epochs;
    private int batchSize;
    private int[] numOfHiddenNeurons;
    private JFrame frame = new JFrame();

    public NetworkSettingsUI() {
        setHiddenNeurons();
        setBatchSize();
        setEpochs();
    }

    private void setEpochs() {
        //asks the user how many epochs they want the network to do
        String[] epochValues = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};//available epoch values
        String epochs = (String) JOptionPane.showInputDialog(frame, "How many epochs do you want?", "Neural Network Settings", JOptionPane.QUESTION_MESSAGE, null, epochValues, epochValues[0]);
        checkValidEntry(epochs);
    }

    private void setBatchSize() {
        JTextField batchSizeValue = new JTextField();
        JComponent[] batchSizeInput = getJComponent("How many values do you want to train the network with? Maximum: 60000 ", batchSizeValue);
        while (true) {
            try {
                JOptionPane.showConfirmDialog(frame, batchSizeInput, "Neural Network Settings", JOptionPane.PLAIN_MESSAGE);
                batchSize = Integer.parseInt(batchSizeValue.getText());
                if (batchSize < 0 || batchSize > 60000) {
                    showInvalidInput("Invalid Input! Batch size has to be between 1 and 60000");
                    continue;
                }
            } catch (Exception e) {
                showInvalidInput("Invalid Input!");
                continue;
            }
            break;
        }
    }

    private void setHiddenNeurons() {
        JTextField hiddenLayerValues = new JTextField();
        JTextField hiddenNeuronValues = new JTextField();

        JComponent[] hiddenLayerInput = getJComponent("Number of hidden layers?", hiddenLayerValues);
        JComponent[] hiddenNeuronInput = getJComponent("Number of hidden neurons?", hiddenNeuronValues);

        int numOfHiddenLayers = getNumOfHiddenLayers(hiddenLayerValues, hiddenLayerInput);

        numOfHiddenNeurons = new int[numOfHiddenLayers];

        setHiddenNeurons(hiddenNeuronValues, hiddenNeuronInput, numOfHiddenLayers);

    }

    private int getNumOfHiddenLayers(JTextField hiddenLayerValues, JComponent[] hiddenLayerInput) {
        int numOfHiddenLayers;
        while (true) {
            try {
                JOptionPane.showConfirmDialog(frame, hiddenLayerInput, "Neural Network Settings", JOptionPane.PLAIN_MESSAGE);
                numOfHiddenLayers = Integer.parseInt(hiddenLayerValues.getText());
                if (numOfHiddenLayers < 1) {
                    showInvalidInput("Invalid Input! Requires a number greater than 0 ");
                    continue;
                }
            } catch (Exception e) {
                showInvalidInput("Invalid Input!");
                continue;
            }
            break;
        }
        return numOfHiddenLayers;
    }

    private void setHiddenNeurons(JTextField hiddenNeuronValues, JComponent[] hiddenNeuronInput, int numOfHiddenLayers) {
        for (int i = 0; i < numOfHiddenLayers; i++) {
            while (true) {
                try {
                    JOptionPane.showConfirmDialog(frame, hiddenNeuronInput, "Neural Network Settings", JOptionPane.PLAIN_MESSAGE);
                    numOfHiddenNeurons[i] = Integer.parseInt(hiddenNeuronValues.getText());
                    if (numOfHiddenNeurons[i] < 1) {
                        showInvalidInput("Invalid Input! Requires a number greater than 0 ");
                        continue;
                    }
                } catch (Exception e) {
                    showInvalidInput("Invalid Input!");
                    continue;
                }
                break;
            }
        }
    }

    private void showInvalidInput(String errorMessage) {
        JOptionPane.showConfirmDialog(frame, errorMessage, "Neural Network Settings", JOptionPane.PLAIN_MESSAGE);
    }

    private JComponent[] getJComponent(String text, JTextField textField) {
        return new JComponent[]{
                new JLabel(text),
                textField
        };
    }

    private void checkValidEntry(String epochs) {
        if (epochs == null) {
            System.exit(0);
        } else {
            this.epochs = Integer.parseInt(epochs);
        }
    }

    public int getEpochs() {
        return epochs;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public int[] getHiddenNeurons() {
        return numOfHiddenNeurons;
    }
}
