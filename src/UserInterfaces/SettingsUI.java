package UserInterfaces;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

class SettingsUI {
    private int batchSize;
    private int epochs;
    private int[] hiddenNeurons;

    SettingsUI() {
        int hiddenLayers = getAlert("Hidden Layers", "Enter number of hidden layers: ", 5);
        this.hiddenNeurons = setHiddenNeurons(hiddenLayers);
        this.batchSize = getAlert("Batch Size", "Enter Batch size: ", 60000);
        this.epochs = getAlert("Epochs", "Enter number of epochs: ", 10);
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

    private int getAlert(String title, String contentText, int upperBound) {
        final int[] variable = {0};
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setContentText(contentText);
        while (true) {
            try {
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    variable[0] = Integer.parseInt(result.get());
                } else {
                    System.exit(0);
                }
                if (variable[0] < 1 || variable[0] > upperBound) {
                    showInvalidInput("Invalid Input! Requires a number between 1 and " + upperBound);
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
            hiddenNeurons[i] = getAlert("Hidden Neurons", "Enter number of hidden neurons in layer " + (i + 1) + ": ", 150);
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
