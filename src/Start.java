import NeuralNetwork.Network;
import ProcessingData.LoadDataSet;
import UserInterfaces.GuessUI.GuessUI;
import UserInterfaces.Other.LoadingUI;
import UserInterfaces.Other.SaveOrLoad;
import UserInterfaces.Other.NetworkSettingsUI;

import javax.swing.*;

class Start {

    /*
     * Based on the label this method returns the target values for the network
     */
    private double[] getTarget(int label) {
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

    void run() throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SaveOrLoad saveOrLoad = new SaveOrLoad();
        int loadReply = JOptionPane.showConfirmDialog(null, "Do you want to load a preset configuration", "Load File?", JOptionPane.YES_NO_OPTION);
        boolean loadFile = (loadReply == (JOptionPane.YES_OPTION));
        if (loadFile) {
            //Create the network with 784 input neurons,  10 output neurons and x amount of hidden neurons with x amount of hidden layers
            Network network = new Network(784, 10, 50, 50);

            double[] weights = saveOrLoad.loadFile("784-50-50-10");
            network.setWeights(weights);

            //Display the guess user interface
            new GuessUI(network);
        } else {
            NetworkSettingsUI settings = new NetworkSettingsUI();
            int batchSize = settings.getBatchSize();//How many values are trained by the network
            int epochs = settings.getEpochs();//How many times the network trains an entire batch
            int[] hiddenNeurons = settings.getHiddenNeurons();//An array containing the number of neurons in each hidden layer

            //Create the network with 784 input neurons,  10 output neurons and x amount of hidden neurons with x amount of hidden layers
            Network network = new Network(784, 10, hiddenNeurons);

            //display loading GUI
            LoadingUI load = new LoadingUI();

            //Load the training data
            LoadDataSet trainingData = new LoadDataSet(batchSize, "Resources\\mnist_train.csv");

            //Trains the network
            for (int j = 0; j < epochs; j++) {
                for (int i = 0; i < batchSize; i++) {
                    network.train(trainingData.getInputData(i), getTarget(trainingData.getLabel(i)));
                }
                trainingData.randomiseTrainingData();
            }

            int saveReply = JOptionPane.showConfirmDialog(null, "Do you want to save the current configuration", "Save File?", JOptionPane.YES_NO_OPTION);
            if (saveReply == JOptionPane.YES_OPTION) {
                double[] weights = network.getWeights();
                saveOrLoad.saveFile(network.getConfig(), weights);
            }
            //close loading GUI
            load.setVisible(false);

            //Display the guess user interface
            new GuessUI(network);
        }
    }
}
