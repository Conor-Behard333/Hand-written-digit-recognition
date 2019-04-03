package NeuralNetwork;

class Hidden_Neuron {

    private double output;
    private double[] weights;
    private double[] deltaSum;
    private Function f = new Function();

    /*
     * Initialises global variables
     */
    Hidden_Neuron(int previousLayer) {
        this.weights = new double[previousLayer];
        this.weights = f.randomiseWeights(weights.length);
        this.deltaSum = new double[previousLayer];
    }

    /*
     * Tunes the weights by finding the gradient of softMax which is: deltaSumTotal * derivative
     * then adds the learning rate * by the previous output * by the gradient for each weight
     * connected to the certain output neuron
     *
     * Also, the gradient is multiplied by each weight and stored in a variable called deltaSum
     * this is used to tune the weights of the next layer
     *
     * deltaSumTotal is the sum of all the deltaSums from the previous layer
     */

    void tuneWeights(double LR, double[] prevOutputs, double deltaSumTotal) {
        double gradient = deltaSumTotal * f.derivative(output);
        for (int i = 0; i < weights.length; i++) {
            weights[i] += LR * prevOutputs[i] * gradient;
            deltaSum[i] = gradient * weights[i];
        }
    }

    /*
     * Calculates the output and assigns it to the variable 'output'
     */
    void calculateOutput(double[] inputs) {
        double weightedSum = f.getWeightedSum(inputs, weights);
        this.output = f.sigmoid(weightedSum);
    }

    double[] getDeltaSum() {
        return deltaSum;
    }

    double getOutput() {
        return output;
    }


}