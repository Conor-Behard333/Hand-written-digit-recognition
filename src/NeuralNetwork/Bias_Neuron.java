package NeuralNetwork;

public class Bias_Neuron {
    private final int input = 1;
    private double weight = Math.random() * 2 - 1;
    private double output = input * weight;

    void setWeight(double delta) {
        weight += delta;
    }

    double getOutput() {
        return output;
    }
}