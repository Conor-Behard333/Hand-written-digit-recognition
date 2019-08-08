package UserInterfaces.GuessUI;

import NeuralNetwork.Function;
import NeuralNetwork.Network;
import ProcessingData.ImageConverter;
import ProcessingData.SaveFile;
import UserInterfaces.Other.ConfidenceUI;

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
    private boolean toggleButtonPressed = false;

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
        JPanel dividerMiddle = newPanel(435, 0, 30, 500);
        add(dividerMiddle);

        JPanel dividerLeft = newPanel(0, 0, 30, 500);
        add(dividerLeft);

        JPanel dividerTop = newPanel(0, 0, 900, 30);
        add(dividerTop);

        JPanel dividerRight = newPanel(870, 0, 30, 500);
        add(dividerRight);
    }

    /*
     * Adds a canvas panel which allows the user to draw on
     */
    private void addCanvasPanel() {
        canvas = new Canvas();
        canvas.setBounds(30,30,400,470);
        canvas.setBackground(Color.black);
        add(canvas);
    }

    /*
     * Adds a panel with the buttons "Clear" and "Guess"
     * which are used to clear the drawing panel and guess
     * the current drawing
     */
    private void addCenterPanel() {
        JPanel centre = newPanel(0, 500, 900, 100);
        add(centre);
        int width = 210, height = 50;

        JToggleButton toggleButton = new JToggleButton("Train");
        toggleButton.setPreferredSize(new Dimension(width, height));
        toggleButton.addActionListener(new TrainButtonActionListener());
        centre.add(toggleButton);

        JButton clearCanvas = new JButton("Clear");
        clearCanvas.setPreferredSize(new Dimension(width, height));
        clearCanvas.addActionListener(new ClearButtonActionListener());
        centre.add(clearCanvas);

        JButton save = new JButton("Save");
        save.setPreferredSize(new Dimension(width, height));
        save.addActionListener(new ButtonSaveActionListener());
        centre.add(save);

        JButton buttonGuess = new JButton("Guess");
        buttonGuess.setPreferredSize(new Dimension(width, height));
        buttonGuess.addActionListener(new ButtonGuessActionListener());
        centre.add(buttonGuess);
    }

    /*
     * Adds a panel which is used to display the networks guess
     */
    private void addPredictPanel() {
        predict = new ImagePanel(filePath);
        add(predict);
    }

    /*
     * Template to create a new JPanel
     */
    private JPanel newPanel(int x, int y, int width, int height) {
        JPanel temp = new JPanel();
        temp.setBounds(x, y, width, height);
        temp.setBackground(Color.GRAY);
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
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            /*Converts the image the user drew into a 28 by 28 png and stores
             *the grey scale values of each pixel in a double array which are
             *then put into the network and the largest output is the guess which
             *is then used to display the image associated with the guess
             */
            ImageConverter imageConverter = new ImageConverter();
            double[] input = imageConverter.getInput();
            double[] output = network.feedForward(input);
            int guess = network.getGuess(output);
            filePath = getImagePath(guess);

            //sets the confidence of the networks guess
            predictionUI.setConfidence(network.feedForward(input));

            //load input image into predict panel
            loadPredictPanel();
            if (toggleButtonPressed) {
                int reply = JOptionPane.showConfirmDialog(null, "Did it guess right?", "", JOptionPane.YES_NO_OPTION);
                if (reply == 0) {
                    //if it got it right train with current values
                    for (int i = 0; i < 10; i++) {
                        network.train(input, new Function().getTarget(guess));
                    }
                } else if (reply == 1) {
                    //else ask for what it actually was and train with that value
                    String[] targetValues = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};//available epoch values
                    String target = (String) JOptionPane.showInputDialog(null, "What number did you draw?", "Train", JOptionPane.QUESTION_MESSAGE, null, targetValues, targetValues[0]);
                    if (target != null) {
                        for (int i = 0; i < 10; i++) {
                            network.train(input, new Function().getTarget(Integer.parseInt(target)));
                        }
                    }
                }
            }
        }

        private void loadPredictPanel() {
            remove(predict);//removes current prediction
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
            remove(canvas);//removes current drawing
            remove(predict);//removes current prediction
            addCanvasPanel();//adds the new canvas panel
            addPredictPanel();//adds the new predict panel
            repaint();//redraws the panels
            revalidate();
        }
    }

    private class TrainButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            toggleButtonPressed = !toggleButtonPressed;
        }
    }
}