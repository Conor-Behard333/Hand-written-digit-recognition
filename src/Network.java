import java.util.Arrays;

class Network {
    private final double LR = 0.1;
    private int numOfInputNeurons;
    private int numOfHiddenNeurons;
    private int numOfOutputNeurons;

    private Hidden_Neuron[] hidden_neurons;
    private Output_Neuron[] output_neurons;

    Network(int inputNeurons, int hiddenNeurons, int outputNeurons) {
        this.numOfInputNeurons = inputNeurons;
        this.numOfHiddenNeurons = hiddenNeurons;
        this.numOfOutputNeurons = outputNeurons;

        this.hidden_neurons = new Hidden_Neuron[hiddenNeurons];
        this.output_neurons = new Output_Neuron[outputNeurons];

        createHiddenNeurons();
        createOutputNeurons();
    }

    double[] feedForward(int[] inputs) {
        Function f = new Function();
        double[] hidden_outputs = new double[numOfHiddenNeurons];
        double[] output_outputs = new double[numOfOutputNeurons];

        //passes inputs to hidden layer and gets their outputs
        for (int i = 0; i < numOfHiddenNeurons; i++) {
            hidden_neurons[i].setOutput(inputs);
            hidden_outputs[i] = hidden_neurons[i].getOutput();
        }
        //passes hidden outputs to output layer and gets their outputs
        for (int i = 0; i < numOfOutputNeurons; i++) {
            output_neurons[i].setWeightedSum(hidden_outputs);
        }
        double[] weightedSum = new double[numOfOutputNeurons];
        for (int i = 0; i < numOfOutputNeurons; i++) {
            weightedSum[i] = output_neurons[i].getWeightedSum();
        }

        for (int i = 0; i < numOfOutputNeurons; i++) {
            output_neurons[i].setOutput(f.softMax(weightedSum, i));
            output_outputs[i] = output_neurons[i].getOutput();
        }
        return output_outputs;
    }

    void train(int[] inputs, double[] target) {
        double[] outputs = feedForward(inputs);
        double error;// cross-entropy
        double[] hidden_outputs = new double[numOfHiddenNeurons];
        double sum = 0;
        for (int i = 0; i < numOfOutputNeurons; i++) {
            sum += target[i] * Math.log(outputs[i]) + (1 - target[i]) * (Math.log(1 - outputs[i]));
        }
        error = -(sum / numOfOutputNeurons);

        System.out.println("Error: " + error);

        for (int i = 0; i < numOfHiddenNeurons; i++) {
            hidden_outputs[i] = hidden_neurons[i].getOutput();
        }
        //tune output weights
        for (int i = 0; i < numOfOutputNeurons; i++) {
            output_neurons[i].tuneWeights(LR, hidden_outputs, target[i]);
        }
        //tune Hidden weights
        for (int i = 0; i < numOfHiddenNeurons; i++) {
            double weightedDeltaHiddenTotal = 0;
            for (int j = 0; j < numOfOutputNeurons; j++) {
                weightedDeltaHiddenTotal += output_neurons[j].getWeightedDeltaHidden()[i];
            }
            hidden_neurons[i].tuneWeights(LR, inputs, weightedDeltaHiddenTotal);
        }
    }

    private void createHiddenNeurons() {
        for (int i = 0; i < numOfHiddenNeurons; i++) {
            hidden_neurons[i] = new Hidden_Neuron(numOfInputNeurons);
        }
    }

    private void createOutputNeurons() {
        for (int i = 0; i < numOfOutputNeurons; i++) {
            output_neurons[i] = new Output_Neuron(numOfHiddenNeurons);
        }
    }

}