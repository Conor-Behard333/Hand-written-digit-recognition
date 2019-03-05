package NeuralNetwork;

public class Network {
    private final double LR = 0.05;
    private int numOfInputNeurons;
    private int numOfHiddenNeurons_layer_1;
    private int numOfHiddenNeurons_layer_2;
    private int numOfOutputNeurons;

    private Hidden_Neuron[] hiddenLayer_1;
    private Hidden_Neuron[] hiddenLayer_2;
    private Output_Neuron[] output_neurons;

    public Network(int inputNeurons, int hiddenNeurons_layer_1, int hiddenNeurons_layer_2, int outputNeurons) {
        this.numOfInputNeurons = inputNeurons;
        this.numOfHiddenNeurons_layer_1 = hiddenNeurons_layer_1;
        this.numOfHiddenNeurons_layer_2 = hiddenNeurons_layer_2;
        this.numOfOutputNeurons = outputNeurons;

        this.hiddenLayer_1 = new Hidden_Neuron[hiddenNeurons_layer_1];
        this.hiddenLayer_2 = new Hidden_Neuron[hiddenNeurons_layer_2];
        this.output_neurons = new Output_Neuron[outputNeurons];

        createHiddenNeurons1();
        createHiddenNeurons();
        createOutputNeurons();
    }

    public double[] feedForward(double[] inputs) {
        Function f = new Function();
        double[] hidden_outputs_layer_1 = new double[numOfHiddenNeurons_layer_1];
        double[] hidden_outputs_layer_2 = new double[numOfHiddenNeurons_layer_2];
        double[] output_outputs = new double[numOfOutputNeurons];

        for (int i = 0; i < numOfHiddenNeurons_layer_1; i++) {
            hiddenLayer_1[i].setOutput(inputs);
            hidden_outputs_layer_1[i] = hiddenLayer_1[i].getOutput();
        }

        for (int i = 0; i < numOfHiddenNeurons_layer_2; i++) {
            hiddenLayer_2[i].setOutput(hidden_outputs_layer_1);
            hidden_outputs_layer_2[i] = hiddenLayer_2[i].getOutput();
        }

        for (int i = 0; i < numOfOutputNeurons; i++) {
            output_neurons[i].setWeightedSum(hidden_outputs_layer_2);
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

    public void train(double[] inputs, double[] target) {
        feedForward(inputs);
        //back propagation
        //get hidden outputs for each layer
        double[] hidden_outputs_layer_1 = getHiddenOutputsLayer1();
        double[] hidden_outputs_layer_2 = getHiddenOutputsLayer2();
        //tune output weights
        tuneOutputWeights(hidden_outputs_layer_2, target);
        //tune Hidden weights (layer 2)
        tuneHiddenWeightsLayer2(hidden_outputs_layer_1);
        //tune Hidden weights (layer 1)
        tuneHiddenWeightsLayer1(inputs);
    }

    private double[] getHiddenOutputsLayer1() {
        double[] x = new double[numOfHiddenNeurons_layer_1];
        for (int i = 0; i < numOfHiddenNeurons_layer_1; i++) {
            x[i] = hiddenLayer_1[i].getOutput();
        }
        return x;
    }

    private double[] getHiddenOutputsLayer2() {
        double[] x = new double[numOfHiddenNeurons_layer_2];
        for (int i = 0; i < numOfHiddenNeurons_layer_2; i++) {
            x[i] = hiddenLayer_2[i].getOutput();
        }
        return x;
    }

    private void tuneHiddenWeightsLayer1(double[] inputs) {
        for (int i = 0; i < numOfHiddenNeurons_layer_1; i++) {
            double deltaSumTotal = 0;
            for (int j = 0; j < numOfHiddenNeurons_layer_2; j++) {
                deltaSumTotal += hiddenLayer_2[j].getDeltaSum()[i];
            }
            hiddenLayer_1[i].tuneWeights(LR, inputs, deltaSumTotal);
        }
    }

    private void tuneHiddenWeightsLayer2(double[] hidden_outputs) {
        for (int i = 0; i < numOfHiddenNeurons_layer_2; i++) {
            double deltaSumTotal = 0;
            for (int j = 0; j < numOfOutputNeurons; j++) {
                deltaSumTotal += output_neurons[j].getDeltaSum()[i];
            }
            hiddenLayer_2[i].tuneWeights(LR, hidden_outputs, deltaSumTotal);
        }
    }

    private void tuneOutputWeights(double[] hidden_outputs, double[] target) {
        for (int i = 0; i < numOfOutputNeurons; i++) {
            output_neurons[i].tuneWeights(LR, hidden_outputs, target[i]);
        }
    }

    private void createHiddenNeurons1() {
        for (int i = 0; i < numOfHiddenNeurons_layer_1; i++) {
            hiddenLayer_1[i] = new Hidden_Neuron(numOfInputNeurons);
        }
    }

    private void createHiddenNeurons() {
        for (int i = 0; i < numOfHiddenNeurons_layer_2; i++) {
            hiddenLayer_2[i] = new Hidden_Neuron(numOfHiddenNeurons_layer_1);
        }
    }

    private void createOutputNeurons() {
        for (int i = 0; i < numOfOutputNeurons; i++) {
            output_neurons[i] = new Output_Neuron(numOfHiddenNeurons_layer_2);
        }
    }

    public int getGuess(double[] outputs) {
        double largest = 0;
        for (double output : outputs) {
            if (output >= largest) {
                largest = output;
            }
        }
        for (int i = 0; i < outputs.length; i++) {
            if (outputs[i] == largest) {
                return i;
            }
        }
        return -1;
    }
}