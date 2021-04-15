# Hand-written digit recognition using a neural network

This was made for my A-Level coursework. The UI, neural network and data processing is split into separate packages.

Download Neural-Network-1.0-SNAPSHOT-shaded.jar

							How to use the program
    Saving and loading files:
		Saving:
				  To save the configurations of a network on the centre window there is a save button which
				  when pressed will store the configuration of the network in a text `file.
							
		Loading:
				  You will be given the option to load a file, if you decide to do so, you will be given a
				  list of all the available save files. The name of the file will show the settings of the
				  network that the weights belong to, for example, the name '(784_50_20_10)[0.13]' is the save
				  file for a network with 784 input neurons, 50 hidden neurons in layer 1, 20 hidden neurons
				  in layer 2, 10 neurons in the output layer and a learning rate of 0.13.
							
    Setting the network configurations
		Number of hidden layers:  
				  The network can have a minimum of 1 hidden layer. The more layers you add the longer it will take
				  for the network to train. A recommended number of layers to get the maximum accuracy of the
				  network is just 1-2. You can not have any more than 5 hidden layers!
				  
		Number of hidden neurons: 
				  The more neurons you add into each hidden layer the longer it will take for the network to
				  train and will become less accurate. The recommended number of neurons in a single layered
				  network is 50-100.
				  
		Batch size:				
				  The batch size is the total amount of training data that the network will use to train itself.
				  The maximum batch size for the network is 60,000. To get the best accuracy for the network the
				  recommended batch size is 60,000.
								
		Number of epochs:
				  The number of epochs is how many times the network trains itself using a full batch. For 
				  example, with a batch size of 60000 and 3 epochs it would train the network with the 60000
				  pieces of training data fully 3 times. The epochs is limited to a maximum of 10 as any more
				  will take too long and wont effect the accuracy much. The recommended number of epochs for a
				  network with 2 hidden layers, 50 neurons in each layer, a batch size of 60000 is 5 epochs.
				  
		Learning rate: 
				  The learning rate determines how much the network learns when updating the weights. The 
				  learning rate typically ranges between 0.0 and 1.0. A recommended configuration is a 
				  single hidden layer network with 50 hidden neurons and a learning rate of 0.14.
	
    Using the interface
	Centre window:
		Guess button:
				  When this button is pressed the network will attempt to correctly guess the number you drew by
				  displaying it's guess on the prediction panel.
								
		Canvas (Panel on the left):
				  This is where you can draw the number you want the network to guess. 
				 
		Prediction (Panel on the right):
				  Once the guess button is pressed the prediction panel will display the number that the network
				  thinks you drew.
		
		Clear button:
				  When this button is pressed anything drawn on the canvas will be removed and reverted back to
				  a clear screen.
				  
		Save button:
				  When this button is pressed the program will save the current configuration of the network.

		Train button:
				  If this button is turned on then every time the network makes a guess the user will be asked
				  whether the network guessed correctly, if it did then the network will be trained to improve 
				  it's prediction, if not, the user can select the number that they drew and the network will
				  then train itself on the selected number.
				  
	Confidence Window (window on the right):
				  This window shows how 'confident' the network is in it's guess. It uses percentages to show
				  how confident it is. For example, if you drew the number 3 and the network guessed 3 then the
				  percentage will be larger for the number 3 than the other numbers. The colour of the prediction
				  bar also shows it's confidence. A confidence value between 0-20 is red, a value between 20-40
				  is yellow, a value between 40-80 is orange and a value between 80-100 is green.