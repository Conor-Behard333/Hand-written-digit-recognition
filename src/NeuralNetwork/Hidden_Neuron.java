package NeuralNetwork;

public class Hidden_Neuron {
    private double weightedSum;
    private double output;
    private double[] weights;
    private double[] deltaSum;
    private Function f = new Function();

    Hidden_Neuron(int previousLayer) {
        this.weights = new double[previousLayer];
        this.weights = f.randomiseWeights(weights.length);
        this.deltaSum = new double[previousLayer];
    }

    void tuneWeights(double LR, double[] prevOutputs, double deltaSumTotal) {
        double gradient = deltaSumTotal * f.derivative(output);
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

    void setOutput(double[] inputs) {
        this.weightedSum = f.getWeightedSum(inputs, weights);
        this.output = f.sigmoid(weightedSum);
    }
}