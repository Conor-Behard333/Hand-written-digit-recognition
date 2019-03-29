package UserInterfaces.GuessUI;

import NeuralNetwork.Network;
import ProcessingData.ImageConverter;
import UserInterfaces.PredictionUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GuessUI extends JFrame {
    private Network network;
    private Canvas canvas;
    private ImagePanel predict;
    private String filePath = "C:\\Users\\conor\\OneDrive\\My Documents\\Number0-9\\None.png";
    private PredictionUI predictionUI = new PredictionUI();

    public GuessUI(Network network) {
        this.network = network;
        createUI();
        setTitle("Neural NeuralNetwork.Network - Digit Classifier");
        setSize(900, 600);
        setBackground(Color.black);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void createUI() {
        addDividers();
        addCanvasPanel();
        addCenterPanel();
        addPredictPanel();
    }

    private void addDividers() {
        Color color = Color.gray;
        JPanel dividerMiddle = newPanel(435, 0, 30, 500, color);
        getContentPane().add(dividerMiddle);

        JPanel dividerLeft = newPanel(0, 0, 30, 500, color);
        getContentPane().add(dividerLeft);

        JPanel dividerTop = newPanel(0, 0, 900, 30, color);
        getContentPane().add(dividerTop);

        JPanel dividerRight = newPanel(870, 0, 30, 500, color);
        getContentPane().add(dividerRight);
    }

    private void addCanvasPanel() {
        canvas = new Canvas();
        canvas.setLocation(30, 30);
        canvas.setSize(405, 470);
        canvas.setBackground(Color.black);
        getContentPane().add(canvas);
    }

    private void addCenterPanel() {
        JPanel centre = newPanel(0, 500, 900, 100, Color.gray);
        getContentPane().add(centre);

        JButton clearCanvas = new JButton("Clear");
        clearCanvas.setLocation(0, 600);
        clearCanvas.setPreferredSize(new Dimension(425, 50));
        clearCanvas.addActionListener(new ButtonClearActionListener());
        centre.add(clearCanvas);

        JButton buttonGuess = new JButton("Guess");
        buttonGuess.setLocation(450, 600);
        buttonGuess.setPreferredSize(new Dimension(425, 50));
        buttonGuess.addActionListener(new ButtonGuessActionListener());
        centre.add(buttonGuess);
    }

    private void addPredictPanel() {
        predict = new ImagePanel(filePath);
        getContentPane().add(predict);
    }

    private JPanel newPanel(int x, int y, int width, int height, Color color) {
        JPanel temp = new JPanel();
        temp.setLocation(x, y);
        temp.setSize(width, height);
        temp.setBackground(color);
        return temp;
    }

    private class ButtonGuessActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int guess;
            try {
                BufferedImage image = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_ARGB);
                canvas.paint(image.getGraphics());
                ImageIO.write(image, "png", new File("C:\\Users\\conor\\OneDrive\\My Documents\\image.png"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            //converts the image the user drew into a 28 by 28 png
            ImageConverter imageConverter = new ImageConverter();
            double[] input = imageConverter.getInput();
            guess = network.getGuess(network.feedForward(input));
            filePath = getImagePath(guess);

            //sets the confidence of the networks guess
            predictionUI.setPredictions(network.feedForward(input));

            //load input image into predict panel
            getContentPane().remove(predict);//removes current prediction
            addPredictPanel();//adds the new panel
            repaint();//draws the image onto the panel
            revalidate();
        }


        private String getImagePath(int guess) {
            String fileName = "C:\\Users\\conor\\OneDrive\\My Documents\\Number0-9\\";
            String[] fileEnding = {"Zero.png", "One.png", "Two.png", "Three.png", "Four.png", "Five.png", "Six.png", "Seven.png", "Eight.png", "Nine.png", "None.png"};
            return fileName + fileEnding[guess];
        }
    }

    private class ButtonClearActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            filePath = "C:\\Users\\conor\\OneDrive\\My Documents\\Number0-9\\None.png";
            getContentPane().remove(canvas);
            getContentPane().remove(predict);
            addCanvasPanel();
            addPredictPanel();
            revalidate();
            repaint();
        }
    }
}
