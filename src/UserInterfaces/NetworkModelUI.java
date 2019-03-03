package UserInterfaces;

import javax.swing.*;
import java.awt.*;

public class NetworkModelUI extends JFrame {
    private final int input = 784;
    private final int hidden;
    private final int output = 10;

    private JPanel inputPanel;
    private JPanel hiddenPanel;
    private JPanel outputPanel;
    private JPanel outputPercentagePanel;

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
        JPanel inputPanel = createPanel(0, 0, 200, 1040, Color.yellow);
        inputPanel.setLayout(new GridLayout(input, 1));
        getContentPane().add(inputPanel);

        JPanel hiddenPanel = createPanel(200, 0, 200, 1040, Color.red);
        inputPanel.setLayout(new GridLayout(hidden, 1));
        getContentPane().add(hiddenPanel);

        JPanel outputPanel = createPanel(400, 0, 200, 1040, Color.blue);
        inputPanel.setLayout(new GridLayout(output, 1));
        getContentPane().add(outputPanel);

        JPanel outputPercentagePanel = createPanel(600, 0, 200, 1040, Color.white);
        getContentPane().add(outputPercentagePanel);
    }

    private JPanel createPanel(int x, int y, int width, int height, Color color) {
        JPanel temp = new JPanel();
        temp.setLocation(x, y);
        temp.setSize(width, height);
        temp.setBackground(color);
        return temp;
    }
}
