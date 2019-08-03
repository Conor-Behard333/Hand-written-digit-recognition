package NeuralNetwork;

public class Network {
    private final double LEARNING_RATE = 0.14;
    private final int NUM_OF_INPUT_NEURONS;
    private final int NUM_OF_OUTPUT_NEURONS;
    private final int[] NUM_OF_HIDDEN_NEURONS;
    private final int NUM_OF_HIDDEN_LAYERS;
    private final int NUM_OF_BIAS_NEURONS;
    private int[] NETWORK_CONFIG;
    private Hidden_Neuron[][] hiddenLayer;
    private Output_Neuron[] outputNeurons;
    private Bias_Neuron[] biasNeurons;
    private String config = "";

    /*
     * initialises all the global variables used and creates all the necessary neurons
     */
    public Network(int inputNeurons, int outputNeurons, int[] hiddenNeurons) {
        this.NUM_OF_HIDDEN_LAYERS = hiddenNeurons.length;
        this.NUM_OF_INPUT_NEURONS = inputNeurons;
        this.NUM_OF_OUTPUT_NEURONS = outputNeurons;
        this.NUM_OF_BIAS_NEURONS = NUM_OF_HIDDEN_LAYERS + 1;
        //stores how many neurons are in each hidden layer
        this.NUM_OF_HIDDEN_NEURONS = new int[NUM_OF_HIDDEN_LAYERS];
        System.arraycopy(hiddenNeurons, 0, NUM_OF_HIDDEN_NEURONS, 0, NUM_OF_HIDDEN_LAYERS);

        this.outputNeurons = new Output_Neuron[outputNeurons];
        //stores all the hidden neurons for each hidden layer
        this.hiddenLayer = new Hidden_Neuron[NUM_OF_HIDDEN_LAYERS][];
        for (int i = 0; i < NUM_OF_HIDDEN_LAYERS; i++) {
            hiddenLayer[i] = new Hidden_Neuron[hiddenNeurons[i]];
        }

        setNetworkConfig();
        setConfig();
        createBiasNeurons();
        createHiddenNeurons();//creates all the hidden neurons
        createOutputNeurons();//creates all the output neurons
    }

    private void createBiasNeurons() {
        biasNeurons = new Bias_Neuron[NUM_OF_BIAS_NEURONS];//+1 is the output layer
        for (int i = 0; i < NUM_OF_BIAS_NEURONS; i++) {
            biasNeurons[i] = new Bias_Neuron(NETWORK_CONFIG[i + 1]);
        }
    }

    private void setNetworkConfig() {
        NETWORK_CONFIG = new int[NUM_OF_HIDDEN_LAYERS + 2];
        int layer = 0;
        for (int i = 0; i < NETWORK_CONFIG.length; i++) {
            if (i == 0) {
                NETWORK_CONFIG[i] = NUM_OF_INPUT_NEURONS;
            } else if (i == NETWORK_CONFIG.length - 1) {
                NETWORK_CONFIG[i] = NUM_OF_OUTPUT_NEURONS;
            } else {
                NETWORK_CONFIG[i] = NUM_OF_HIDDEN_NEURONS[layer];
                layer++;
            }
        }
    }

    private void setConfig() {
        config += NUM_OF_INPUT_NEURONS + "-";
        for (int i = 0; i < NUM_OF_HIDDEN_LAYERS; i++) {
            config += NUM_OF_HIDDEN_NEURONS[i] + "-";
        }
        config += NUM_OF_OUTPUT_NEURONS + "";
    }

    public String getConfig() {
        return config;
    }

    /*
     * Puts the inputs into the network and feeds it through
     */
    public double[] feedForward(double[] inputs) {
        double[] outputLayer_Outputs = new double[NUM_OF_OUTPUT_NEURONS];
        double[] weightedSum = new double[NUM_OF_OUTPUT_NEURONS];

        //stores the outputs for each hidden output for each hidden layer
        double[][] hiddenLayer_outputs = new double[NUM_OF_HIDDEN_LAYERS][];
        for (int i = 0; i < NUM_OF_HIDDEN_LAYERS; i++) {
            hiddenLayer_outputs[i] = new double[NUM_OF_HIDDEN_NEURONS[i]];
        }

        //1.calculates the outputs for each hidden neuron in each hidden layer
        getHiddenLayerOutputs(inputs, hiddenLayer_outputs);

        //2. Calculate the weighted sum for each output neuron in the output layer using the second hidden layer outputs
        calculateOutputWeightedSum(weightedSum, hiddenLayer_outputs[NUM_OF_HIDDEN_LAYERS - 1]);

        //3. Calculate the output for each output neuron in the output layer using the second hidden layer outputs
        getOutputLayerOutputs(outputLayer_Outputs, weightedSum);
        return outputLayer_Outputs;
    }

    private void getOutputLayerOutputs(double[] outputLayer_Outputs, double[] weightedSum) {
        for (int i = 0; i < NUM_OF_OUTPUT_NEURONS; i++) {
            outputNeurons[i].calculateOutput(weightedSum, i);
            outputLayer_Outputs[i] = outputNeurons[i].getOutput();
        }
    }

    private void calculateOutputWeightedSum(double[] weightedSum, double[] hiddenLayer_output) {
        for (int i = 0; i < NUM_OF_OUTPUT_NEURONS; i++) {
            outputNeurons[i].calculateWeightedSum(hiddenLayer_output, biasNeurons[biasNeurons.length - 1].getOutput(i));
            weightedSum[i] = outputNeurons[i].getWeightedSum();
        }
    }

    private void getHiddenLayerOutputs(double[] inputs, double[][] hiddenLayer_outputs) {
        for (int i = 0; i < NUM_OF_HIDDEN_LAYERS; i++) {
            for (int j = 0; j < NUM_OF_HIDDEN_NEURONS[i]; j++) {

                hiddenLayer[i][j].calculateOutput(inputs, biasNeurons[i].getOutput(i));
                hiddenLayer_outputs[i][j] = hiddenLayer[i][j].getOutput();
            }
            inputs = hiddenLayer_outputs[i];
        }
    }

    /*
     * Trains the network with the training values using back propagation
     */
    public void train(double[] inputs, double[] target) {
        double[][] hidden_outputs = new double[NUM_OF_HIDDEN_LAYERS][];
        //1. Feeds the inputs through the network
        feedForward(inputs);

        //get hidden outputs for each layer
        getHiddenLayerOutputs(hidden_outputs);

        //2. Tunes the output weights
        tuneOutputWeights(hidden_outputs[NUM_OF_HIDDEN_LAYERS - 1], target);

        if (NUM_OF_HIDDEN_LAYERS > 1) {
            //3. Tunes the hidden layers in-between the first hidden layer and the output layer
            tuneHiddenLayerWeights(hidden_outputs);

            //4. Tunes the first hidden layer's weights if there is more than 1 hidden layer
            tuneFirstHiddenLayer(inputs);
        } else {
            //4. Tunes the first hidden layer's weights if there is only 1 hidden layer
            tuneOnlyHiddenLayer(inputs);
        }

    }

    private void tuneHiddenLayerWeights(double[][] hidden_outputs) {
        int layersInBetween = NUM_OF_HIDDEN_LAYERS - 1;
        for (int i = NUM_OF_HIDDEN_LAYERS - 1; i > 0; i--) {
            if (i == layersInBetween) {
                tunePenultimateLayer(hidden_outputs[i - 1], i);
            } else {
                tuneInBetweenLayers(hidden_outputs[i - 1], i);
            }
        }
    }

    private void getHiddenLayerOutputs(double[][] hidden_outputs) {
        for (int i = 0; i < NUM_OF_HIDDEN_LAYERS; i++) {
            hidden_outputs[i] = getHiddenOutputs(i);
        }
    }

    /*
     * Returns an array of the outputs for each neuron in a given hidden layer
     */
    private double[] getHiddenOutputs(int layer) {
        double[] x = new double[NUM_OF_HIDDEN_NEURONS[layer]];
        for (int i = 0; i < NUM_OF_HIDDEN_NEURONS[layer]; i++) {
            x[i] = hiddenLayer[layer][i].getOutput();
        }
        return x;
    }

    private void tunePenultimateLayer(double[] inputs, int layer) {
        for (int i = 0; i < NUM_OF_HIDDEN_NEURONS[layer]; i++) {
            double deltaSumTotal = 0;
            for (int j = 0; j < NUM_OF_OUTPUT_NEURONS; j++) {
                deltaSumTotal += outputNeurons[j].getDeltaSum()[i];
            }
            hiddenLayer[layer][i].tuneWeights(LEARNING_RATE, inputs, deltaSumTotal, biasNeurons[biasNeurons.length - 2], NUM_OF_HIDDEN_NEURONS[layer]);
        }
    }

    private void tuneInBetweenLayers(double[] inputs, int layer) {
        for (int i = 0; i < NUM_OF_HIDDEN_NEURONS[layer]; i++) {
            double deltaSumTotal = 0;
            for (int j = 0; j < NUM_OF_HIDDEN_NEURONS[layer + 1]; j++) {
                deltaSumTotal += hiddenLayer[layer + 1][j].getDeltaSum()[i];
            }
            hiddenLayer[layer][i].tuneWeights(LEARNING_RATE, inputs, deltaSumTotal, biasNeurons[1], NUM_OF_HIDDEN_NEURONS[layer]);
        }
    }

    private void tuneFirstHiddenLayer(double[] inputs) {
        for (int i = 0; i < NUM_OF_HIDDEN_NEURONS[0]; i++) {
            double deltaSumTotal = 0;
            for (int j = 0; j < NUM_OF_HIDDEN_NEURONS[1]; j++) {
                deltaSumTotal += hiddenLayer[1][j].getDeltaSum()[i];
            }
            hiddenLayer[0][i].tuneWeights(LEARNING_RATE, inputs, deltaSumTotal, biasNeurons[0], NUM_OF_HIDDEN_NEURONS[0]);
        }

    }

    private void tuneOnlyHiddenLayer(double[] inputs) {
        for (int i = 0; i < NUM_OF_HIDDEN_NEURONS[0]; i++) {
            double deltaSumTotal = 0;
            for (int j = 0; j < NUM_OF_OUTPUT_NEURONS; j++) {
                deltaSumTotal += outputNeurons[j].getDeltaSum()[i];
            }
            hiddenLayer[0][i].tuneWeights(LEARNING_RATE, inputs, deltaSumTotal, biasNeurons[0], NUM_OF_HIDDEN_NEURONS[0]);
        }
    }

    /*
     * Tunes the weights of the output layer
     */
    private void tuneOutputWeights(double[] hidden_outputs, double[] target) {
        for (int i = 0; i < NUM_OF_OUTPUT_NEURONS; i++) {
            outputNeurons[i].tuneWeights(LEARNING_RATE, hidden_outputs, target[i], biasNeurons[biasNeurons.length - 1], NUM_OF_OUTPUT_NEURONS);
        }
    }

    /*
     * Creates hidden neurons
     */
    private void createHiddenNeurons() {
        for (int i = 0; i < NUM_OF_HIDDEN_LAYERS; i++) {
            for (int j = 0; j < NUM_OF_HIDDEN_NEURONS[i]; j++) {
                if (i == 0) {
                    hiddenLayer[i][j] = new Hidden_Neuron(NUM_OF_INPUT_NEURONS);
                } else {
                    hiddenLayer[i][j] = new Hidden_Neuron(NUM_OF_HIDDEN_NEURONS[i - 1]);
                }
            }
        }
    }

    /*
     * Creates output neurons
     */
    private void createOutputNeurons() {
        for (int i = 0; i < NUM_OF_OUTPUT_NEURONS; i++) {
            outputNeurons[i] = new Output_Neuron(NUM_OF_HIDDEN_NEURONS[NUM_OF_HIDDEN_LAYERS - 1]);
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

    public double[] getWeights() {
        int size = 0;
        for (int i = 0; i < NETWORK_CONFIG.length - 1; i++) {
            size += (NETWORK_CONFIG[i] * NETWORK_CONFIG[i + 1]) + NETWORK_CONFIG[i + 1];
        }

        double[] weights = new double[size];
        int index = 0;
        for (int i = 0; i < NUM_OF_HIDDEN_NEURONS.length; i++) {
            for (int j = 0; j < NUM_OF_HIDDEN_NEURONS[i]; j++) {
                double[] temp = hiddenLayer[i][j].getWeights();
                for (double weight : temp) {
                    weights[index] = weight;
                    index++;
                }
            }
        }

        for (int i = 0; i < NUM_OF_OUTPUT_NEURONS; i++) {
            double[] temp = outputNeurons[i].getWeights();
            for (double weight : temp) {
                weights[index] = weight;
                index++;
            }
        }

        for (Bias_Neuron biasNeuron : biasNeurons) {
            double[] temp = biasNeuron.getWeights();
            for (double weight : temp) {
                weights[index] = weight;
                index++;
            }
        }
        return weights;
    }

    public void setWeights(double[] weights) {
        int lastIndex = 0;
        for (int i = 0; i < NETWORK_CONFIG.length - 1; i++) {
            if (i == NETWORK_CONFIG.length - 2) {
                lastIndex = setWeights(weights, lastIndex, NETWORK_CONFIG[i], i, NETWORK_CONFIG[i + 1], true);
            } else {
                lastIndex = setWeights(weights, lastIndex, NETWORK_CONFIG[i], i, NETWORK_CONFIG[i + 1], false);
            }
        }
        setBiasWeights(weights, lastIndex);
    }

    private void setBiasWeights(double[] weights, int lastIndex) {
        for (int i = 0; i < biasNeurons.length; i++) {
            double[] temp = new double[NETWORK_CONFIG[i + 1]];
            for (int j = 0; j < temp.length; j++) {
                temp[j] = weights[lastIndex];
                lastIndex++;
            }
            biasNeurons[i].setWeights(temp);
        }
    }

    private int setWeights(double[] weights, int lastIndex, int prevLayer, int layer, int totalNeurons, boolean outputLayer) {
        int index = 0;
        for (int neuron = 0; neuron < totalNeurons; neuron++) {
            double[] temp = new double[prevLayer];
            for (int j = 0; j < temp.length; j++) {
                temp[index] = weights[lastIndex];
                index++;
                lastIndex++;
            }
            if (outputLayer) {
                outputNeurons[neuron].setWeights(temp);
            } else {
                hiddenLayer[layer][neuron].setWeights(temp);
            }
            index = 0;
        }
        return lastIndex;
    }
}
