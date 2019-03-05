package UserInterfaces;

import javax.swing.*;
import java.awt.*;

public class Neuron extends JPanel {
    private int numberOfNeurons;
    private int x = 10;
    private int y = 10;

    Neuron(int numberOfNeurons, int x, int y, int width, int height) {
        this.numberOfNeurons = numberOfNeurons;
        setSize(width, height);
        setLocation(x, y);
        setLayout(new GridLayout(numberOfNeurons, 1));
    }

    public void paint(Graphics g) {
        super.paint(g);
        for (int i = 0; i < numberOfNeurons; i++) {
            g.drawOval(x, y, 12, 12);
            y += 13;
        }
    }
}
