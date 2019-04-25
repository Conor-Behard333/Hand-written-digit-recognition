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
        double[] outputLayer_Outputs = new double[numOfOutputNeurons];
        double[] weightedSum = new double[numOfOutputNeurons];

        //stores the outputs for each hidden output for each hidden layer
        double[][] hiddenLayer_outputs = new double[numberOfHiddenLayers][];
        for (int i = 0; i < numberOfHiddenLayers; i++) {
            hiddenLayer_outputs[i] = new double[numOfHiddenNeurons[i]];
        }

        //1.calculates the outputs for each hidden neuron in each hidden layer
        getHiddenLayerOutputs(inputs, hiddenLayer_outputs);

        //2. Calculate the weighted sum for each output neuron in the output layer using the second hidden layer outputs
        calculateOutputWeightedSum(weightedSum, hiddenLayer_outputs[numberOfHiddenLayers - 1]);

        //3. Calculate the output for each output neuron in the output layer using the second hidden layer outputs
        GetOutputLayerOutputs(outputLayer_Outputs, weightedSum);
        return outputLayer_Outputs;
    }

    private void GetOutputLayerOutputs(double[] outputLayer_Outputs, double[] weightedSum) {
        for (int i = 0; i < numOfOutputNeurons; i++) {
            output_neurons[i].calculateOutput(weightedSum, i);
            outputLayer_Outputs[i] = output_neurons[i].getOutput();
        }
    }

    private void calculateOutputWeightedSum(double[] weightedSum, double[] hiddenLayer_output) {
        for (int i = 0; i < numOfOutputNeurons; i++) {
            output_neurons[i].calculateWeightedSum(hiddenLayer_output);
            weightedSum[i] = output_neurons[i].getWeightedSum();
        }
    }

    private void getHiddenLayerOutputs(double[] inputs, double[][] hiddenLayer_outputs) {
        for (int i = 0; i < numberOfHiddenLayers; i++) {
            for (int j = 0; j < numOfHiddenNeurons[i]; j++) {
                hiddenLayer[i][j].calculateOutput(inputs);
                hiddenLayer_outputs[i][j] = hiddenLayer[i][j].getOutput();
            }
            inputs = hiddenLayer_outputs[i];
        }
    }

    /*
     * Trains the network with the training values using back propagation
     */
    public void train(double[] inputs, double[] target) {
        double[][] hidden_outputs = new double[numberOfHiddenLayers][];
        //1. Feeds the inputs through the network
        feedForward(inputs);

        //get hidden outputs for each layer
        getHiddenLayerOutputs(hidden_outputs);

        //2. Tunes the output weights
        tuneOutputWeights(hidden_outputs[numberOfHiddenLayers - 1], target);

        //3. Tunes the hidden layers in-between the first hidden layer and the output layer
        tuneInBetweenHiddenLayerWeights(hidden_outputs);

        //4. Tunes the first hidden layer's weights
        tuneFirstLayer(inputs);
    }

    private void tuneInBetweenHiddenLayerWeights(double[][] hidden_outputs) {
        for (int i = numberOfHiddenLayers - 2; i > 0; i--) {
            tuneInBetweenLayers(hidden_outputs[i - 1], i);
        }
    }

    private void getHiddenLayerOutputs(double[][] hidden_outputs) {
        for (int i = 0; i < numberOfHiddenLayers; i++) {
            hidden_outputs[i] = getHiddenOutputsLayer(i);
        }
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

    private void tuneInBetweenLayers(double[] inputs, int layer) {
        for (int i = 0; i < numOfHiddenNeurons[layer]; i++) {
            double deltaSumTotal = 0;
            for (int j = 0; j < numOfHiddenNeurons[layer + 1]; j++) {
                deltaSumTotal += hiddenLayer[layer + 1][j].getDeltaSum()[i];
            }
            hiddenLayer[layer][i].tuneWeights(LR, inputs, deltaSumTotal);
        }
    }

    private void tuneFirstLayer(double[] inputs) {
        for (int i = 0; i < numOfHiddenNeurons[0]; i++) {
            double deltaSumTotal = 0;
            for (int j = 0; j < numOfOutputNeurons; j++) {
                deltaSumTotal += output_neurons[j].getDeltaSum()[i];
            }
            hiddenLayer[0][i].tuneWeights(LR, inputs, deltaSumTotal);
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
        largest = getLargest(outputs, largest);
        for (int i = 0; i < outputs.length; i++) {
            if (outputs[i] == largest) {
                return i;
            }
        }
        return -1;
    }

    private double getLargest(double[] outputs, double largest) {
        for (double output : outputs) {
            if (output >= largest) {
                largest = output;
            }
        }
        return largest;
    }
}