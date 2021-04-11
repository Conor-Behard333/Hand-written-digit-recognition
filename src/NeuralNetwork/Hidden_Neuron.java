package NeuralNetwork;

import java.io.Serializable;

class Hidden_Neuron extends Function implements Serializable{
    private double output;
    private double[] weights;
    private final double[] DELTA_SUM;
    
    /*
     * Constructor to initialises global variables
     */
    Hidden_Neuron(int previousLayerSize) {
        this.weights = new double[previousLayerSize];
        this.weights = randomiseWeights(weights.length);
        this.DELTA_SUM = new double[previousLayerSize];
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
    
    void tuneWeights(double learningRate, double[] prevOutputs, double deltaSumTotal, Bias_Neuron bias, int numOfHiddenNeurons) {
        double gradient = deltaSumTotal * derivative(output);
        for (int i = 0; i < weights.length; i++) {
            weights[i] += learningRate * prevOutputs[i] * gradient;
            DELTA_SUM[i] = gradient * weights[i];
        }
        tuneBias(numOfHiddenNeurons, bias, learningRate, gradient);
    }
    
    /*
     * Calculates the output and assigns it to the variable 'output'
     */
    void calculateOutput(double[] inputs, double bias) {
        double weightedSum = getWeightedSum(inputs, weights);
        weightedSum += bias;
        this.output = sigmoid(weightedSum);
    }
    
    //Getters
    double[] getDeltaSum() {
        return DELTA_SUM;
    }
    
    double getOutput() {
        return output;
    }
    
    double[] getWeights() {
        return weights;
    }
}