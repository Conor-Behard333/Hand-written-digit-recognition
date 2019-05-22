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
        JOptionPane.showConfirmDialog(frame, batchSizeInput, "Neural Network Settings", JOptionPane.PLAIN_MESSAGE);
        batchSize = Integer.parseInt(batchSizeValue.getText());
    }

    private void setHiddenNeurons() {
        JTextField hiddenLayerValues = new JTextField();
        JTextField hiddenNeuronValues = new JTextField();

        JComponent[] hiddenLayerInput = getJComponent("Number of hidden layers?", hiddenLayerValues);
        JComponent[] hiddenNeuronInput = getJComponent("Number of hidden neurons?", hiddenNeuronValues);

        JOptionPane.showConfirmDialog(frame, hiddenLayerInput, "Neural Network Settings", JOptionPane.PLAIN_MESSAGE);
        int numOfHiddenLayers = Integer.parseInt(hiddenLayerValues.getText());
        numOfHiddenNeurons = new int[numOfHiddenLayers];

        for (int i = 0; i < numOfHiddenLayers; i++) {
            JOptionPane.showConfirmDialog(frame, hiddenNeuronInput, "Neural Network Settings", JOptionPane.PLAIN_MESSAGE);
            numOfHiddenNeurons[i] = Integer.parseInt(hiddenNeuronValues.getText());
        }
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
