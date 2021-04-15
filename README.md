## Hand-written digit recognition using a neural network

This was made for my A-Level coursework, I created a neural network which is able to classify drawings of numbers from 0-9.

## Prerequisites

You will need java 8 to run this program, as javafx was removed in JDK 11 onwards.

## How to run the program

Download the Neural-Network-1.0-SNAPSHOT-shaded.jar file

Then simply run the file by double-clicking it

If the file does not run, try using the command line to run the file. This is down by changing the directory to where
the jar file is located on you computer and running:

```
java -jar Neural-Network-1.0-SNAPSHOT-shaded.jar
```

# How to use the program

# Saving and loading files:

## Saving:

To save the configurations of a network on the centre window there is a save button which when pressed will store the
configuration of the network in a text `file.

## Loading:

You will be given the option to load a file, if you decide to do so, you will be given a list of all the available save
files. The name of the file will show the settings of the network that the weights belong to, for example, the name '(
784_50_20_10)[0.13]' is the save file for a network with 784 input neurons, 50 hidden neurons in layer 1, 20 hidden
neurons in layer 2, 10 neurons in the output layer and a learning rate of 0.13.

# Setting the network configurations Number of hidden layers:

The network can have a minimum of 1 hidden layer. The more layers you add the longer it will take for the network to
train. A recommended number of layers to get the maximum accuracy of the network is just 1-2. You can not have any more
than 5 hidden layers!

## Number of hidden neurons:

The more neurons you add into each hidden layer the longer it will take for the network to train and will become less
accurate. The recommended number of neurons in a single layered network is 50-100.

## Batch size:

The batch size is the total amount of training data that the network will use to train itself. The maximum batch size
for the network is 60,000. To get the best accuracy for the network the recommended batch size is 60,000.

## Number of epochs:

The number of epochs is how many times the network trains itself using a full batch. For example, with a batch size of
60000 and 3 epochs it would train the network with the 60000 pieces of training data fully 3 times. The epochs is
limited to a maximum of 10 as anymore will take too long and won't affect the accuracy much. The recommended number of
epochs for a network with 2 hidden layers, 50 neurons in each layer, a batch size of 60000 is 5 epochs.

## Learning rate:

The learning rate determines how much the network learns when updating the weights. The learning rate typically ranges
between 0.0 and 1.0. A recommended configuration is a single hidden layer network with 50 hidden neurons and a learning
rate of 0.14.

# Using the interface

## Centre window:

### Guess button:

After this button is pressed the network will attempt to correctly guess the number you drew by displaying it's guess on
the prediction panel.

### Canvas (Panel on the left):

This is where you can draw the number you want the network to guess.

### Prediction (Panel on the right):

After the guess button is pressed the prediction panel will display the number that the network thinks you drew.

### Clear button:

When this button is pressed anything drawn on the canvas will be removed and reverted to a clear screen.

### Save button:

After this button is pressed the program will save the current configuration of the network.

###Train button:
If this button is turned on then every time the network makes a guess the user will be asked whether the network guessed
correctly, if it did then the network will be trained to improve its prediction, if not, the user can select the number
that they drew, and the network will then train itself on the selected number.

##Confidence Window (window on the right):
This window shows how 'confident' the network is in its guess. It uses percentages to show how confident it is. For
example, if you drew the number 3, and the network guessed 3 then the percentage will be larger for the number 3 than the
other numbers. The colour of the prediction bar also shows its confidence. A confidence value between 0-20 is red, a
value between 20-40 is yellow, a value between 40-80 is orange, and a value between 80-100 is green.