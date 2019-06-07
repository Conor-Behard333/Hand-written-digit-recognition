package UserInterfaces;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InfoUI extends JFrame {
    private int x = 725;
    private int y = 1000;

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
        button.setSize(100, 20);
        button.setLocation(300, 950);
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

        textArea.setText("\t\t\t     How to use the program\n" +
                "    Setting the network configurations\n" +
                "\t\tNumber of hidden layers:  \n" +
                "\t\t\t\t  The network can have a minimum of 1 layer.\n" +
                "\t\t\t\t  The more layers you add the longer it will \n" +
                "\t\t\t\t  take for the network to train. A recommended\n" +
                "\t\t\t\t  number of layers to get the maximum accuracy \n" +
                "\t\t\t\t  of the network is 2.\n" +
                "\t\t\t\t  \n" +
                "\t\tNumber of hidden neurons: \n" +
                "\t\t\t\t  The more neurons you add into each hidden\n" +
                "\t\t\t\t  layer the longer it wil take for the network\n" +
                "\t\t\t\t  to train and will become less accurate. The\n" +
                "\t\t\t\t  recommended number of neurons in a 2 layer\n" +
                "\t\t\t\t  network is 50 in each layer.\n" +
                "\t\t\t\t  \n" +
                "\t\tBatch size:\t\t\t\t\n" +
                "\t\t\t\t  The batch size is the total amount of training\n" +
                "\t\t\t\t  data that the network will use to train itself.\n" +
                "\t\t\t\t  The maximum batch size for the network is 60000.\n" +
                "\t\t\t\t  To get the best accuracy for the network the \n" +
                "\t\t\t\t  recommended batch size is 60000.\n" +
                "\t\t\t\t\t\t\t\t\n" +
                "\t\tNumber of epochs:\n" +
                "\t\t\t\t  The number of epochs is how many times the network\n" +
                "\t\t\t\t  trains intself using a full batch. For example,\n" +
                "\t\t\t\t  with a batch size of 60000 and 3 epochs it would \n" +
                "\t\t\t\t  train the network with the 60000 pieces of training\n" +
                "\t\t\t\t  data fully 3 times. The epochs is limited to a maximum \n" +
                "\t\t\t\t  of 10 as any more will take to long and wont effect the\n" +
                "\t\t\t\t  accuracy much. The recommended number of epochs for a \n" +
                "\t\t\t\t  network with 2 hidden layers, 50 neurons in each layer,\n" +
                "\t\t\t\t  a batch size of 60000 is 5 epochs.\n" +
                "\t\n" +
                "    Using the interface\n" +
                "\tCentre window:\n" +
                "\t\tGuess button:\n" +
                "\t\t\t\t  When this button is pressed the network will attempt\n" +
                "\t\t\t\t  to correctly guess the number you drew.\n" +
                "\t\t\t\t\t\t\t\t\n" +
                "\t\tCanvas (Panel on the left):\n" +
                "\t\t\t\t  This is where you can draw the number you want the \n" +
                "\t\t\t\t  network to guess. \n" +
                "\t\t\t\t \n" +
                "\t\tprediction (Panel on the right):\n" +
                "\t\t\t\t  Once the guess button is pressed the prediction panel\n" +
                "\t\t\t\t  will display the number that the network thinks you \n" +
                "\t\t\t\t  drew.\n" +
                "\t\t\n" +
                "\t\tClear button:\n" +
                "\t\t\t\t  When this button is pressed anything drawn on the canvas\n" +
                "\t\t\t\t  will be removed and reverted back to a clear screen.\n" +
                "\t\t\t\t  \n" +
                "\tConfidence Window (window on the right):\n" +
                "\t\t\t\t  This window shows how 'confident' the network is in it's\n" +
                "\t\t\t\t  guess. It uses percentages to show how confident it is.\n" +
                "\t\t\t\t  For example, if you drew the number 3 and the network\n" +
                "\t\t\t\t  guessed 3 then the percentage will be larger for the\n" +
                "\t\t\t\t  number 3 than the other numbers.\n" +
                "\t\t\t\t\t\t\t\t\n" +
                "\t\t\t\t\t\t\t\t\n" +
                "\t\t\t\t\t\t\t\t  ");
        textArea.setLocation(0, 0);
        textArea.setSize(x, y - 50);
        getContentPane().add(textArea);
    }

    private void addPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setSize(x, y);
        getContentPane().add(panel);
    }
}

