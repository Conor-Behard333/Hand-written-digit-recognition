package NeuralNetwork;

class Output_Neuron extends Function {
    private double weightedSum;
    private double output;
    private double[] weights;
    private double[] deltaSum;

    /*
     * Initialises global variables
     */
    Output_Neuron(int hiddenNeurons) {
        this.weights = new double[hiddenNeurons];
        this.weights = randomiseWeights(weights.length);
        this.deltaSum = new double[hiddenNeurons];
    }

    /*
     * Tunes the weights by finding the gradient of softMax which is: error * derivative
     * then adds the learning rate * by the previous output * by the gradient for each weight
     * connected to the certain output neuron
     *
     * Also, the gradient is multiplied by each weight and stored in a variable called deltaSum
     * this is used to tune the weights of the next layer
     */
    void tuneWeights(double LR, double[] prevOutputs, double target) {
        double gradient = (target - output) * derivative(output);
        for (int i = 0; i < weights.length; i++) {
            weights[i] += LR * prevOutputs[i] * gradient;
            deltaSum[i] = gradient * weights[i];
        }
    }

    /*
     * Calculates the output and assigns it to the variable 'output'
     */
    void calculateOutput(double[] weightedSums, int i) {
        output = softMax(weightedSums, i);
    }

    /*
     * Calculates the weighted sum and assigns it to the variable 'weightedSum'
     */
    void calculateWeightedSum(double[] hiddenInputs) {
        weightedSum = getWeightedSum(hiddenInputs, weights);
    }

    double getOutput() {
        return output;
    }

    double getWeightedSum() {
        return weightedSum;
    }

    double[] getDeltaSum() {
        return deltaSum;
    }

}
