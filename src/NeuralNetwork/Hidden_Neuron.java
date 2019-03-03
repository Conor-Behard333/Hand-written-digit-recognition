package NeuralNetwork;

class Hidden_Neuron {
    private double output;
    private double[] weights;
    private Function f = new Function();

    Hidden_Neuron(int inputNeurons) {
        this.weights = new double[inputNeurons];
        this.weights = f.randomiseWeights(weights.length);
    }

    void tuneWeights(double LR, double[] inputs, double weightedDeltaHiddenTotal) {
        for (int i = 0; i < weights.length; i++) {
            weights[i] += delta_weights(LR, inputs[i], weightedDeltaHiddenTotal);
        }
    }

    private double delta_weights(double LR, double input, double weightedDeltaHiddenTotal) {
        double deltaOutput = f.derivative(output) * weightedDeltaHiddenTotal;
        return LR * deltaOutput * input;
    }

    double getOutput() {
        return output;
    }

    void setOutput(double[] inputs) {
        double weightedSum = f.getWeightedSum(inputs, weights);
        this.output = f.sigmoid(weightedSum);
    }
}