package UserInterfaces;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

class SettingsUI {
    private int batchSize;
    private int epochs;
    private int[] hiddenNeurons;
    private double learningRate;

    SettingsUI() {
        int hiddenLayers = (int) getAlert("Hidden Layers", "Enter number of hidden layers: ", 5, false);
        hiddenNeurons = setHiddenNeurons(hiddenLayers);
        batchSize = (int) getAlert("Batch Size", "Enter Batch size: ", 60000, false);
        epochs = (int) getAlert("Epochs", "Enter number of epochs: ", 10, false);
        learningRate = getAlert("Learning Rate", "Enter the learning rate: ", 1, true);
    }

    int getBatchSize() {
        return batchSize;
    }

    int getEpochs() {
        return epochs;
    }

    int[] getHiddenNeurons() {
        return hiddenNeurons;
    }

    double getLearningRate() {
        return learningRate;
    }

    private double getAlert(String title, String contentText, double upperBound, boolean learningRate) {
        final double[] variable = {0};
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setContentText(contentText);
        while (true) {
            try {
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    variable[0] = Double.parseDouble(result.get());
                } else {
                    System.exit(0);
                }
                if ((variable[0] < 1 || variable[0] > upperBound) && !learningRate) {
                    showInvalidInput("Invalid Input! Requires a number between 1 and " + upperBound);
                    continue;
                } else if ((variable[0] < 0 || variable[0] > upperBound) && learningRate) {
                    showInvalidInput("Invalid Input! Requires a number between 0 and " + upperBound);
                    continue;
                }
            } catch (Exception e) {
                continue;
            }
            break;
        }
        return variable[0];
    }

    private int[] setHiddenNeurons(int hiddenLayers) {
        int[] hiddenNeurons = new int[hiddenLayers];
        for (int i = 0; i < hiddenLayers; i++) {
            hiddenNeurons[i] = (int) getAlert("Hidden Neurons", "Enter number of hidden neurons in layer " + (i + 1) + ": ", 150, false);
        }
        return hiddenNeurons;
    }

    private void showInvalidInput(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Input");
        alert.setHeaderText(error);
        alert.setContentText("Please re-enter choice");
        alert.showAndWait();
    }
}