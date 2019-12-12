package NeuralNetwork;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Function {
    /*
     * Calculates and returns the weighted sum for a neuron
     *
     * Weighted sum = Σ(weight[i] * inputs[i]))
     */
    double getWeightedSum(double[] inputs, double... weights) {
        double sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += weights[i] * inputs[i];
        }
        return sum;
    }

    /*
     * Returns a double array filled with random decimals
     * between -1 and 1
     */
    double[] randomiseWeights(int size) {
        double[] arr = new double[size];
        for (int i = 0; i < size; i++) {
            arr[i] = (Math.random() * 1 - 1) / Math.sqrt(size);
        }
        return arr;
    }

    /*
     * Returns the output of the softMax function for a specific neuron:
     * output = (e^weightedSum[i]) / Σ(e^weightedSum[i])
     */
    double softMax(double[] weightedSums, int neuron) {
        double sum = getSumOfWeightedSum(weightedSums);
        double[] output = getOutputArray(weightedSums, sum);
        return output[neuron];
    }

    private double[] getOutputArray(double[] weightedSums, double sum) {
        double[] output = new double[weightedSums.length];
        for (int i = 0; i < output.length; i++) {
            output[i] = Math.exp(weightedSums[i]) / sum;
        }
        return output;
    }

    private double getSumOfWeightedSum(double[] weightedSums) {
        double sum = 0;
        for (double weightedSum : weightedSums) {
            sum += Math.exp(weightedSum);
        }
        return sum;
    }

    /*
     * Returns a double array which contains normalised values
     *
     * Normalisation function:
     * normalised number = (number - minimum)/(maximum - minimum)
     */
    protected double[] normalise(double[] inputs) {
        int min = 0;
        double max = getMax(inputs);
        double[] y = new double[inputs.length];
        for (int i = 0; i < inputs.length; i++) {
            y[i] = (inputs[i] - min) / (max - min);
        }
        return y;
    }

    /*
     * Returns the maximum value within an array
     */
    private double getMax(double[] values) {
        double max = 0;
        for (double value : values) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    void tuneBias(int numOfNeurons, Bias_Neuron bias, double learningRate, double gradient) {
        for (int i = 0; i < numOfNeurons; i++) {
            bias.setWeight(learningRate * 1 * gradient, i);
        }
    }

    public double[] getTarget(int label) {
        double[][] targets = {{1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 1}};
        return targets[label];
    }

    /*
     * Returns the value of x having gone through
     * the sigmoid function
     */
    double sigmoid(double x) {
        return 1d / (1 + Math.exp(-x));
    }

    /*
     * Returns the value of x having gone through
     * the derivative of sigmoid and soft max function
     */
    double derivative(double x) {
        return x * (1 - x);
    }

    public String getChoiceAlert(String[] options, String title, String contentText) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(options[0], options);
        dialog.setTitle(title);
        dialog.setContentText(contentText);
        Optional<String> response = dialog.showAndWait();
        if (response.isPresent()) {
            return response.get();
        } else {
            System.exit(0);
        }
        return null;
    }

    public Alert conformationAlert(String title, String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(contentText);
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yesButton, noButton, cancelButton);
        return alert;
    }
}