class Hidden_Neuron {
    private double weightedSum;
    private double output;
    private double[] weights;
    private Function f = new Function();

    Hidden_Neuron(int inputNeurons) {
        this.weights = new double[inputNeurons];
        this.weights = f.setWeights(weights.length);
    }

    void tuneWeights(double LR, int[] inputs, double weightedDeltaHiddenTotal) {
        for (int i = 0; i < weights.length; i++) {
            weights[i] += delta_weights(LR, inputs[i], weightedDeltaHiddenTotal);
        }
    }

    private double delta_weights(double LR, double input, double weightedDeltaHiddenTotal) {
        //derivative of sigmoid(output) multiplied with the hidden weighted delta total
        double deltaOutput = f.derivative(output) * weightedDeltaHiddenTotal;
        //learning rate multiplied with the delta output and the input
        return LR * deltaOutput * input;
    }

    double getOutput() {
        return output;
    }

    void setOutput(int[] inputs) {
        this.weightedSum = f.getWeightedSum(inputs, weights);
        this.output = f.sigmoid(weightedSum);
    }
}