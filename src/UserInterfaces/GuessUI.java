package UserInterfaces;

import NeuralNetwork.Function;
import NeuralNetwork.Network;
import ProcessingData.ImageConverter;
import ProcessingData.LoadDataSet;
import ProcessingData.LoadFile;
import ProcessingData.SaveFile;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class GuessUI {
    private Network network;
    private Stage guess;

    GuessUI() {
        guess = new Stage();
        guess.setTitle("Neural Network - Hand Written Digit Recognition - (784_100H_10)[0.14]");
        guess.setResizable(false);

        loadFile(true); /*Load the default network config*/

        //set up user interface
        ConfidenceUI confidenceUI = setUpConfidenceWindow();

        BorderPane mainWindow = getMainWindow(confidenceUI);

        Scene scene = new Scene(mainWindow, 895, 600);
        scene.getStylesheets().add("Styles.css");

        guess.setScene(scene);
        guess.show();
    }

    /*
     * Creates all the components that are in the main window
     */
    private BorderPane getMainWindow(ConfidenceUI confidenceUI) {
        BorderPane borderPane = new BorderPane();

        //Create Menu bar
        MenuBar menuBar = getMenuBar();

        //Create canvas
        Canvas draw = new Canvas(405, 520);
        GraphicsContext draw_gc = draw.getGraphicsContext2D();
        draw.setTranslateX(20);
        draw.setTranslateY(20);

        //Create image pane
        Text number = getNumber();
        Pane imagePane = getImagePane(number);

        //Create draw pane
        Pane drawPane = addDrawCanvas(draw, draw_gc);

        //Add buttons
        FlowPane buttons = addButtons(draw, draw_gc, number, confidenceUI);

        //Set the layout for each pane
        borderPane.setTop(menuBar);
        borderPane.setLeft(drawPane);
        borderPane.setRight(imagePane);
        borderPane.setBottom(buttons);
        return borderPane;
    }

    /*
     * Returns a MenuBar which gives
     */
    private MenuBar getMenuBar() {
        Menu menu = new Menu("Tips");
        MenuItem instructions = new MenuItem("Instructions");
        instructions.setOnAction(event -> showInstruction());
        menu.getItems().add(instructions);
        return new MenuBar(menu);
    }

    /*
     * Returns a Text object which displays the number the network has predicted
     */
    private Text getNumber() {
        Text number = new Text("");
        number.setFont(Font.font("Dialog", FontWeight.NORMAL, FontPosture.REGULAR, 500));
        number.setX(100);
        number.setY(430);
        return number;
    }

    /*
     * Returns the pane which contains the displayed number
     */
    private Pane getImagePane(Text number) {
        Pane pane = new Pane(number);
        pane.getStyleClass().add("canvas");
        pane.setPrefWidth(465);
        return pane;
    }

    /*
     * Creates the confidence window
     */
    private ConfidenceUI setUpConfidenceWindow() {
        ConfidenceUI confidenceUI = new ConfidenceUI();
        Stage prediction = new Stage();
        prediction.setTitle("Neural Network - Confidence");
        prediction.setResizable(false);
        prediction.setScene(confidenceUI.getScene());
        prediction.setX(1415);
        prediction.setY(135);
        prediction.show();
        return confidenceUI;
    }

    /*
     * Creates a pane that the user can draw on
     */
    private Pane addDrawCanvas(Canvas draw, GraphicsContext gc) {
        gc.setLineWidth(25);//Brush size
        gc.setLineCap(StrokeLineCap.ROUND);
        gc.setLineJoin(StrokeLineJoin.ROUND);
        Line line = new Line();

        //when the mouse is pressed create the start of the line
        draw.setOnMousePressed(event -> {
            gc.beginPath();
            gc.setStroke(Color.BLACK);
            gc.setFill(Color.BLACK);
            line.setStartX(event.getX());
            line.setStartY(event.getY());
            gc.closePath();
        });

        //when the user drags the mouse move the line to the location of the mouse
        draw.setOnMouseDragged(event -> {
            gc.lineTo(event.getX(), event.getY());
            gc.stroke();
        });

        Pane pane = new Pane();
        pane.getChildren().addAll(draw);
        pane.getStyleClass().add("canvas");
        return pane;
    }

    /*
     * Create all the buttons needed and add them to a FlowPane
     */
    private FlowPane addButtons(Canvas draw, GraphicsContext draw_gc, Text number, ConfidenceUI confidenceUI) {
        int width = 151, height = 50;

        ToggleButton trainButton = new ToggleButton("Train");
        trainButton.setPrefSize(width, height);

        Button clearButton = getClearButton(draw, draw_gc, number, width, height);

        Button saveButton = getSaveButton(width, height);

        Button loadButton = getLoadButton(width, height);

        Button createButton = getCreateButton(width, height);

        Button guessButton = getGuessButton(draw, number, confidenceUI, width, height, trainButton);

        return new FlowPane(trainButton, clearButton, saveButton, loadButton, createButton, guessButton);
    }

    /*
     * Creates a button that when pressed gives the user the option to create their own network
     */
    private Button getCreateButton(int width, int height) {
        Button createButton = new Button("Create new Network");
        createButton.setPrefSize(width, height);

        createButton.setOnAction(event -> {
            Stage settingsStage = new Stage();
            SettingsUI settings = new SettingsUI(settingsStage);
            settingsStage.showAndWait();            /*wait for the user to enter network settings*/
            if (!settingsStage.isShowing()) {
                //train network and update title
                trainNetwork(settings.getHiddenNeurons(), settings.getBatchSize(), settings.getEpochs(),
                        settings.getLearningRate());
                guess.setTitle("Neural Network - Hand Written Digit Recognition - " + network.getConfig());
            }
        });
        return createButton;
    }

    /*
     * Creates a button that allows the user to load any network that has been saved
     */
    private Button getLoadButton(int width, int height) {
        Button loadButton = new Button("Load saved Network");
        loadButton.setPrefSize(width, height);

        loadButton.setOnAction(event -> {
            loadFile(false);
            guess.setTitle("Neural Network - Hand Written Digit Recognition - " + network.getConfig());
        });
        return loadButton;
    }

    /*
     * Creates a button that passes the image the user drew into the network
     * and displays the networks guess to the user
     */
    private Button getGuessButton(Canvas draw, Text number, ConfidenceUI confidenceUI, int width, int height,
                                  ToggleButton trainButton) {
        Button guessButton = new Button("Guess");
        guessButton.setPrefSize(width, height);

        guessButton.setOnAction(event -> {
            getDrawing(draw, new File("Resources\\image.png"));

            double[] input = new ImageConverter().getInput();
            int guess = inputImageIntoNetwork(number, confidenceUI, input);

            /*
             * If the train button is toggled then ask the user whether the network guessed correctly
             * if it was correct then it will retrain the network
             */
            if (trainButton.isSelected()) {
                Alert alert = conformationAlert(null, "Did it guess right?");
                Optional<ButtonType> response = alert.showAndWait();
                if (response.get().getText().equalsIgnoreCase("Yes")) {
                    retrain(true, input, guess);
                } else if (response.get().getText().equalsIgnoreCase("no")) {
                    retrain(false, input, guess);
                }
            }
        });
        return guessButton;
    }

    /*
     * Feeds the image (stored as a double array) the user has drawn into the network
     * and returns the final guess of the network
     */
    private int inputImageIntoNetwork(Text number, ConfidenceUI confidenceUI, double[] input) {
        double[] output = network.feedForward(input);
        int guess = network.getGuess(output);
        confidenceUI.updateValues(output);
        number.setText(Integer.toString(guess));
        return guess;
    }

    /*
     * Screenshots the image the user has drawn, inverts the pixels as the network is trained with images with
     * black backgrounds and white ink and the user has drawn with a white background and black ink,
     * then creates the image as a png
     */
    private void getDrawing(Canvas draw, File file) {
        try {
            WritableImage writableImage = new WritableImage((int) Math.round(draw.getWidth()),
                    (int) Math.round(draw.getHeight()));
            draw.snapshot(null, writableImage);
            invertPixelValues(writableImage, draw);
            BufferedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
            ImageIO.write(renderedImage, "png", file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /*
     * Trains the network 20 times based on whether it's initial guess was correct,
     * if it was correct then the network is trained with the target value it guessed,
     * if it was incorrect then the user is told to enter the number they drew and once
     * selected that number is assigned as the target value
     */
    private void retrain(boolean correct, double[] input, int guess) {
        if (correct) {
            for (int i = 0; i < 20; i++) {
                network.train(input, new Function().getTarget(guess));
            }
        } else {
            String[] targetValues = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
            String target = getChoiceAlert(targetValues, "Train", "Which number did you draw");
            if (target != null) {
                for (int i = 0; i < 20; i++) {
                    network.train(input, new Function().getTarget(Integer.parseInt(target)));
                }
            }
        }
    }

    /*
     * Creates a button that allows the user to save the current neural network that is being used
     */
    private Button getSaveButton(int width, int height) {
        Button saveButton = new Button("Save Network");
        saveButton.setPrefSize(width, height);

        saveButton.setOnAction(event -> {
            Alert alert = conformationAlert("Save File?",
                    "Do you want to save the current configuration?");
            Optional<ButtonType> response = alert.showAndWait();
            if (response.get().getText().equalsIgnoreCase("yes")) {
                SaveNetwork();
            }
        });
        return saveButton;
    }

    /*
     * Saves the weights in a text file and names the file with the config of the network
     */
    private void SaveNetwork() {
        double[] weights = network.getWeights();
        new SaveFile().save("Resources\\SaveFiles\\" + network.getConfig(), weights);

        //Lets the user know that the file has been saved
        Alert done = new Alert(Alert.AlertType.INFORMATION);
        done.setTitle("File saved!");
        done.setContentText("The file has been saved and named: " + network.getConfig());
        done.showAndWait();
    }

    /*
     * Creates a button that clears the canvas and the guess display pane
     */
    private Button getClearButton(Canvas draw, GraphicsContext draw_gc, Text number, int width, int height) {
        Button clearCanvasButton = new Button("Clear Canvas");
        clearCanvasButton.setPrefSize(width, height);

        clearCanvasButton.setOnAction(event -> {
            draw_gc.clearRect(0, 0, draw.getWidth(), draw.getHeight());
            number.setText("");
        });
        return clearCanvasButton;
    }

    /*
     * Trains a network with hyper-parameters provided by the user
     * i.e. hidden neurons, batch size, epochs and learning rate
     */
    private void trainNetwork(int[] hiddenNeurons, int batchSize, int epochs, double learningRate) {
        Stage loadingStage = new Stage();
        ProgressBar loading = getLoadingBar(loadingStage);

        network = new Network(learningRate, 784, 10, hiddenNeurons);
        Task train = train(batchSize, epochs);

        loading.progressProperty().bind(train.progressProperty());      /*Binds the two process*/
        new Thread(train).start();
        loadingStage.show();        /*Displays the loading window*/

        //stop the training process and close the loading window once the training is complete
        train.setOnSucceeded(closeEvent -> {
            loadingStage.close();
            train.cancel();
        });
        loadingStage.setOnCloseRequest(closeEvent -> System.exit(0));
    }

    /*
     * Creates a thread which trains the network and updates the progress bar simultaneously
     */
    private Task train(int batchSize, int epochs) {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                double max = batchSize * epochs;
                double current = 0;
                //Load the training data
                LoadDataSet trainingData = new LoadDataSet(batchSize, "Resources\\mnist_train.csv");
                //Trains the network
                for (int j = 0; j < epochs; j++) {
                    for (int i = 0; i < batchSize; i++) {
                        network.train(trainingData.getInputData(i), trainingData.getLabel(i));
                        current++;
                        updateProgress(current, max);   /*Updates the progress bar*/
                    }
                    trainingData.randomiseTrainingData();
                }
                return null;
            }
        };
    }

    /*
     * Creates a loading bar for the loading stage when creating a network
     */
    private ProgressBar getLoadingBar(Stage loadingStage) {
        loadingStage.setTitle("Training Please Wait...");
        ProgressBar loading = new ProgressBar(0);
        loading.setPrefSize(400, 60);
        loading.setStyle("-fx-accent: #5bc2e7");
        Label progress = new Label("Progress: ");
        progress.setTranslateX(10);
        progress.setTranslateY(10);
        progress.setStyle("-fx-font-size: 25");
        HBox hBox = new HBox(progress, loading);
        hBox.setSpacing(20);
        Scene loadingScene = new Scene(hBox, 530, 60);
        loadingStage.setScene(loadingScene);
        return loading;
    }

    /*
     * Loads a network that the user chooses, if the program is first starting up then a default
     * file is loaded
     */
    private void loadFile(boolean startUp) {
        String saveFile;
        if (startUp) {
            saveFile = "(784_100H_10)[0.14].txt"; /*Default network*/
        } else {
            //Gives the user the current options
            String[] files = getFileNames();
            saveFile = getChoiceAlert(files, "Choose a config file", "Choose your preset:");
        }
        //set the weights for the network if the user chose a file
        if (saveFile != null) {
            int[] hiddenNeurons = getNumOfHiddenNeurons(saveFile);
            double learningRate = getLearningRate(saveFile);
            network = new Network(learningRate, 784, 10, hiddenNeurons);
            double[] weights = new LoadFile().loadWeights("Resources\\SaveFiles\\" + saveFile);
            network.setWeights(weights);
        }
    }

    /*
     * Returns the learning rate for the network chosen by the user
     */
    private double getLearningRate(String saveFile) {
        int pos = 0;
        for (int i = 0; i < saveFile.length(); i++) {
            if (saveFile.charAt(i) == '[') {
                pos = i;
                break;
            }
        }
        return Double.parseDouble(saveFile.substring(pos + 1, saveFile.length() - 5));
    }

    /*
     * Returns the names of all the files in the folder 'SaveFiles'
     */
    private String[] getFileNames() {
        File folder = new File("Resources\\SaveFiles");
        File[] files = folder.listFiles();
        String[] fileNames = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            fileNames[i] = files[i].getName();
        }
        return fileNames;
    }

    /*
     * Returns the number of hidden neurons that are in the network chosen by the user
     */
    private int[] getNumOfHiddenNeurons(String saveFile) {
        ArrayList<Integer> temp = new ArrayList<>();
        saveFile = getTopology(saveFile);
        Pattern pattern = Pattern.compile("(\\d+H)");
        Matcher matcher = pattern.matcher(saveFile);
        while (matcher.find()) {
            String num = saveFile.substring(matcher.start(), matcher.end() - 1);
            temp.add(Integer.parseInt(num));
        }
        return temp.stream().mapToInt(i -> i).toArray();
    }

    /*
     * Returns the topology of the network (how many neurons are in each layer and how many hidden layers)
     */
    private String getTopology(String saveFile) {
        int end = 0;
        for (int i = 0; i < saveFile.length(); i++) {
            if (saveFile.charAt(i) == ')') {
                end = i;
            }
        }
        saveFile = saveFile.substring(1, end);
        return saveFile;
    }


    /*
     * Inverts the pixel brightness (colour) of an image
     */
    private void invertPixelValues(WritableImage writableImage, Canvas draw) {
        PixelWriter writer = writableImage.getPixelWriter();
        PixelReader reader = writableImage.getPixelReader();
        for (int x = 0; x < draw.getWidth(); x++) {
            for (int y = 0; y < draw.getHeight(); y++) {
                int alpha = 255;
                int value = reader.getArgb(x, y);
                value = 255 - value;
                int p = (alpha << 24) | (value << 16) | (value << 8) | value;
                writer.setArgb(x, y, p);
            }
        }
    }


    /*
     * Displays the instructions to the user which are stored in a text file
     */
    private void showInstruction() {
        Stage stage = new Stage();
        int width = 1000, height = 1000;

        TextArea instructions = getInstructions(width, height);

        Button okButton = getOkButton(stage);

        VBox v = new VBox(instructions, okButton);
        stage.setScene(new Scene(v, width, height));
        stage.show();
    }

    /*
     * Creates a textArea that displays the instructions
     */
    private TextArea getInstructions(int width, int height) {
        TextArea instructions = new TextArea();
        instructions.setPrefSize(width, height);
        instructions.setEditable(false);
        instructions.setText(new LoadFile().loadTextFile("Resources\\Instructions.txt"));
        instructions.setStyle("-fx-font-size: 1.5em;");
        return instructions;
    }

    /*
     * Creates a Button that closes the window once pressed
     */
    private Button getOkButton(Stage stage) {
        Button okButton = new Button("Ok");
        okButton.setTranslateX(350);
        okButton.setPrefWidth(300);
        okButton.setPrefHeight(100);
        okButton.setOnAction(actionEvent -> stage.close());
        return okButton;
    }


    /*
     * Creates a pop up message giving the user options on what to pick.
     * It returns the option chosen by the user, if they close the window it
     * returns null.
     */
    private String getChoiceAlert(String[] options, String title, String contentText) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(options[0], options);
        dialog.setTitle(title);
        dialog.setContentText(contentText);
        Optional<String> response = dialog.showAndWait();
        return response.orElse(null);
    }

    /*
     * Creates a pop up message giving the user information stored in
     * the string contentText
     */
    private Alert conformationAlert(String title, String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(contentText);
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yesButton, noButton, cancelButton);
        return alert;
    }
}