import NeuralNetwork.Network;
import ProcessingData.LoadDataSet;
import UserInterfaces.GuessUI.GuessUI;
import UserInterfaces.Other.LoadingUI;
import UserInterfaces.Other.NetworkSettingsUI;

import javax.swing.*;

public class Start {

    /*
     * Based on the label this method returns the target values for the network
     */
    private static double[] getTarget(int label) {
        double[][] targets = {{1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 1}};
        return targets[label];
    }

    public void run() throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        NetworkSettingsUI settings = new NetworkSettingsUI();
        int batchSize = settings.getBatchSize();//How many values are trained by the network
        int epochs = settings.getEpochs();//How many times the network trains an entire batch
        int[] hiddenNeurons = settings.getHiddenNeurons();//An array containing the number of neurons in each hidden layer

        //display loading GUI
        LoadingUI load = new LoadingUI();
        load.setVisible(true);

        //Load the training data
        LoadDataSet trainingData = new LoadDataSet(batchSize, "Resources\\mnist_train.csv");

        //Create the network with 784 input neurons,  10 output neurons and x amount of hidden neurons with x amount of hidden layers
        Network network = new Network(784, 10, hiddenNeurons);

        //Trains the network
        for (int j = 0; j < epochs; j++) {
            for (int i = 0; i < batchSize; i++) {
                network.train(trainingData.getInputData(i), getTarget(trainingData.getLabel(i)));
            }
            trainingData.randomiseTrainingData();
        }

        //close loading GUI
        load.setVisible(false);

        //Display the guess user interface
        GuessUI guessUI = new GuessUI(network);
        guessUI.setVisible(true);
    }
}
