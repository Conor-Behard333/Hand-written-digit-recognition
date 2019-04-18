package NeuralNetwork;

public class Function {
    /*
     * Calculates and returns the weighted sum for a neuron
     *
     * Weighted sum = Σ(weight[i] * inputs[i]))
     */
    double getWeightedSum(double[] inputs, double... weights) {
        double sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += weights[i] * inputs[i];
        }
        return sum;
    }

    /*
     * Returns a double array filled with random decimals
     * between -1 and 1
     */
    double[] randomiseWeights(int size) {
        double[] arr = new double[size];
        for (int i = 0; i < size; i++) {
            arr[i] = Math.random() * 2 - 1;
        }
        return arr;
    }

    /*
     * Returns the output of the softMax function for a specific neuron:
     * output = (e^weightedSum[i]) / Σ(e^weightedSum[i])
     */
    double softMax(double[] weightedSums, int neuron) {
        double[] output = new double[weightedSums.length];
        double sum = 0;
        for (double weightedSum : weightedSums) {
            sum += Math.exp(weightedSum);
        }
        for (int i = 0; i < output.length; i++) {
            output[i] = Math.exp(weightedSums[i]) / sum;
        }
        return output[neuron];
    }

    /*
     * Returns a double array which contains normalised values
     *
     * Normalisation function:
     * normalised number = (number - minimum)/(maximum - minimum)
     */
    public double[] normalise(double[] x) {
        int min = 0;
        double max = getMax(x);
        double[] y = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            y[i] = (x[i] - min) / (max - min);
        }
        return y;
    }

    /*
     * Returns the maximum value within an array
     */
    private double getMax(double[] values) {
        double max = 0;
        for (double value : values) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    /*
     * Returns the value of x having gone through
     * the sigmoid function
     */
    double sigmoid(double x) {
        return 1d / (1 + Math.exp(-x));
    }

    /*
     * Returns the value of x having gone through
     * the derivative of sigmoid and soft max function
     */
    double derivative(double x) {
        return x * (1 - x);
    }
}