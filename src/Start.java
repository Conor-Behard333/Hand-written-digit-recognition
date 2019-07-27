import NeuralNetwork.Function;
import NeuralNetwork.Network;
import ProcessingData.LoadDataSet;
import UserInterfaces.GuessUI.GuessUI;
import ProcessingData.LoadFile;
import UserInterfaces.Other.LoadingUI;
import UserInterfaces.Other.NetworkSettingsUI;

import javax.swing.*;
import java.io.File;

class Start extends Function {

    /*
     * Based on the label this method returns the target values for the network
     */
    void run() throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        int loadReply = JOptionPane.showConfirmDialog(null, "Do you want to load a preset configuration", "Load File?", JOptionPane.YES_NO_OPTION);
        if (loadReply == -1) {
            System.exit(0);
        }
        boolean loadFile = (loadReply == (JOptionPane.YES_OPTION));
        if (loadFile) {
            String[] fileNames = getFileNames();
            String saveFile = (String) JOptionPane.showInputDialog(null, "Select a save file", "Save file", JOptionPane.QUESTION_MESSAGE, null, fileNames, fileNames[0]);
            int[] hiddenNeurons = getNumOfHiddenNeurons(saveFile);
            //Create the network with 784 input neurons,  10 output neurons and x amount of hidden neurons with x amount of hidden layers
            Network network = new Network(784, 10, hiddenNeurons);
            double[] weights = new LoadFile().load("Resources\\SaveFiles\\" + saveFile);
            network.setWeights(weights);
            //Display the guess user interface
            new GuessUI(network);
        } else {
            NetworkSettingsUI settings = new NetworkSettingsUI();
            //How many values are trained by the network
            int batchSize = settings.getBatchSize();
            //How many times the network trains an entire batch
            int epochs = settings.getEpochs();
            //An array containing the number of neurons in each hidden layer
            int[] hiddenNeurons = settings.getHiddenNeurons();

            //Create the network with 784 input neurons,  10 output neurons and x amount of hidden neurons with x amount of hidden layers
            Network network = new Network(784, 10, hiddenNeurons);

            //display loading GUI
            LoadingUI load = new LoadingUI();

            //Load the training data
            LoadDataSet trainingData = new LoadDataSet(batchSize, "Resources\\mnist_train.csv");

            //Trains the network
            for (int j = 0; j < epochs; j++) {
                for (int i = 0; i < batchSize; i++) {
                    network.train(trainingData.getInputData(i), trainingData.getLabel(i));
                }
                trainingData.randomiseTrainingData();
            }

            //close loading GUI
            load.setVisible(false);

            //Display the guess user interface
            new GuessUI(network);
        }
    }

    private int[] getNumOfHiddenNeurons(String saveFile) {
        int count = 0;
        for (int i = 0; i < saveFile.length(); i++) {
            if (saveFile.charAt(i) == '-') {
                count++;
            }
        }
        int numOfHiddenLayers = count - 1;
        int[] numOfHiddenNeurons = new int[numOfHiddenLayers];
        int start = getStart(saveFile);
        int end = 0;
        for (int i = 0; i < numOfHiddenLayers; i++) {
            for (int j = start; j < saveFile.length(); j++) {
                if (saveFile.charAt(j) == '-') {
                    end = j;
                    break;
                }
            }
            numOfHiddenNeurons[i] = Integer.parseInt(saveFile.substring(start, end));
            start = end + 1;
        }
        return numOfHiddenNeurons;
    }

    private int getStart(String saveFile) {
        for (int i = 0; i < saveFile.length(); i++) {
            if (saveFile.charAt(i) == '-') {
                return i + 1;
            }
        }
        return 0;
    }

    private String[] getFileNames() {
        File folder = new File("Resources\\SaveFiles");
        File[] files = folder.listFiles();
        String[] fileNames = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            fileNames[i] = files[i].getName();
        }
        return fileNames;
    }
}
