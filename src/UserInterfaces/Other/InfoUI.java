package UserInterfaces.Other;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InfoUI extends JFrame {
    private int x = 900;
    private int y = 1040;

    public InfoUI() {
        createUI();
        setTitle("Instructions");
        setSize(x, y);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void createUI() {
        addLabel();
        addOkButton();
        addPanel();
    }

    private void addOkButton() {
        JButton button = new JButton("Ok");
        button.setSize(150, 30);
        button.setLocation(375, 970);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        getContentPane().add(button);
    }

    private void addLabel() {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setText("\t\t\t\t       How to use the program\n" +
                "    Saving and loading files:\n" +
                "\t\tsaving:\n" +
                "\t\t\t\t  To save the configurations of a network on the centre window there is a save button which\n" +
                "\t\t\t\t  when pressed will store the configuration of the network in a file.\n" +
                "\t\t\t\t\t\t\t\n" +
                "\t\tLoading:\n" +
                "\t\t\t\t  You will be given the option to load a file, if you decide to do so, you will be given a\n" +
                "\t\t\t\t  list of all the available save files. The name of the file will show the settings of the\n" +
                "\t\t\t\t  network that the weights belong to, for example, the name '(1)784-50-20-10' is the save\n" +
                "\t\t\t\t  file for a network with 784 input neurons, 50 hidden neurons in layer 1, 20 hidden neurons\n" +
                "\t\t\t\t  in layer 2 and 10 neurons in the output layer.\n" +
                "\t\t\t\t\t\t\t\n" +
                "    Setting the network configurations\n" +
                "\t\tNumber of hidden layers:  \n" +
                "\t\t\t\t  The network can have a minimum of 1 layer. The more layers you add the longer it will take\n" +
                "\t\t\t\t  for the network to train. A recommended number of layers to get the maximum accuracy of the\n" +
                "\t\t\t\t  network is 2.\n" +
                "\t\t\t\t  \n" +
                "\t\tNumber of hidden neurons: \n" +
                "\t\t\t\t  The more neurons you add into each hidden layer the longer it will take for the network to\n" +
                "\t\t\t\t  train and will become less accurate. The recommended number of neurons in a 2 layer network \n" +
                "\t\t\t\t  is 50 in each layer.\n" +
                "\t\t\t\t  \n" +
                "\t\tBatch size:\t\t\t\t\n" +
                "\t\t\t\t  The batch size is the total amount of training data that the network will use to train itself.\n" +
                "\t\t\t\t  The maximum batch size for the network is 60000. To get the best accuracy for the network the\n" +
                "\t\t\t\t  recommended batch size is 60000.\n" +
                "\t\t\t\t\t\t\t\t\n" +
                "\t\tNumber of epochs:\n" +
                "\t\t\t\t  The number of epochs is how many times the network trains itself using a full batch. For \n" +
                "\t\t\t\t  example, with a batch size of 60000 and 3 epochs it would train the network with the 60000\n" +
                "\t\t\t\t  pieces of training data fully 3 times. The epochs is limited to a maximum of 10 as any more\n" +
                "\t\t\t\t  will take to long and wont effect the accuracy much. The recommended number of epochs for a\n" +
                "\t\t\t\t  network with 2 hidden layers, 50 neurons in each layer, a batch size of 60000 is 5 epochs.\n" +
                "\t\n" +
                "    Using the interface\n" +
                "\tCentre window:\n" +
                "\t\tGuess button:\n" +
                "\t\t\t\t  When this button is pressed the network will attempt to correctly guess the number you drew.\n" +
                "\t\t\t\t\t\t\t\t\n" +
                "\t\tCanvas (Panel on the left):\n" +
                "\t\t\t\t  This is where you can draw the number you want the network to guess. \n" +
                "\t\t\t\t \n" +
                "\t\tprediction (Panel on the right):\n" +
                "\t\t\t\t  Once the guess button is pressed the prediction panel will display the number that the network\n" +
                "\t\t\t\t  thinks you drew.\n" +
                "\t\t\n" +
                "\t\tClear button:\n" +
                "\t\t\t\t  When this button is pressed anything drawn on the canvas will be removed and reverted back to\n" +
                "\t\t\t\t  a clear screen.\n" +
                "\t\t\t\t  \n" +
                "\t\tSave button:\n" +
                "\t\t\t\t  When this button is pressed the program will save the current configuration of the network. \n" +
                "\t\t\t\t  \n" +
                "\tConfidence Window (window on the right):\n" +
                "\t\t\t\t  This window shows how 'confident' the network is in it's guess. It uses percentages to show\n" +
                "\t\t\t\t  how confident it is. For example, if you drew the number 3 and the network guessed 3 then the\n" +
                "\t\t\t\t  percentage will be larger for the number 3 than the other numbers.");
        textArea.setLocation(0, 0);
        textArea.setSize(x, y - 70);
        getContentPane().add(textArea);
    }

    private void addPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setSize(x, y);
        getContentPane().add(panel);
    }
}

