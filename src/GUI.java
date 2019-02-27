import com.sun.xml.internal.messaging.saaj.soap.JpegDataContentHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {

    private JButton buttonTrain, buttonGuess, clearCanvas;
    private JPanel centre, predict;
    private Canvas c;
    private Network network;

    public GUI(Network network) {
        this.network = network;

        createUI();

        setTitle("Neural Network");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI(new Network(754, 74, 10)).setVisible(true);
            }
        });
    }

    private void createUI() {
        addCanvasPanel();
        addCenterPanel();
        addPredictPanel();
    }


    private void addCanvasPanel() {
        c = new Canvas();
        c.setSize(400, 600);
        c.setLocation(0, 0);
        c.setBackground(Color.white);
        getContentPane().add(c);
    }

    private void addCenterPanel() {
        centre = new JPanel();
        centre.setLayout(new GridLayout(3, 1));
        centre.setSize(100, 600);
        centre.setLocation(400, 0);
        //centre.setBackground(Color.red);
        getContentPane().add(centre);

        buttonTrain = new JButton("Train");
        buttonTrain.setPreferredSize(new Dimension(100, 50));
        buttonTrain.addActionListener(new ButtonTrainActionListener());
        centre.add(buttonTrain);

        buttonGuess = new JButton("Guess");
        buttonGuess.setPreferredSize(new Dimension(100, 50));
        buttonGuess.addActionListener(new ButtonGuessActionListener());
        centre.add(buttonGuess);

        clearCanvas = new JButton("Clear");
        clearCanvas.setPreferredSize(new Dimension(100, 50));
        clearCanvas.addActionListener(new ButtonClearActionListener());
        centre.add(clearCanvas);
    }

    private void addPredictPanel() {
        predict = new JPanel();
        predict.setSize(400, 600);
        predict.setLocation(500, 0);
        predict.setBackground(Color.white);
        getContentPane().add(predict);
    }

    private class ButtonGuessActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //get inputs
            //feed forward
            //get guess
            //show probabilities
        }
    }

    private class ButtonTrainActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //train network
        }
    }

    private class ButtonClearActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //clear canvas
            c.clear();
        }
    }
}
