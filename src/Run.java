import java.io.IOException;

public class Run {
    public static void main(String[] args) throws IOException {
        //load data
        int batchSizeTraining = 60000;
        DataSet trainingData = new DataSet(batchSizeTraining, "C:\\Users\\conor\\IdeaProjects\\Files\\mnist_train.csv");
        DataSet testingData = new DataSet(10000, "C:\\Users\\conor\\IdeaProjects\\Files\\mnist_test.csv");

        Network network = new Network(784, 74, 10);//optimal = 74 hidden
        //train
        int epochs = 5;
        for (int j = 0; j < epochs; j++) {
            for (int i = 0; i < batchSizeTraining; i++) {
                network.train(trainingData.getInputData(i), getTarget(trainingData.getLabel(i)));
            }
            for (int i = 0; i < 10000; i++) {
                network.train(testingData.getInputData(i), getTarget(testingData.getLabel(i)));
            }
        }
        //display user interface
        GUI ui = new GUI(network);
        ui.setVisible(true);
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