package UserInterfaces.GuessUI;

import NeuralNetwork.Network;
import ProcessingData.ImageConverter;

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
            BufferedImage image = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_ARGB);
            canvas.paint(image.getGraphics());
            try {
                ImageIO.write(image, "png", new File("C:\\Users\\conor\\OneDrive\\My Documents\\image.png"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            ImageConverter imageConverter = new ImageConverter();
            guess = network.getGuess(network.feedForward(imageConverter.getInput()));
            filePath = getImagePath(guess);
            //load input image into predict panel
            getContentPane().remove(predict);
            addPredictPanel();
            repaint();
            revalidate();
        }


        private String getImagePath(int guess) {
            String fileName = "C:\\Users\\conor\\OneDrive\\My Documents\\Number0-9\\";
            switch (guess) {
                case -1:
                    fileName += "None.png";
                    break;
                case 0:
                    fileName += "Zero.png";
                    break;
                case 1:
                    fileName += "One.png";
                    break;
                case 2:
                    fileName += "Two.png";
                    break;
                case 3:
                    fileName += "Three.png";
                    break;
                case 4:
                    fileName += "Four.png";
                    break;
                case 5:
                    fileName += "Five.png";
                    break;
                case 6:
                    fileName += "Six.png";
                    break;
                case 7:
                    fileName += "Seven.png";
                    break;
                case 8:
                    fileName += "Eight.png";
                    break;
                case 9:
                    fileName += "Nine.png";
                    break;
            }
            return fileName;
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
