class Function {
    //for output
    double getWeightedSum(double[] inputs, double... weights) {
        // TODO: 19-Feb-19 Add bias
        double sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += weights[i] * inputs[i];
        }
        return sum;
    }

    //for hidden
    double getWeightedSum(int[] inputs, double... weights) {
        // TODO: 19-Feb-19 Add bias
        double sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += weights[i] * inputs[i];
        }
        return sum;
    }

    double[] setWeights(int size) {
        double[] arr = new double[size];
        for (int i = 0; i < size; i++) {
            arr[i] = Math.random() * 1;
        }
        return arr;
    }

    double sigmoid(double x) {
        return 1d / (1 + Math.exp(-x));
    }

    //((x * (1 - x)) = derivative of sigmoid
    double dSigmoid(double x) {
        return x * (1 - x);
    }
}