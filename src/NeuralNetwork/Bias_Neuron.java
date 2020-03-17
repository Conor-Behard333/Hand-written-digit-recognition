package NeuralNetwork;

class Bias_Neuron extends Function {
    private double[] weights;
    
    /*Constructor to initialise the neuron*/
    Bias_Neuron(int neuronsInNextLayer) {
        this.weights = new double[neuronsInNextLayer];
        weights = randomiseWeights(neuronsInNextLayer);
    }
    
    //getters
    double getOutput(int index) {
        return weights[index];
    }
    
    double[] getWeights() {
        return weights;
    }
    
    //Setters
    void setWeight(double delta, int index) {
        weights[index] += delta;
    }
    
    void setWeights(double[] weights) {
        this.weights = weights;
    }
}