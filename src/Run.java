public class Run {
    public static void main(String[] args) {
        int batchSizeTraining = 60000;
        int batchSizeTesting = 10000;
        double accuracy = 0;
        int epochs = 1;
        DataSet trainingData = new DataSet(batchSizeTraining, "C:\\Users\\conor\\IdeaProjects\\Files\\mnist_train.csv");
        DataSet testingData = new DataSet(batchSizeTesting, "C:\\Users\\conor\\IdeaProjects\\Files\\mnist_test.csv");
        Network network = new Network(784, 74, 10);
        for (int j = 0; j < epochs; j++) {
            for (int i = 0; i < batchSizeTraining; i++) {
                network.train(trainingData.getInputData(i), getTarget(trainingData.getLabel(i)));
            }
        }

        for (int n = 0; n < 10000; n++) {
            int guess = getGuess(network.feedForward(testingData.getInputData(n)));
            int label = testingData.getLabel(n);
            if (label == guess) {
                accuracy++;
            }
        }
        System.out.println("Accuracy: " + (accuracy / 100) + "%");
        //Best Accuracy: 74.61%
    }

    private static int getGuess(double[] outputs) {
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