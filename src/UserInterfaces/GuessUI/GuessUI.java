package UserInterfaces.GuessUI;

import NeuralNetwork.Network;
import ProcessingData.ImageConverter;
import ProcessingData.SaveFile;
import UserInterfaces.Other.ConfidenceUI;
import jdk.nashorn.internal.scripts.JO;

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
    private String filePath = "Resources\\Number0-9\\None.png";
    private ConfidenceUI predictionUI = new ConfidenceUI();

    /*
     * Creates the window for the guess UI
     */
    public GuessUI(Network network) {
        this.network = network;
        createUI();
        setTitle("Neural Network - Digit Classifier");
        setSize(900, 600);
        setBackground(Color.black);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    /*
     * Adds all the required panels into the frame (window)
     */
    private void createUI() {
        addBoarders();
        addCanvasPanel();
        addCenterPanel();
        addPredictPanel();
    }

    /*
     * Adds borders to the image panels
     */
    private void addBoarders() {
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

    /*
     * Adds a canvas panel which allows the user to draw on
     */
    private void addCanvasPanel() {
        canvas = new Canvas();
        canvas.setLocation(30, 30);
        canvas.setSize(405, 470);
        canvas.setBackground(Color.black);
        getContentPane().add(canvas);
    }

    /*
     * Adds a panel with the buttons "Clear" and "Guess"
     * which are used to clear the drawing panel and guess
     * the current drawing
     */
    private void addCenterPanel() {
        JPanel centre = newPanel(0, 500, 900, 100, Color.gray);
        getContentPane().add(centre);

        JButton clearCanvas = getButton("Clear", 0, 300, 250, 50);
        clearCanvas.addActionListener(new ClearButtonActionListener());
        centre.add(clearCanvas);

        JButton save = getButton("Save", 300, 600, 250, 50);
        save.addActionListener(new ButtonSaveActionListener());
        centre.add(save);

        JButton buttonGuess = getButton("Guess", 600, 900, 250, 50);
        buttonGuess.addActionListener(new ButtonGuessActionListener());
        centre.add(buttonGuess);
    }

    private JButton getButton(String name, int x, int y, int width, int height) {
        JButton buttonGuess = new JButton(name);
        buttonGuess.setLocation(x, y);
        buttonGuess.setPreferredSize(new Dimension(width, height));
        return buttonGuess;
    }

    /*
     * Adds a panel which is used to display the networks guess
     */
    private void addPredictPanel() {
        predict = new ImagePanel(filePath);
        getContentPane().add(predict);
    }

    /*
     * Template to create a new JPanel
     */
    private JPanel newPanel(int x, int y, int width, int height, Color color) {
        JPanel temp = new JPanel();
        temp.setLocation(x, y);
        temp.setSize(width, height);
        temp.setBackground(color);
        return temp;
    }

    /*
     * ActionListener for the guess button
     */
    private class ButtonGuessActionListener implements ActionListener {
        @Override
        /*
         * When the button is pressed the image will be converted
         * into the appropriate values for the network and are then
         * put into the network and the largest output will be the guess
         * and the image associated with the guess will be displayed
         *
         * The prediction UI panel will also be updated to the current
         * confidence values that the network generated for the drawn image
         * and will be shown as progress bars
         */
        public void actionPerformed(ActionEvent e) {
            //Writes the drawn image and stores it as a png at the src directory
            try {
                createImageFile();
            } catch (Exception error) {
                System.out.println(error);
            }

            //Converts the image the user drew into a 28 by 28 png and stores
            //the grey scale values of each pixel in a double array which are
            //then put into the network and the largest output is the guess which
            //is then used to display the image associated with the guess
            ImageConverter imageConverter = new ImageConverter();
            double[] input = imageConverter.getInput();
            double[] output = network.feedForward(input);
            int guess = network.getGuess(output);
            filePath = getImagePath(guess);

            //sets the confidence of the networks guess
            predictionUI.setConfidence(network.feedForward(input));

            //load input image into predict panel
            loadPredictPanel();
        }

        private void loadPredictPanel() {
            getContentPane().remove(predict);//removes current prediction
            addPredictPanel();//adds the new panel
            repaint();//draws the image onto the panel
            revalidate();
        }

        private void createImageFile() throws IOException {
            BufferedImage image = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_ARGB);
            canvas.paint(image.getGraphics());
            ImageIO.write(image, "png", new File("Resources\\image.png"));
        }

        /*
         * Returns the filepath associated with the guess
         */
        private String getImagePath(int guess) {
            String fileName = "Resources\\Number0-9\\";
            String[] fileEnding = {"Zero.png", "One.png", "Two.png", "Three.png", "Four.png", "Five.png", "Six.png", "Seven.png", "Eight.png", "Nine.png", "None.png"};
            return fileName + fileEnding[guess];
        }
    }

    private class ButtonSaveActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int saveReply = JOptionPane.showConfirmDialog(null, "Do you want to save the current configuration", "Save File?", JOptionPane.YES_NO_OPTION);
            if (saveReply == JOptionPane.YES_OPTION) {
                File folder = new File("Resources\\SaveFiles");
                double[] weights = network.getWeights();
                new SaveFile().save("Resources\\SaveFiles\\" + "(" + (folder.listFiles().length + 1) + ")" + network.getConfig(), weights);
                JOptionPane.showMessageDialog(null, "The file has been saved and named: \n" + network.getConfig(), "File saved", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private class ClearButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            filePath = "Resources\\Number0-9\\None.png";
            getContentPane().remove(canvas);//removes current drawing
            getContentPane().remove(predict);//removes current prediction
            addCanvasPanel();//adds the new canvas panel
            addPredictPanel();//adds the new predict panel
            repaint();//redraws the panels
            revalidate();
        }
    }
}

