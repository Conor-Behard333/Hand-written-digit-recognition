import java.io.IOException;

public class Run {
    public static void main(String[] args) throws IOException {
        NetworkSettingsUI settings = new NetworkSettingsUI();
        int batchSize = settings.getBatchSize();
        int epochs = settings.getEpochs();
        int hiddenNeurons = settings.getHiddenNeurons();

        //load data
        DataSet trainingData = new DataSet(batchSize, "C:\\Users\\conor\\IdeaProjects\\Files\\mnist_train.csv");

        Network network = new Network(784, hiddenNeurons, 10);//optimal = 74 hidden

        //train
        for (int j = 0; j < epochs; j++) {
            for (int i = 0; i < batchSize; i++) {
                network.train(trainingData.getInputData(i), getTarget(trainingData.getLabel(i)));
            }
        }
        //display the guess user interface
        GuessUI guessUI = new GuessUI(network);
        guessUI.setVisible(true);

//        NetworkModelUI n = new NetworkModelUI(74);
//        n.setVisible(true);
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