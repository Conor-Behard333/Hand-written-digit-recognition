import NeuralNetwork.Network;
import ProcessingData.DataSet;
import UserInterfaces.GuessUI.GuessUI;
import UserInterfaces.NetworkSettingsUI;

import java.io.IOException;

public class Run {
    public static void main(String[] args) throws IOException {

        NetworkSettingsUI settings = new NetworkSettingsUI();
        int batchSize = settings.getBatchSize();//how many values are trained by the network
        int epochs = settings.getEpochs();//how many times the network trains an entire batch

        //load the training data
        DataSet trainingData = new DataSet(batchSize, "C:\\Users\\conor\\IdeaProjects\\Files\\mnist_train.csv");

        //create the network with 784 input neurons, 74 hidden neurons (layer 1), 74 hidden neurons (layer 2) and 10 output neurons
        Network network = new Network(784, 45, 45, 10);//45 gets optimum accuracy

        //trains the network
        for (int j = 0; j < epochs; j++) {
            for (int i = 0; i < batchSize; i++) {
                network.train(trainingData.getInputData(i), getTarget(trainingData.getLabel(i)));
            }
        }
        //display the guess user interface
        GuessUI guessUI = new GuessUI(network);
        guessUI.setVisible(true);

    }

    private static double[] getTarget(int label) {
        switch (label) {
            case 0:
                return new double[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            case 1:
                return new double[]{0, 1, 0, 0, 0, 0, 0, 0, 0, 0};
            case 2:
                return new double[]{0, 0, 1, 0, 0, 0, 0, 0, 0, 0};
            case 3:
                return new double[]{0, 0, 0, 1, 0, 0, 0, 0, 0, 0};
            case 4:
                return new double[]{0, 0, 0, 0, 1, 0, 0, 0, 0, 0};
            case 5:
                return new double[]{0, 0, 0, 0, 0, 1, 0, 0, 0, 0};
            case 6:
                return new double[]{0, 0, 0, 0, 0, 0, 1, 0, 0, 0};
            case 7:
                return new double[]{0, 0, 0, 0, 0, 0, 0, 1, 0, 0};
            case 8:
                return new double[]{0, 0, 0, 0, 0, 0, 0, 0, 1, 0};
            case 9:
                return new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
            default:
                System.out.println("ERROR");
                return null;
        }
    }
}