package UserInterfaces;

import javax.swing.*;
import java.awt.*;

public class NetworkModelUI extends JFrame {
    private final int input = 784;
    private final int hidden;
    private final int output = 10;

    public NetworkModelUI(int hidden) {
        this.hidden = hidden;
        setTitle("Neural NeuralNetwork.Network - Digit Classifier");
        setSize(800, 1040);
        setBackground(Color.black);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(1100, 0);
        setResizable(false);
        createUI();
    }

    private void createUI() {

        getContentPane().add(new Neuron(input, 0, 0, 200, 1040));

        getContentPane().add(new Neuron(hidden, 200, 0, 200, 1040));

        getContentPane().add(new Neuron(output, 400, 0, 200, 1040));

        getContentPane().add(createPanel(600, 0, 200, 1040));
    }

    private JPanel createPanel(int x, int y, int width, int height) {
        JPanel temp = new JPanel();
        temp.setLocation(x, y);
        temp.setSize(width, height);
        return temp;
    }
}
