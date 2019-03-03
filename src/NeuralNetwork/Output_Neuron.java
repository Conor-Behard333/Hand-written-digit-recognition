package NeuralNetwork;

class Output_Neuron {
    private double weightedSum;
    private double output;
    private double[] weights;
    private double[] weightedDeltaHidden;
    private Function f = new Function();

    Output_Neuron(int hiddenNeurons) {
        this.weights = new double[hiddenNeurons];
        this.weights = f.randomiseWeights(weights.length);
        this.weightedDeltaHidden = new double[hiddenNeurons];
    }

    void tuneWeights(double LR, double[] hidden_output, double target) {
        double delta = (target - output) * f.derivative(output);
        for (int i = 0; i < weights.length; i++) {
            weights[i] += delta_weights(i, LR, delta, hidden_output);
        }
    }

    private double delta_weights(int i, double LR, double delta, double[] hidden_output) {
        weightedDeltaHidden[i] = delta * weights[i];
        return LR * delta * hidden_output[i];
    }

    double[] getWeightedDeltaHidden() {
        return weightedDeltaHidden;
    }

    double getOutput() {
        return output;
    }

    void setOutput(double output) {
        this.output = output;
    }

    double getWeightedSum() {
        return weightedSum;
    }

    void setWeightedSum(double[] hiddenInputs) {
        weightedSum = f.getWeightedSum(hiddenInputs, weights);
    }
}