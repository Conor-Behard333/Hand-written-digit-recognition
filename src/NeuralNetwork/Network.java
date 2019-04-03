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

    /*
     * initialises all the global variables used and creates all the necessary neurons
     */
    public Network(int inputNeurons, int hiddenNeurons_layer_1, int hiddenNeurons_layer_2, int outputNeurons) {
        this.numOfInputNeurons = inputNeurons;
        this.numOfHiddenNeurons_layer_1 = hiddenNeurons_layer_1;
        this.numOfHiddenNeurons_layer_2 = hiddenNeurons_layer_2;
        this.numOfOutputNeurons = outputNeurons;

        this.hiddenLayer_1 = new Hidden_Neuron[hiddenNeurons_layer_1];
        this.hiddenLayer_2 = new Hidden_Neuron[hiddenNeurons_layer_2];
        this.output_neurons = new Output_Neuron[outputNeurons];

        createHiddenNeurons(true);//Creates the first layer of hidden neurons
        createHiddenNeurons(false);//Creates the second layer of hidden neurons
        createOutputNeurons();//Creates the output layer
    }


    /*
     * Puts the inputs into the network and feeds it through
     */
    public double[] feedForward(double[] inputs) {
        double[] hidden_outputs_layer_1 = new double[numOfHiddenNeurons_layer_1];
        double[] hidden_outputs_layer_2 = new double[numOfHiddenNeurons_layer_2];
        double[] output_outputs = new double[numOfOutputNeurons];
        double[] weightedSum = new double[numOfOutputNeurons];

        //1. Calculate the output for each hidden neuron in the first layer using the image inputs
        for (int i = 0; i < numOfHiddenNeurons_layer_1; i++) {
            hiddenLayer_1[i].calculateOutput(inputs);
            hidden_outputs_layer_1[i] = hiddenLayer_1[i].getOutput();
        }

        //2. Calculate the output for each hidden neuron in the second layer using the first hidden layer outputs
        for (int i = 0; i < numOfHiddenNeurons_layer_2; i++) {
            hiddenLayer_2[i].calculateOutput(hidden_outputs_layer_1);
            hidden_outputs_layer_2[i] = hiddenLayer_2[i].getOutput();
        }

        //3. Calculate the weighted sum for each output neuron in the output layer using the second hidden layer outputs
        for (int i = 0; i < numOfOutputNeurons; i++) {
            output_neurons[i].calculateWeightedSum(hidden_outputs_layer_2);
            weightedSum[i] = output_neurons[i].getWeightedSum();
        }

        //4. Calculate the output for each output neuron in the output layer using the second hidden layer outputs
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
        //1. Feeds the inputs through the network
        feedForward(inputs);

        //get hidden outputs for each layer
        double[] hidden_outputs_layer_2 = getHiddenOutputsLayer(false);
        double[] hidden_outputs_layer_1 = getHiddenOutputsLayer(true);

        //2. Tunes the output weights
        tuneOutputWeights(hidden_outputs_layer_2, target);

        //3. Tunes the second hidden layer's weights
        tuneHiddenWeightsLayer(hidden_outputs_layer_1, false);

        //4. Tunes the first hidden layer's weights
        tuneHiddenWeightsLayer(inputs, true);
    }

    /*
     * Returns the first hidden layer outputs if 'firsLayer' = true
     * otherwise it will return the second hidden layer outputs
     */
    private double[] getHiddenOutputsLayer(boolean firstLayer) {
        if (firstLayer) {
            double[] x = new double[numOfHiddenNeurons_layer_1];
            for (int i = 0; i < numOfHiddenNeurons_layer_1; i++) {
                x[i] = hiddenLayer_1[i].getOutput();
            }
            return x;
        } else {
            double[] x = new double[numOfHiddenNeurons_layer_2];
            for (int i = 0; i < numOfHiddenNeurons_layer_2; i++) {
                x[i] = hiddenLayer_2[i].getOutput();
            }
            return x;
        }
    }

    /*
     * Tunes the weights of the first layer if 'firstLayer' = true
     * otherwise it will tune the weights of the second layer
     *
     * deltaSumTotal is the sum of
     */
    private void tuneHiddenWeightsLayer(double[] inputs, boolean firstLayer) {
        if (firstLayer) {
            for (int i = 0; i < numOfHiddenNeurons_layer_1; i++) {
                double deltaSumTotal = 0;
                for (int j = 0; j < numOfHiddenNeurons_layer_2; j++) {
                    deltaSumTotal += hiddenLayer_2[j].getDeltaSum()[i];
                }
                hiddenLayer_1[i].tuneWeights(LR, inputs, deltaSumTotal);
            }
        } else {
            for (int i = 0; i < numOfHiddenNeurons_layer_2; i++) {
                double deltaSumTotal = 0;
                for (int j = 0; j < numOfOutputNeurons; j++) {
                    deltaSumTotal += output_neurons[j].getDeltaSum()[i];
                }
                hiddenLayer_2[i].tuneWeights(LR, inputs, deltaSumTotal);
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
    private void createHiddenNeurons(boolean firstLayer) {
        if (firstLayer) {
            for (int i = 0; i < numOfHiddenNeurons_layer_1; i++) {
                hiddenLayer_1[i] = new Hidden_Neuron(numOfInputNeurons);
            }
        } else {
            for (int i = 0; i < numOfHiddenNeurons_layer_2; i++) {
                hiddenLayer_2[i] = new Hidden_Neuron(numOfHiddenNeurons_layer_1);
            }
        }
    }

    /*
     * Creates output neurons
     */
    private void createOutputNeurons() {
        for (int i = 0; i < numOfOutputNeurons; i++) {
            output_neurons[i] = new Output_Neuron(numOfHiddenNeurons_layer_2);
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