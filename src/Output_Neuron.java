class Output_Neuron {
    private double weightedSum;
    private double output;
    private double[] weights;
    private double[] weightedDeltaHidden;
    private Function f = new Function();

    Output_Neuron(int hiddenNeurons) {
        this.weights = new double[hiddenNeurons];
        this.weights = f.setWeights(weights.length);
        this.weightedDeltaHidden = new double[hiddenNeurons];
    }

    void setOutput(double[] hiddenInputs) {
        this.weightedSum = f.getWeightedSum(hiddenInputs, weights);
        this.output = f.sigmoid(weightedSum);
    }

    void tuneWeights(double LR, double[] hidden_output, double target) {
        //derivative of mean squared error multiplied by the derivative of sigmoid(output)
        double delta = (target - output) * f.dSigmoid(output);
        for (int i = 0; i < weights.length; i++) {
            weights[i] += delta_weights(i, LR, delta, hidden_output);
        }
    }

    double delta_weights(int i, double LR, double delta, double[] hidden_output) {
        weightedDeltaHidden[i] = delta * weights[i];
        return LR * delta * hidden_output[i];
    }

    double[] getWeightedDeltaHidden() {
        return weightedDeltaHidden;
    }

    double getOutput() {
        return output;
    }
}