package NeuralNetwork;

class Output_Neuron {
    private double weightedSum;
    private double output;
    private double[] weights;
    private double[] deltaSum;
    private Function f = new Function();

    Output_Neuron(int hiddenNeurons) {
        this.weights = new double[hiddenNeurons];
        this.weights = f.randomiseWeights(weights.length);
        this.deltaSum = new double[hiddenNeurons];
    }

    void tuneWeights(double LR, double[] prevOutputs, double target) {
        double gradient = (target - output) * f.derivative(output);
        for (int i = 0; i < weights.length; i++) {
            weights[i] += LR * prevOutputs[i] * gradient;
            deltaSum[i] = gradient * weights[i];
        }
    }

    double[] getDeltaSum() {
        return deltaSum;
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