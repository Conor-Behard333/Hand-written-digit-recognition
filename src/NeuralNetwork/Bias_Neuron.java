package NeuralNetwork;

class Bias_Neuron extends Function {
    private double[] weights;

    /*Initialise the neuron*/
    Bias_Neuron(int neuronsInNextLayer) {
        this.weights = new double[neuronsInNextLayer];
        weights = randomiseWeights(neuronsInNextLayer);
    }

    void setWeight(double delta, int index) {
        weights[index] += delta;
    }

    double getOutput(int index) {
        return weights[index];
    }

    double[] getWeights() {
        return weights;
    }

    void setWeights(double[] weights) {
        this.weights = weights;
    }
}