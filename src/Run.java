
public class Run {
    public static void main(String[] args) {

        Network network = new Network(784, 15, 10);
        DataSet ds = new DataSet();
        //train the network
        for (int i = 0; i < 50000; i++) {
            network.train(ds.getInput(),ds.getLabel());
        }
    }
}