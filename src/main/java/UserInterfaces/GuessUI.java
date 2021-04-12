package UserInterfaces;

import NeuralNetwork.Function;
import NeuralNetwork.Network;
import ProcessingData.ImageConverter;
import ProcessingData.LoadDataSet;
import ProcessingData.LoadFile;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.Optional;


class GuessUI {
    private Network network;
    private final Stage GUESS;
    private final Stage CONFIDENCE = new Stage();

    GuessUI() {
        GUESS = new Stage();
        GUESS.setTitle("Neural Network - Hand Written Digit Recognition - (784_100H_10)[0.14]");
        GUESS.getIcons().add(new Image("https://cdn4.vectorstock.com/i/1000x1000/01/68/pen-icon-vector-23190168.jpg"));
        GUESS.setResizable(false);


        network = loadFile(true); /*Load the default network config*/

        //set up user interface
        ConfidenceUI confidenceUI = setUpConfidenceWindow();
        BorderPane mainWindow = getMainWindow(confidenceUI);

        Scene scene = new Scene(mainWindow, 895, 600);
        scene.getStylesheets().add("styles.css");

        GUESS.setOnCloseRequest(event -> System.exit(0));//exit program if the main window is closed
        GUESS.setScene(scene);
        GUESS.show();
    }

    /*
     * Creates all the components that are in the main window
     */
    private BorderPane getMainWindow(ConfidenceUI confidenceUI) {
        BorderPane borderPane = new BorderPane();

        //Create Menu bar
        MenuBar menuBar = getMenuBar();

        //Create canvas
        Canvas draw = new Canvas(405, 495);
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
     * Returns a MenuBar which when selected displays the instructions on how
     * to use the program
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
        CONFIDENCE.setTitle("Neural Network - Confidence");
        CONFIDENCE.getIcons().add(new Image("https://media.istockphoto.com/vectors/magnifier-search-prediction-icon-with-name-vector-id1072898784"));
        CONFIDENCE.setResizable(false);
        CONFIDENCE.setScene(confidenceUI.getScene());
        CONFIDENCE.setX(1415);
        CONFIDENCE.setY(135);
        CONFIDENCE.show();
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
            settingsStage.showAndWait();/*wait for the user to enter network settings*/
            if (!settingsStage.isShowing()) {
                //train network and update title
                if (settings.getHiddenNeurons() != null && settings.getBatchSize() != -1 && settings.getEpochs() != -1 && settings.getLearningRate() != -1) {
                    GUESS.close();
                    CONFIDENCE.close();
                    trainNetwork(settings.getHiddenNeurons(), settings.getBatchSize(), settings.getEpochs(),
                            settings.getLearningRate());
                    GUESS.setTitle("Neural Network - Hand Written Digit Recognition - " + network.getConfig());
                }
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
            network = loadFile(false);
            if (network != null) {
                GUESS.setTitle("Neural Network - Hand Written Digit Recognition - " + network.getConfig());
            }
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
            String[] exploded;
            StringBuilder path;
            try {
                path = new StringBuilder(this.getClass().getResource(this.getClass().getSimpleName() + ".class").toString());
                exploded = path.toString().split("/");

                int endIndex = 0;
                for (int i = 0; i < exploded.length; i++) {
                    if (exploded[i].equals("Neural-Network-1.0-SNAPSHOT-shaded.jar!")) {
                        endIndex = i;
                    }
                }
                path = new StringBuilder();
                for (int i = 1; i < endIndex; i++) {
                    path.append(exploded[i]).append("/");
                }

                path = new StringBuilder(path.toString().replace("%20", " "));

                File image = new File(path + "image.png");

                getDrawing(draw, image);

                double[] input = new ImageConverter().getInput(path.toString());
                image.delete();
                int guess = 0;
                if (Double.isNaN(input[0])) {
                    Alert alert = conformationAlert("Please draw a number", "Please draw a number from 0 - 9", false);
                    alert.showAndWait();
                } else {
                    guess = inputImageIntoNetwork(number, confidenceUI, input);/*Inputs image into neural network*/
                }
                /*
                 * If the train button is toggled then ask the user whether the network guessed correctly
                 * if it was correct then it will retrain the network
                 */
                if (trainButton.isSelected() && !Double.isNaN(input[0])) {
                    askUserToTrainNetwork(input, guess);
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error", Arrays.toString(e.getStackTrace()));
            }
        });
        return guessButton;
    }

    /*
     * Creates an alert for the user asking them whether or not the network guessed correctly
     */
    private void askUserToTrainNetwork(double[] input, int guess) {
        Alert alert = conformationAlert("Train", "Did it guess right?", true);
        Optional<ButtonType> response = alert.showAndWait();
        if (response.isPresent()) {
            if (response.get().getText().equalsIgnoreCase("Yes")) {
                //if it did guess correctly then train the network with the number that it guessed
                retrain(true, input, guess);
            } else if (response.get().getText().equalsIgnoreCase("no")) {
                /* if it did not guess correctly ask the user what number they drew and train
                 * the network with that number
                 */
                retrain(false, input, guess);
            }
        }
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
            WritableImage writableImage = new WritableImage((int) Math.round(draw.getWidth()), (int) Math.round(draw.getHeight()));
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
            conformationAlert("Trained", "Network has been trained", false).showAndWait();
        } else {
            String[] targetValues = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
            String target = getChoiceAlert(targetValues, "Train", "Which number did you draw");
            if (target != null) {
                for (int i = 0; i < 20; i++) {
                    network.train(input, new Function().getTarget(Integer.parseInt(target)));
                }
                conformationAlert("Trained", "Network has been trained.", false).showAndWait();
            } else {
                askUserToTrainNetwork(input, guess);
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
                    "Do you want to save the current configuration?", true);
            Optional<ButtonType> response = alert.showAndWait();
            if (response.isPresent()) {
                if (response.get().getText().equalsIgnoreCase("yes")) {
                    DirectoryChooser dc = new DirectoryChooser();
                    File file = dc.showDialog(GUESS);
                    if (file != null) {
                        saveNetwork(file.getAbsolutePath());
                    }
                }
            }
        });
        return saveButton;
    }

    /*
     * Saves the weights in a text file and names the file with the config of the network
     */
    private void saveNetwork(String fileDir) {
        serialize(network, fileDir + "/" + network.getConfig());
        //Lets the user know that the file has been saved
        Alert done = new Alert(Alert.AlertType.INFORMATION);
        done.setTitle("File saved!");
        done.setContentText("The file has been saved and named: " + network.getConfig());
        done.showAndWait();
    }

    private void serialize(Object obj, String filePath) {
        try {
            FileOutputStream in = new FileOutputStream(filePath + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(in);
            out.writeObject(obj);
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            GUESS.show();
            CONFIDENCE.show();
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
                LoadDataSet trainingData = new LoadDataSet(batchSize, "Misc/mnist_train.csv");
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
        loading.setStyle("-fx-accent: #5BC2E7");
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
    private Network loadFile(boolean startUp) {
        String saveFile;
        if (startUp) {
            saveFile = "(784_100H_10)[0.14].ser"; //Default network
            return (Network) deserialize("SaveFiles/" + saveFile, startUp);
        } else {
            //Gives the user the current options
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("ser files", "*.ser"));
            File file = fc.showOpenDialog(GUESS);
            if (file != null) {
                saveFile = file.getPath();
                return (Network) deserialize(saveFile, startUp);
            }
        }
        return null;
    }

    private Object deserialize(String filePath, boolean startUp) {
        Object obj = null;
        try {
            InputStream inputStream;
            if (startUp) {
                inputStream = this.getClass().getClassLoader().getResourceAsStream(filePath);
            } else {
                inputStream = new FileInputStream(filePath);
            }
            File tmpFile = File.createTempFile("file", "temp");
            assert inputStream != null;
            FileUtils.copyInputStreamToFile(inputStream, tmpFile);
            try {
                FileInputStream fileIn = new FileInputStream(tmpFile);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                obj = in.readObject();
                in.close();
                fileIn.close();
            } finally {
                if (startUp) {
                    tmpFile.delete();
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
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
        stage.setTitle("Instructions");
        stage.getIcons().add(new Image("https://static.thenounproject.com/png/660441-200.png"));
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
        instructions.setText(new LoadFile().loadTextFile("Misc/Instructions.txt"));
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
    private Alert conformationAlert(String title, String contentText, boolean choice) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(contentText);
        if (choice) {
            ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(yesButton, noButton, cancelButton);
        } else {
            ButtonType okButton = new ButtonType("Ok", ButtonBar.ButtonData.YES);
            alert.getButtonTypes().setAll(okButton);
        }
        return alert;
    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);

        if (alertType.equals(Alert.AlertType.ERROR)) {
            TextArea fullMessage = new TextArea(contentText);
            fullMessage.setWrapText(true);
            fullMessage.setEditable(false);
            alert.getDialogPane().setContent(fullMessage);
        } else {
            alert.setContentText(contentText);
        }
        alert.showAndWait();
    }
}