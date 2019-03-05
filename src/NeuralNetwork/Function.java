package NeuralNetwork;

public class Function {
    double getWeightedSum(double[] inputs, double... weights) {
        double sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += weights[i] * inputs[i];
        }
        return sum;
    }

    double[] randomiseWeights(int size) {
        double[] arr = new double[size];
        for (int i = 0; i < size; i++) {
            arr[i] = Math.random() * 2 - 1;
        }
        return arr;
    }

    double softMax(double[] weightedSums, int neuron) {
        double[] output = new double[weightedSums.length];
        double sum = 0;
        for (double weight : weightedSums) {
            sum += Math.exp(weight);
        }
        for (int i = 0; i < output.length; i++) {
            output[i] = Math.exp(weightedSums[i]) / sum;
        }
        return output[neuron];
    }

    public double[] normalise(double[] x) {
        int min = 0;
        double max = getMax(x);
        double[] y = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            y[i] = (x[i] - min) / (max - min);
        }
        return y;
    }

    private double getMax(double[] values) {
        double max = 0;
        for (double value : values) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    double sigmoid(double x) {
        return 1d / (1 + Math.exp(-x));
    }

    double derivative(double x) {
        return x * (1 - x);
    }
}