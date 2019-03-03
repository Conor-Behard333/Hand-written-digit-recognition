package NeuralNetwork;

public class Network {
    private final double LR = 0.05;
    private int numOfInputNeurons;
    private int numOfHiddenNeurons;
    private int numOfOutputNeurons;

    private Hidden_Neuron[] hidden_neurons;
    private Output_Neuron[] output_neurons;

    public Network(int inputNeurons, int hiddenNeurons, int outputNeurons) {
        this.numOfInputNeurons = inputNeurons;
        this.numOfHiddenNeurons = hiddenNeurons;
        this.numOfOutputNeurons = outputNeurons;

        this.hidden_neurons = new Hidden_Neuron[hiddenNeurons];
        this.output_neurons = new Output_Neuron[outputNeurons];

        createHiddenNeurons();
        createOutputNeurons();
    }

    public double[] feedForward(double[] inputs) {
        Function f = new Function();
        double[] hidden_outputs = new double[numOfHiddenNeurons];
        double[] output_outputs = new double[numOfOutputNeurons];

        //set and get the hidden neuron's outputs
        for (int i = 0; i < numOfHiddenNeurons; i++) {
            hidden_neurons[i].setOutput(inputs);
            hidden_outputs[i] = hidden_neurons[i].getOutput();
        }

        //set the weighted sum of each output neuron
        for (int i = 0; i < numOfOutputNeurons; i++) {
            output_neurons[i].setWeightedSum(hidden_outputs);
        }

        //get the weighted sum of each output neuron and put them into an array
        double[] weightedSum = new double[numOfOutputNeurons];
        for (int i = 0; i < numOfOutputNeurons; i++) {
            weightedSum[i] = output_neurons[i].getWeightedSum();
        }

        //set and get the output neuron's outputs
        for (int i = 0; i < numOfOutputNeurons; i++) {
            output_neurons[i].setOutput(f.softMax(weightedSum, i));
            output_outputs[i] = output_neurons[i].getOutput();
        }
        //return the final output of the network
        return output_outputs;
    }

    public void train(double[] inputs, double[] target) {
        feedForward(inputs);
        //back propagation
        double[] hidden_outputs = getHiddenOutputs();//get hidden outputs
        tuneOutputWeights(hidden_outputs, target);//tune output weights
        tuneHiddenWeights(inputs); //tune Hidden weights
    }

    private double[] getHiddenOutputs() {
        double[] x = new double[numOfHiddenNeurons];
        for (int i = 0; i < numOfHiddenNeurons; i++) {
            x[i] = hidden_neurons[i].getOutput();
        }
        return x;
    }

    private void tuneHiddenWeights(double[] inputs) {
        for (int i = 0; i < numOfHiddenNeurons; i++) {
            double weightedDeltaHiddenTotal = 0;
            for (int j = 0; j < numOfOutputNeurons; j++) {
                weightedDeltaHiddenTotal += output_neurons[j].getWeightedDeltaHidden()[i];
            }
            hidden_neurons[i].tuneWeights(LR, inputs, weightedDeltaHiddenTotal);
        }
    }

    private void tuneOutputWeights(double[] hidden_outputs, double[] target) {
        for (int i = 0; i < numOfOutputNeurons; i++) {
            output_neurons[i].tuneWeights(LR, hidden_outputs, target[i]);
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
        return 10;
    }
}