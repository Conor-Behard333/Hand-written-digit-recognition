package NeuralNetwork;

public class Network {
    private final double LR = 0.07;
    private int numOfInputNeurons;
    private int numOfOutputNeurons;
    private int[] numOfHiddenNeurons;
    private int numOfHiddenLayers;
    private Hidden_Neuron[][] hiddenLayer;
    private Output_Neuron[] outputNeurons;
    private String config = "";

    /*
     * initialises all the global variables used and creates all the necessary neurons
     */
    public Network(int inputNeurons, int outputNeurons, int... hiddenNeurons) {

        this.numOfHiddenLayers = hiddenNeurons.length;
        this.numOfInputNeurons = inputNeurons;
        this.numOfOutputNeurons = outputNeurons;

        //stores how many neurons are in each hidden layer
        this.numOfHiddenNeurons = new int[numOfHiddenLayers];
        for (int i = 0; i < numOfHiddenLayers; i++) {
            numOfHiddenNeurons[i] = hiddenNeurons[i];
        }

        this.outputNeurons = new Output_Neuron[outputNeurons];
        //stores all the hidden neurons for each hidden layer
        this.hiddenLayer = new Hidden_Neuron[numOfHiddenLayers][];
        for (int i = 0; i < numOfHiddenLayers; i++) {
            hiddenLayer[i] = new Hidden_Neuron[hiddenNeurons[i]];
        }
        setConfig();
        createHiddenNeurons();//creates all the hidden neurons
        createOutputNeurons();//creates all the output neurons
    }

    public void setConfig() {
        config += numOfInputNeurons + "-";
        for (int i = 0; i < numOfHiddenLayers; i++) {
            config += numOfHiddenNeurons[i] + "-";
        }
        config += numOfOutputNeurons + "";
    }

    public String getConfig() {
        return config;
    }

    /*
     * Puts the inputs into the network and feeds it through
     */
    public double[] feedForward(double[] inputs) {
        double[] outputLayer_Outputs = new double[numOfOutputNeurons];
        double[] weightedSum = new double[numOfOutputNeurons];

        //stores the outputs for each hidden output for each hidden layer
        double[][] hiddenLayer_outputs = new double[numOfHiddenLayers][];
        for (int i = 0; i < numOfHiddenLayers; i++) {
            hiddenLayer_outputs[i] = new double[numOfHiddenNeurons[i]];
        }

        //1.calculates the outputs for each hidden neuron in each hidden layer
        getHiddenLayerOutputs(inputs, hiddenLayer_outputs);

        //2. Calculate the weighted sum for each output neuron in the output layer using the second hidden layer outputs
        calculateOutputWeightedSum(weightedSum, hiddenLayer_outputs[numOfHiddenLayers - 1]);

        //3. Calculate the output for each output neuron in the output layer using the second hidden layer outputs
        GetOutputLayerOutputs(outputLayer_Outputs, weightedSum);
        return outputLayer_Outputs;
    }

    private void GetOutputLayerOutputs(double[] outputLayer_Outputs, double[] weightedSum) {
        for (int i = 0; i < numOfOutputNeurons; i++) {
            outputNeurons[i].calculateOutput(weightedSum, i);
            outputLayer_Outputs[i] = outputNeurons[i].getOutput();
        }
    }

    private void calculateOutputWeightedSum(double[] weightedSum, double[] hiddenLayer_output) {
        for (int i = 0; i < numOfOutputNeurons; i++) {
            outputNeurons[i].calculateWeightedSum(hiddenLayer_output);
            weightedSum[i] = outputNeurons[i].getWeightedSum();
        }
    }

    private void getHiddenLayerOutputs(double[] inputs, double[][] hiddenLayer_outputs) {
        for (int i = 0; i < numOfHiddenLayers; i++) {
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
        double[][] hidden_outputs = new double[numOfHiddenLayers][];
        //1. Feeds the inputs through the network
        feedForward(inputs);

        //get hidden outputs for each layer
        getHiddenLayerOutputs(hidden_outputs);

        //2. Tunes the output weights
        tuneOutputWeights(hidden_outputs[numOfHiddenLayers - 1], target);

        //3. Tunes the hidden layers in-between the first hidden layer and the output layer
        tuneHiddenLayerWeights(hidden_outputs);

        //4. Tunes the first hidden layer's weights
        tuneFirstLayer(inputs);
    }

    private void tuneHiddenLayerWeights(double[][] hidden_outputs) {
        for (int i = numOfHiddenLayers - 2; i > 0; i--) {
            tuneHiddenLayers(hidden_outputs[i - 1], i);
        }
    }

    private void getHiddenLayerOutputs(double[][] hidden_outputs) {
        for (int i = 0; i < numOfHiddenLayers; i++) {
            hidden_outputs[i] = getHiddenOutputs(i);
        }
    }

    /*
     * Returns an array of the outputs for each neuron in a given hidden layer
     */
    private double[] getHiddenOutputs(int layer) {
        double[] x = new double[numOfHiddenNeurons[layer]];
        for (int i = 0; i < numOfHiddenNeurons[layer]; i++) {
            x[i] = hiddenLayer[layer][i].getOutput();
        }
        return x;
    }

    private void tuneHiddenLayers(double[] inputs, int layer) {
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
                deltaSumTotal += outputNeurons[j].getDeltaSum()[i];
            }
            hiddenLayer[0][i].tuneWeights(LR, inputs, deltaSumTotal);
        }
    }

    /*
     * Tunes the weights of the output layer
     */
    private void tuneOutputWeights(double[] hidden_outputs, double[] target) {
        for (int i = 0; i < numOfOutputNeurons; i++) {
            outputNeurons[i].tuneWeights(LR, hidden_outputs, target[i]);
        }
    }

    /*
     * Creates hidden neurons
     */
    private void createHiddenNeurons() {
        for (int i = 0; i < numOfHiddenLayers; i++) {
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
            outputNeurons[i] = new Output_Neuron(numOfHiddenNeurons[numOfHiddenLayers - 1]);
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
        int[] numOfLayers = new int[2 + numOfHiddenLayers];
        int layer = 0;
        for (int i = 0; i < numOfLayers.length; i++) {
            if (i == 0) {
                numOfLayers[i] = numOfInputNeurons;
            } else if (i == numOfLayers.length - 1) {
                numOfLayers[i] = numOfOutputNeurons;
            } else {
                numOfLayers[i] = numOfHiddenNeurons[layer];
                layer++;
            }
        }
        int size = 0;
        for (int i = 0; i < numOfLayers.length - 1; i++) {
            size += numOfLayers[i] * numOfLayers[i + 1];
        }

        double[] weights = new double[size];
        int index = 0;
        for (int i = 0; i < numOfHiddenNeurons.length; i++) {
            for (int j = 0; j < numOfHiddenNeurons[i]; j++) {
                double[] temp = hiddenLayer[i][j].getWeights();
                for (int k = 0; k < temp.length; k++) {
                    weights[index] = temp[k];
                    index++;
                }
            }
        }

        for (int i = 0; i < numOfOutputNeurons; i++) {
            double[] temp = outputNeurons[i].getWeights();
            for (int j = 0; j < temp.length; j++) {
                weights[index] = temp[j];
                index++;
            }
        }
        return weights;
    }

    public void setWeights(double[] weights) {
        int lastIndex = 0;
        int[] layersOrder = new int[2 + numOfHiddenLayers];
        layersOrder[0] = numOfInputNeurons;
        layersOrder[layersOrder.length - 1] = numOfOutputNeurons;
        for (int i = 1; i < layersOrder.length - 1; i++) {
            layersOrder[i] = numOfHiddenNeurons[i - 1];
        }
        for (int i = 0; i < layersOrder.length - 1; i++) {
            if (i == layersOrder.length - 2) {
                lastIndex = setWeights(weights, lastIndex, layersOrder[i], i, layersOrder[i + 1], true);
            } else {
                lastIndex = setWeights(weights, lastIndex, layersOrder[i], i, layersOrder[i + 1], false);
            }
        }
    }

    private int setWeights(double[] weights, int lastIndex, int prevLayer, int layer, int totalNeurons, boolean output) {
        int index = 0;
        for (int neuron = 0; neuron < totalNeurons; neuron++) {
            double[] temp = new double[prevLayer];
            for (int j = 0; j < temp.length; j++) {
                temp[index] = weights[lastIndex];
                index++;
                lastIndex++;
            }
            if (output) {
                outputNeurons[neuron].setWeights(temp);
            } else {
                hiddenLayer[layer][neuron].setWeights(temp);
            }
            index = 0;
        }
        return lastIndex;
    }
}