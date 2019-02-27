import sun.nio.ch.Net;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GUI extends JFrame {
    private static Network network;
    private JButton buttonGuess, clearCanvas;
    private JPanel centre, predict, divider;
    private Canvas canvas;

    public GUI(Network network) {
        this.network = network;
        createUI();
        setTitle("Neural Network");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) {
        GUI g = new GUI(new Network(784, 74, 10));
    }


    private void createUI() {
        addDivider();
        addCanvasPanel();
        addCenterPanel();
        addPredictPanel();
    }

    private void addDivider() {
        divider = new JPanel();
        divider.setLocation(435, 0);
        divider.setSize(30, 500);
        divider.setBackground(Color.gray);
        getContentPane().add(divider);
    }

    private void addCanvasPanel() {
        canvas = new Canvas();
        canvas.setLocation(0, 0);
        canvas.setSize(100, 100);//normally 435, 500 but made smaller for testing
        canvas.setBackground(Color.white);
        getContentPane().add(canvas);
    }

    private void addCenterPanel() {
        centre = new JPanel();
        centre.setLocation(0, 500);
        centre.setSize(900, 100);
        centre.setBackground(Color.gray);
        getContentPane().add(centre);

        clearCanvas = new JButton("Clear");
        clearCanvas.setLocation(0, 600);
        clearCanvas.setPreferredSize(new Dimension(425, 50));
        clearCanvas.addActionListener(new ButtonClearActionListener());
        centre.add(clearCanvas);

        buttonGuess = new JButton("Guess");
        buttonGuess.setLocation(450, 600);
        buttonGuess.setPreferredSize(new Dimension(425, 50));
        buttonGuess.addActionListener(new ButtonGuessActionListener());
        centre.add(buttonGuess);

    }

    private void addPredictPanel() {
        predict = new JPanel();
        predict.setLocation(500, 0);
        predict.setSize(435, 500);
        predict.setBackground(Color.white);
        getContentPane().add(predict);
    }

    private class ButtonGuessActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            BufferedImage image = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_ARGB);
            canvas.paint(image.getGraphics());
            try {
                ImageIO.write(image, "png", new File("C:\\Users\\conor\\OneDrive\\My Documents\\image.png"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
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
            getContentPane().remove(canvas);
            addCanvasPanel();
        }
    }
}
