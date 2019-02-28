class Function {
    //for output
    double getWeightedSum(double[] inputs, double... weights) {
        double sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += weights[i] * inputs[i];
        }
        return sum;
    }

    double[] setWeights(int size) {
        double[] arr = new double[size];
        for (int i = 0; i < size; i++) {
            arr[i] = Math.random() * 2 - 1;
        }
        return arr;
    }

    double sigmoid(double x) {
        return 1d / (1 + Math.exp(-x));
    }

    double derivative(double x) {
        return x * (1 - x);
    }

    double softMax(double[] weightedSums, int neuron) {
        double[] output = new double[weightedSums.length];
        double sum = 0;
        for (int i = 0; i < weightedSums.length; i++) {
            sum += Math.exp(weightedSums[i]);
        }
        for (int i = 0; i < output.length; i++) {
            output[i] = Math.exp(weightedSums[i]) / sum;
        }
        return output[neuron];
    }
}