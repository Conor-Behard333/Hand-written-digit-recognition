package NeuralNetwork;

public class Network {
    private final double LR = 0.07;
    private int numOfInputNeurons;
    private int numOfOutputNeurons;
    private int[] numOfHiddenNeurons;
    private int numberOfHiddenLayers;
    private Hidden_Neuron[][] hiddenLayer;
    private Output_Neuron[] output_neurons;

    /*
     * initialises all the global variables used and creates all the necessary neurons
     */
    public Network(int inputNeurons, int outputNeurons, int... hiddenNeurons) {
        this.numberOfHiddenLayers = hiddenNeurons.length;
        this.numOfInputNeurons = inputNeurons;
        this.numOfOutputNeurons = outputNeurons;

        //stores how many neurons are in each hidden layer
        this.numOfHiddenNeurons = new int[numberOfHiddenLayers];
        for (int i = 0; i < numberOfHiddenLayers; i++) {
            numOfHiddenNeurons[i] = hiddenNeurons[i];
        }

        this.output_neurons = new Output_Neuron[outputNeurons];
        //stores all the hidden neurons for each hidden layer
        this.hiddenLayer = new Hidden_Neuron[numberOfHiddenLayers][];
        for (int i = 0; i < numberOfHiddenLayers; i++) {
            hiddenLayer[i] = new Hidden_Neuron[hiddenNeurons[i]];
        }

        createHiddenNeurons();//creates all the hidden neurons
        createOutputNeurons();//creates all the output neurons
    }

    /*
     * Puts the inputs into the network and feeds it through
     */
    public double[] feedForward(double[] inputs) {
        double[] output_outputs = new double[numOfOutputNeurons];
        double[] weightedSum = new double[numOfOutputNeurons];

        //stores the outputs for each hidden output for each hidden layer
        double[][] hidden_outputs = new double[numberOfHiddenLayers][];
        for (int i = 0; i < numberOfHiddenLayers; i++) {
            hidden_outputs[i] = new double[numOfHiddenNeurons[i]];
        }

        //1.calculates the outputs for each hidden neuron in each hidden layer
        for (int i = 0; i < numberOfHiddenLayers; i++) {
            for (int j = 0; j < numOfHiddenNeurons[i]; j++) {
                hiddenLayer[i][j].calculateOutput(inputs);
                hidden_outputs[i][j] = hiddenLayer[i][j].getOutput();
            }
            inputs = hidden_outputs[i];
        }

        //2. Calculate the weighted sum for each output neuron in the output layer using the second hidden layer outputs
        for (int i = 0; i < numOfOutputNeurons; i++) {
            output_neurons[i].calculateWeightedSum(hidden_outputs[numberOfHiddenLayers - 1]);
            weightedSum[i] = output_neurons[i].getWeightedSum();
        }

        //3. Calculate the output for each output neuron in the output layer using the second hidden layer outputs
        for (int i = 0; i < numOfOutputNeurons; i++) {
            output_neurons[i].calculateOutput(weightedSum, i);
            output_outputs[i] = output_neurons[i].getOutput();
        }
        return output_outputs;
    }

    /*
     * Trains the network with the training values using back propagation
     */
    public void train(double[] inputs, double[] target) {
        double[][] hidden_outputs = new double[numberOfHiddenLayers][];
        //1. Feeds the inputs through the network
        feedForward(inputs);

        //get hidden outputs for each layer
        for (int i = 0; i < numberOfHiddenLayers; i++) {
            hidden_outputs[i] = getHiddenOutputsLayer(i);
        }

        //2. Tunes the output weights
        tuneOutputWeights(hidden_outputs[numberOfHiddenLayers - 1], target);

        //3. Tunes the hidden layers in-between the first hidden layer and the output layer
        for (int i = numberOfHiddenLayers - 1; i > 0; i--) {
            tuneHiddenWeightsLayer(hidden_outputs[i - 1], i);
        }

        //4. Tunes the first hidden layer's weights
        tuneHiddenWeightsLayer(inputs, 0);
    }

    /*
     * Returns an array of the outputs for each neuron in a given hidden layer
     */
    private double[] getHiddenOutputsLayer(int layer) {
        double[] x = new double[numOfHiddenNeurons[layer]];
        for (int i = 0; i < numOfHiddenNeurons[layer]; i++) {
            x[i] = hiddenLayer[layer][i].getOutput();
        }
        return x;
    }

    /*
     * Tunes the weights for the hidden layers
     */
    private void tuneHiddenWeightsLayer(double[] inputs, int layer) {
        if (numberOfHiddenLayers == layer + 1) {
            for (int i = 0; i < numOfHiddenNeurons[layer]; i++) {
                double deltaSumTotal = 0;
                for (int j = 0; j < numOfOutputNeurons; j++) {
                    deltaSumTotal += output_neurons[j].getDeltaSum()[i];
                }
                hiddenLayer[layer][i].tuneWeights(LR, inputs, deltaSumTotal);
            }
        } else {
            for (int i = 0; i < numOfHiddenNeurons[layer]; i++) {
                double deltaSumTotal = 0;
                for (int j = 0; j < numOfHiddenNeurons[layer + 1]; j++) {
                    deltaSumTotal += hiddenLayer[layer + 1][j].getDeltaSum()[i];
                }
                hiddenLayer[layer][i].tuneWeights(LR, inputs, deltaSumTotal);
            }
        }
    }

    /*
     * Tunes the weights of the output layer
     */
    private void tuneOutputWeights(double[] hidden_outputs, double[] target) {
        for (int i = 0; i < numOfOutputNeurons; i++) {
            output_neurons[i].tuneWeights(LR, hidden_outputs, target[i]);
        }
    }

    /*
     * Creates hidden neurons
     */
    private void createHiddenNeurons() {
        for (int i = 0; i < numberOfHiddenLayers; i++) {
            for (int j = 0; j < numOfHiddenNeurons[i]; j++) {
                if (i == 0) {
                    hiddenLayer[i][j] = new Hidden_Neuron(numOfInputNeurons);
                } else {
                    hiddenLayer[i][j] = new Hidden_Neuron(numOfHiddenNeurons[i - 1]);
                }
            }
        }
    }

    /*
     * Creates output neurons
     */
    private void createOutputNeurons() {
        for (int i = 0; i < numOfOutputNeurons; i++) {
            output_neurons[i] = new Output_Neuron(numOfHiddenNeurons[numberOfHiddenLayers - 1]);
        }
    }

    /*
     * Converts the networks' outputs into a guess by returning the index of
     * the largest number in the array (the index represents which number it guess i.e. index = 2 guess = 2)
     */
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