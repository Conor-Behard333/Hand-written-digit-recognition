package NeuralNetwork;

public class Network {
    private final int NUM_OF_INPUT_NEURONS;
    private final int NUM_OF_OUTPUT_NEURONS;
    private final int[] NUM_OF_HIDDEN_NEURONS;
    private final int NUM_OF_HIDDEN_LAYERS;
    private final int NUM_OF_BIAS_NEURONS;
    private final double LEARNING_RATE;
    private int[] networkConfig;
    private Hidden_Neuron[][] hiddenLayer;
    private Output_Neuron[] outputNeurons;
    private Bias_Neuron[] biasNeurons;
    private String config = "";
    
    /*
     * initialises all the global variables used and creates all the necessary neurons
     */
    public Network(double LEARNING_RATE, int inputNeurons, int outputNeurons, int[] hiddenNeurons) {
        NUM_OF_HIDDEN_LAYERS = hiddenNeurons.length;
        NUM_OF_INPUT_NEURONS = inputNeurons;
        NUM_OF_OUTPUT_NEURONS = outputNeurons;
        NUM_OF_BIAS_NEURONS = NUM_OF_HIDDEN_LAYERS + 1;
        this.outputNeurons = new Output_Neuron[outputNeurons];
        this.LEARNING_RATE = LEARNING_RATE;
        
        //stores how many neurons are in each hidden layer
        NUM_OF_HIDDEN_NEURONS = new int[NUM_OF_HIDDEN_LAYERS];
        System.arraycopy(hiddenNeurons, 0, NUM_OF_HIDDEN_NEURONS, 0, NUM_OF_HIDDEN_LAYERS);
        
        //creates all the hidden neurons for each hidden layer
        hiddenLayer = new Hidden_Neuron[NUM_OF_HIDDEN_LAYERS][];
        for (int i = 0; i < NUM_OF_HIDDEN_LAYERS; i++) {
            hiddenLayer[i] = new Hidden_Neuron[hiddenNeurons[i]];
        }
        
        setNetworkConfig();
        setConfig();
        
        createBiasNeurons();
        createHiddenNeurons();
        createOutputNeurons();
    }
    
    /*
     * Initialises all bias neurons in the network
     */
    private void createBiasNeurons() {
        biasNeurons = new Bias_Neuron[NUM_OF_BIAS_NEURONS];
        for (int i = 0; i < NUM_OF_BIAS_NEURONS; i++) {
            biasNeurons[i] = new Bias_Neuron(networkConfig[i + 1]);
        }
    }
    
    /*
     * Sets the network config (how many neurons in each layer)
     */
    private void setNetworkConfig() {
        networkConfig = new int[NUM_OF_HIDDEN_LAYERS + 2];
        int layer = 0;
        for (int i = 0; i < networkConfig.length; i++) {
            if (i == 0) {
                networkConfig[i] = NUM_OF_INPUT_NEURONS;
            } else if (i == networkConfig.length - 1) {
                networkConfig[i] = NUM_OF_OUTPUT_NEURONS;
            } else {
                networkConfig[i] = NUM_OF_HIDDEN_NEURONS[layer];
                layer++;
            }
        }
    }
    
    /*
     * Sets the config in string format
     */
    private void setConfig() {
        //format for txt file is (InputNeurons_HiddenNeuronsH_HiddenNeuronsH_OutputNeurons)[learning rate]
        config += "(" + NUM_OF_INPUT_NEURONS + "_";
        for (int i = 0; i < NUM_OF_HIDDEN_LAYERS; i++) {
            config = config.concat(NUM_OF_HIDDEN_NEURONS[i] + "H_");
        }
        config += NUM_OF_OUTPUT_NEURONS + ")[" + LEARNING_RATE + "]";
    }
    
    //getter or the variable config
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
        
        //Stage 1
        getHiddenLayerOutputs(inputs, hiddenLayer_outputs);
        
        //Stage 2
        calculateOutputWeightedSum(weightedSum, hiddenLayer_outputs[NUM_OF_HIDDEN_LAYERS - 1]);
        
        //Stage 3
        getOutputLayerOutputs(outputLayer_Outputs, weightedSum);
        return outputLayer_Outputs;
    }
    
    /*
     * calculates the outputs for each hidden neuron in each hidden layer
     */
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
     * Calculate the weighted sum for each output neuron in the output layer using the second hidden layer outputs
     */
    private void calculateOutputWeightedSum(double[] weightedSum, double[] hiddenLayer_output) {
        for (int i = 0; i < NUM_OF_OUTPUT_NEURONS; i++) {
            outputNeurons[i].calculateWeightedSum(hiddenLayer_output, biasNeurons[biasNeurons.length - 1].getOutput(i));
            weightedSum[i] = outputNeurons[i].getWeightedSum();
        }
    }
    
    /*
     * Calculate the output for each output neuron in the output layer using the second hidden layer outputs
     */
    private void getOutputLayerOutputs(double[] outputLayer_Outputs, double[] weightedSum) {
        for (int i = 0; i < NUM_OF_OUTPUT_NEURONS; i++) {
            outputNeurons[i].calculateOutput(weightedSum, i);
            outputLayer_Outputs[i] = outputNeurons[i].getOutput();
        }
    }
    
    /*
     * Trains the network with the training values using back propagation
     */
    public void train(double[] inputs, double[] target) {
        double[][] hidden_outputs = new double[NUM_OF_HIDDEN_LAYERS][];
        //Stage 1
        feedForward(inputs);
        
        //Stage 2
        getHiddenLayerOutputs(hidden_outputs);
        
        //Stage 3
        tuneOutputWeights(hidden_outputs[NUM_OF_HIDDEN_LAYERS - 1], target);
        
        if (NUM_OF_HIDDEN_LAYERS > 1) {
            //Stage 4
            tuneHiddenLayerWeights(hidden_outputs);
            
            //Stage 5
            tuneFirstHiddenLayer(inputs);
        } else {
            //Stage 5
            tuneOnlyHiddenLayer(inputs);
        }
        
    }
    
    /*
     * Tunes the hidden layers in-between the first hidden layer and the output layer
     */
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
    
    /*
     * Get hidden outputs for each layer
     */
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
    
    /*
     * Tunes the weights of second to last layer of the network
     */
    private void tunePenultimateLayer(double[] inputs, int layer) {
        for (int i = 0; i < NUM_OF_HIDDEN_NEURONS[layer]; i++) {
            double deltaSumTotal = 0;
            for (int j = 0; j < NUM_OF_OUTPUT_NEURONS; j++) {
                deltaSumTotal += outputNeurons[j].getDeltaSum()[i];
            }
            hiddenLayer[layer][i].tuneWeights(LEARNING_RATE, inputs, deltaSumTotal, biasNeurons[biasNeurons.length - 2], NUM_OF_HIDDEN_NEURONS[layer]);
        }
    }
    
    /*
     * Tunes the weights of layers in-between the input layer and the output layer
     */
    private void tuneInBetweenLayers(double[] inputs, int layer) {
        for (int i = 0; i < NUM_OF_HIDDEN_NEURONS[layer]; i++) {
            double deltaSumTotal = 0;
            for (int j = 0; j < NUM_OF_HIDDEN_NEURONS[layer + 1]; j++) {
                deltaSumTotal += hiddenLayer[layer + 1][j].getDeltaSum()[i];
            }
            hiddenLayer[layer][i].tuneWeights(LEARNING_RATE, inputs, deltaSumTotal, biasNeurons[1], NUM_OF_HIDDEN_NEURONS[layer]);
        }
    }
    
    /*
     * Tunes the first hidden layer's weights if there is more than 1 hidden layer
     */
    private void tuneFirstHiddenLayer(double[] inputs) {
        for (int i = 0; i < NUM_OF_HIDDEN_NEURONS[0]; i++) {
            double deltaSumTotal = 0;
            for (int j = 0; j < NUM_OF_HIDDEN_NEURONS[1]; j++) {
                deltaSumTotal += hiddenLayer[1][j].getDeltaSum()[i];
            }
            hiddenLayer[0][i].tuneWeights(LEARNING_RATE, inputs, deltaSumTotal, biasNeurons[0], NUM_OF_HIDDEN_NEURONS[0]);
        }
        
    }
    
    /*
     * Tunes the first hidden layer's weights if there is only 1 hidden layer
     */
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
     * Initialises hidden neurons
     */
    private void createHiddenNeurons() {
        //iterates through each hidden layer
        for (int i = 0; i < NUM_OF_HIDDEN_LAYERS; i++) {
            //iterates through each hidden neuron in the hidden layer
            for (int j = 0; j < NUM_OF_HIDDEN_NEURONS[i]; j++) {
                if (i == 0) {//The first hidden layer is the input layer
                    hiddenLayer[i][j] = new Hidden_Neuron(NUM_OF_INPUT_NEURONS);
                } else {
                    hiddenLayer[i][j] = new Hidden_Neuron(NUM_OF_HIDDEN_NEURONS[i - 1]);
                }
            }
        }
    }
    
    /*
     * Initialises output neurons
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
        largest = getLargest(outputs, largest);//gets the largest number out of the array
        for (int i = 0; i < outputs.length; i++) {
            if (outputs[i] == largest) {//returns the index of the largest number
                return i;
            }
        }
        return -1;
    }
    
    /*
     * Returns the largest value in the array
     */
    private double getLargest(double[] outputs, double largest) {
        for (double output : outputs) {
            //if output is greater than the current largest number then set the largest number to the output
            if (output >= largest) {
                largest = output;
            }
        }
        return largest;
    }
    
    /*
     * Stores all of the weights in the network into a
     * double array
     */
    public double[] getWeights() {
        int size = getTotalNumOfWeights();
        
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
    
    /*
     * Calculates and returns the number of weights in the network
     * */
    private int getTotalNumOfWeights() {
        int size = 0;
        //number of weights in a network is:
        // (neurons in layer[i] * neurons in next layer) + neurons in next layer for each layer
        for (int i = 0; i < networkConfig.length - 1; i++) {
            size += (networkConfig[i] * networkConfig[i + 1]) + networkConfig[i + 1];
        }
        return size;
    }
    
    /*
     * Sets the weights for each layer in the network
     * Procedure is called when the user is loading a network
     */
    public void setWeights(double[] weights) {
        int lastIndex = 0;
        for (int i = 0; i < networkConfig.length - 1; i++) {
            if (i == networkConfig.length - 2) {
                //sets the weights for the output layer
                lastIndex = setWeights(weights, lastIndex, networkConfig[i], i, networkConfig[i + 1], true);
            } else {
                //sets the weights for the rest of the layers
                lastIndex = setWeights(weights, lastIndex, networkConfig[i], i, networkConfig[i + 1], false);
            }
        }
        //sets the weights for the bias neurons
        setBiasWeights(weights, lastIndex);
    }
    
    /*
     * Iterates through the array of weights and sets the bias weights
     */
    private void setBiasWeights(double[] weights, int lastIndex) {
        for (int i = 0; i < biasNeurons.length; i++) {
            double[] temp = new double[networkConfig[i + 1]];
            for (int j = 0; j < temp.length; j++) {
                temp[j] = weights[lastIndex];
                lastIndex++;
            }
            biasNeurons[i].setWeights(temp);
        }
    }
    
    /*
     * Iterates through the array of weights and sets the weights for
     * the each hidden and output neuron
     */
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