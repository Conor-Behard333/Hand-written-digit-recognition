package UserInterfaces;

import NeuralNetwork.Function;
import NeuralNetwork.Network;
import ProcessingData.LoadDataSet;
import ProcessingData.LoadFile;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Run extends Application {
    private Network network;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = getInstructionScene(primaryStage);
        //display info
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.seOnCloseRequest(close -> System.exit(0));
        primaryStage.setOnHidden(onHiddenEvent -> {
            //load or train option
            Alert alert = new Function().conformationAlert("Load File?", "Do you want to load a preset configuration?");
            Optional<ButtonType> response = alert.showAndWait();
            if (response.get().getText().equalsIgnoreCase("Yes")) {
                LoadFile();
                new GuessUI(network);
            } else if (response.get().getText().equalsIgnoreCase("No")) {
                //train network
                SettingsUI settings = new SettingsUI();
                int[] hiddenNeurons = settings.getHiddenNeurons();
                int batchSize = settings.getBatchSize();
                int epochs = settings.getEpochs();
                double learningRate = settings.getLearningRate();

                Stage loadingStage = new Stage();
                Task train = train(batchSize, epochs);
                ProgressBar loading = getLoadingBar(loadingStage);

                network = new Network(learningRate, 784, 10, hiddenNeurons);
                loading.progressProperty().bind(train.progressProperty());
                new Thread(train).start();
                loadingStage.show();
                train.setOnSucceeded(closeEvent -> {
                    loadingStage.close();
                    train.cancel();
                    new GuessUI(network);
                });
                loadingStage.setOnCloseRequest(closeEvent -> {
                    System.exit(0);
                });
            } else {
                System.exit(0);
            }
        });
    }

    private Scene getInstructionScene(Stage primaryStage) {
        int width = 1000, height = 1000;
        TextArea textArea = new TextArea();
        textArea.setPrefSize(width, height);
        textArea.setEditable(false);
        textArea.setText(new LoadFile().loadTextFile("Resources\\Instructions.txt"));
        textArea.setStyle("-fx-font-size: 1.5em;");//4em
        Button ok = new Button("Ok");
        ok.setTranslateX(350);
        ok.setPrefWidth(300);
        ok.setPrefHeight(100);
        ok.setOnAction(actionEvent -> primaryStage.hide());
        VBox v = new VBox(textArea, ok);
        return new Scene(v, width, height);
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

    private void LoadFile() {
        String[] files = getFileNames();
        String saveFile = new Function().getChoiceAlert(files, "Choose config file", "Choose your preset:");
        int[] hiddenNeurons = getNumOfHiddenNeurons(saveFile);
        double learningRate = getLearningRate(saveFile);
        network = new Network(learningRate, 784, 10, hiddenNeurons);
        double[] weights = new LoadFile().loadWeights("Resources\\SaveFiles\\" + saveFile);
        network.setWeights(weights);
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
        Pattern pattern = Pattern.compile("_(\\d+)_");
        Matcher matcher = pattern.matcher(saveFile);
        while (matcher.find()) {
            String num = saveFile.substring(matcher.start() + 1, matcher.end() - 1);
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
}
