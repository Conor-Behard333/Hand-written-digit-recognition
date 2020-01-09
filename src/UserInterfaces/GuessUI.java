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

    private BorderPane getMainWindow(ConfidenceUI confidenceUI) {
        BorderPane borderPane = new BorderPane();

        //Create menubar
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

    private MenuBar getMenuBar() {
        Menu menu = new Menu("Tips");
        MenuItem instructions = new MenuItem("Instructions");
        instructions.setOnAction(event -> showInstruction());
        menu.getItems().add(instructions);
        return new MenuBar(menu);
    }

    private Text getNumber() {
        Text number = new Text("");
        number.setFont(Font.font("Dialog", FontWeight.NORMAL, FontPosture.REGULAR, 500));
        number.setX(100);
        number.setY(430);
        return number;
    }

    private Pane getImagePane(Text number) {
        Pane pane = new Pane(number);
        pane.getStyleClass().add("canvas");
        pane.setPrefWidth(465);
        return pane;
    }

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

    private Pane addDrawCanvas(Canvas draw, GraphicsContext gc) {
        Pane pane = new Pane();
        gc.setLineWidth(25);//Brush size
        gc.setLineCap(StrokeLineCap.ROUND);
        gc.setLineJoin(StrokeLineJoin.ROUND);
        Line line = new Line();
        draw.setOnMousePressed(event -> {
            gc.beginPath();
            gc.setStroke(Color.BLACK);
            gc.setFill(Color.BLACK);
            line.setStartX(event.getX());
            line.setStartY(event.getY());
            gc.closePath();
        });

        draw.setOnMouseDragged(event -> {
            gc.lineTo(event.getX(), event.getY());
            gc.stroke();
        });
        pane.getChildren().addAll(draw);
        pane.getStyleClass().add("canvas");
        return pane;
    }

    private FlowPane addButtons(Canvas draw, GraphicsContext draw_gc, Text number, ConfidenceUI confidenceUI) {
        int width = 151, height = 50;
        ToggleButton trainButton = new ToggleButton("Train");
        trainButton.setPrefSize(width, height);

        Button clearButton = getClearButton(draw, draw_gc, number, width, height);

        Button saveButton = getSaveButton(width, height);

        Button guessButton = getGuessButton(draw, number, confidenceUI, width, height, trainButton);

        Button loadButton = getLoadButton(width, height);

        Button createButton = getCreateButton(width, height);

        return new FlowPane(trainButton, clearButton, saveButton, loadButton, createButton, guessButton);
    }

    private Button getCreateButton(int width, int height) {
        Button createButton = new Button("Create new Network");
        createButton.setPrefSize(width, height);
        createButton.setOnAction(event -> {
            //train network
            Stage settingsStage = new Stage();
            SettingsUI settings = new SettingsUI(settingsStage);
            settingsStage.showAndWait();
            try {
                if (!settingsStage.isShowing()) {
                    trainNetwork(settings.getHiddenNeurons(), settings.getBatchSize(), settings.getEpochs(),
                            settings.getLearningRate());
                    guess.setTitle("Neural Network - Hand Written Digit Recognition - " + network.getConfig());
                }
            } catch (NullPointerException ignored) {
            }

        });
        return createButton;
    }

    private Button getLoadButton(int width, int height) {
        Button loadButton = new Button("Load saved Network");
        loadButton.setPrefSize(width, height);
        loadButton.setOnAction(event -> {
            loadFile(false);
            guess.setTitle("Neural Network - Hand Written Digit Recognition - " + network.getConfig());
        });
        return loadButton;
    }

    private Button getGuessButton(Canvas draw, Text number, ConfidenceUI confidenceUI, int width, int height,
                                  ToggleButton trainButton) {
        Button guessButton = new Button("Guess");
        guessButton.setPrefSize(width, height);
        guessButton.setOnAction(event -> {
            File file = new File("Resources\\image.png");
            getDrawing(draw, file);

            ImageConverter imageConverter = new ImageConverter();
            double[] input = imageConverter.getInput();
            int guess = inputImage(number, confidenceUI, input);        /*Inputs image into neural network*/

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

    private int inputImage(Text number, ConfidenceUI confidenceUI, double[] input) {
        double[] output = network.feedForward(input);
        int guess = network.getGuess(output);
        confidenceUI.updateValues(output);
        number.setText(Integer.toString(guess));
        return guess;
    }

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

    private void retrain(boolean correct, double[] input, int guess) {
        if (correct) {
            for (int i = 0; i < 10; i++) {
                network.train(input, new Function().getTarget(guess));
            }
        } else {
            String[] targetValues = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};     /*available epoch values*/
            String target = getChoiceAlert(targetValues, "Train", "Which number did you draw");
            if (target != null) {
                for (int i = 0; i < 10; i++) {
                    network.train(input, new Function().getTarget(Integer.parseInt(target)));
                }
            }
        }
    }

    private Button getSaveButton(int width, int height) {
        Button saveButton = new Button("Save Network");
        saveButton.setPrefSize(width, height);
        saveButton.setOnAction(event -> {
            Alert alert = conformationAlert("Save File?",
                    "Do you want to save the current configuration?");
            Optional<ButtonType> response = alert.showAndWait();
            if (response.get().getText().equalsIgnoreCase("yes")) {
                double[] weights = network.getWeights();
                new SaveFile().save("Resources\\SaveFiles\\" + network.getConfig(), weights);
                Alert done = new Alert(Alert.AlertType.INFORMATION);
                done.setTitle("File saved!");
                done.setContentText("The file has been saved and named: " + network.getConfig());
                done.showAndWait();
            }
        });
        return saveButton;
    }

    private Button getClearButton(Canvas draw, GraphicsContext draw_gc, Text number, int width, int height) {
        Button clearCanvasButton = new Button("Clear Canvas");
        clearCanvasButton.setPrefSize(width, height);
        clearCanvasButton.setOnAction(event -> {
            draw_gc.clearRect(0, 0, draw.getWidth(), draw.getHeight());
            number.setText("");
        });
        return clearCanvasButton;
    }

    private void trainNetwork(int[] hiddenNeurons, int batchSize, int epochs, double learningRate) {
        Stage loadingStage = new Stage();
        network = new Network(learningRate, 784, 10, hiddenNeurons);
        Task train = train(batchSize, epochs);
        ProgressBar loading = getLoadingBar(loadingStage);
        loading.progressProperty().bind(train.progressProperty());
        new Thread(train).start();
        loadingStage.show();
        train.setOnSucceeded(closeEvent -> {
            loadingStage.close();
            train.cancel();
        });
        loadingStage.setOnCloseRequest(closeEvent -> {
            System.exit(0);
        });
    }

    private void loadFile(boolean startUp) {
        String saveFile;
        if (startUp) {
            saveFile = "(784_100H_10)[0.14].txt";
        } else {
            String[] files = getFileNames();
            saveFile = getChoiceAlert(files, "Choose config file", "Choose your preset:");
        }
        if (saveFile != null) {
            int[] hiddenNeurons = getNumOfHiddenNeurons(saveFile);
            double learningRate = getLearningRate(saveFile);
            network = new Network(learningRate, 784, 10, hiddenNeurons);
            double[] weights = new LoadFile().loadWeights("Resources\\SaveFiles\\" + saveFile);
            network.setWeights(weights);
        }
    }

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

    private String[] getFileNames() {
        File folder = new File("Resources\\SaveFiles");
        File[] files = folder.listFiles();
        String[] fileNames = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            fileNames[i] = files[i].getName();
        }
        return fileNames;
    }

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
                        updateProgress(current, max);
                    }
                    trainingData.randomiseTrainingData();
                }
                return null;
            }
        };
    }

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

    private void showInstruction() {
        Stage stage = new Stage();
        int width = 1000, height = 1000;
        TextArea textArea = new TextArea();
        textArea.setPrefSize(width, height);
        textArea.setEditable(false);
        textArea.setText(new LoadFile().loadTextFile("Resources\\Instructions.txt"));
        textArea.setStyle("-fx-font-size: 1.5em;");
        Button ok = new Button("Ok");
        ok.setTranslateX(350);
        ok.setPrefWidth(300);
        ok.setPrefHeight(100);
        ok.setOnAction(actionEvent -> stage.hide());
        VBox v = new VBox(textArea, ok);
        stage.setScene(new Scene(v, width, height));
        stage.show();
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