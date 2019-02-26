public class Run {
    public static void main(String[] args) {

        Network network = new Network(784, 15, 10);
        DataSet ds = new DataSet();
        TestingDataSet tds = new TestingDataSet();
        //train the network
        for (int i = 0; i < 50000; i++) {
            network.train(ds.getInput(), ds.getLabel());
        }
        network.feedForward(tds.getInput());
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