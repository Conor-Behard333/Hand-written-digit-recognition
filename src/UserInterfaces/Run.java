package UserInterfaces;

import NeuralNetwork.Function;
import NeuralNetwork.Network;
import ProcessingData.LoadDataSet;
import ProcessingData.LoadFile;
import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

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

                Stage loadingStage = new Stage();
                Task train = train(batchSize, epochs, loadingStage);
                ProgressBar loading = getLoadingBar(loadingStage);

                network = new Network(784, 10, hiddenNeurons);
                loading.progressProperty().bind(train.progressProperty());
                new Thread(train).start();
                loadingStage.show();
                loadingStage.setOnCloseRequest(closeEvent -> {
                    System.exit(0);
                });
            } else {
                System.exit(0);
            }
        });
    }

    private ProgressBar getLoadingBar(Stage loadingStage) {
        loadingStage.setTitle("Loading Please Wait...");
        ProgressBar loading = new ProgressBar(0);
        loading.setPrefSize(400, 60);
        Label label = new Label("Progress: ");
        label.setTranslateX(10);
        label.setTranslateY(10);
        label.setStyle("-fx-font-size: 25");
        HBox hBox = new HBox(label, loading);
        hBox.setSpacing(20);
        Scene loadingScene = new Scene(hBox, 530, 60);
        loadingStage.setScene(loadingScene);
        return loading;
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

    private Task train(int batchSize, int epochs, Stage loadingStage) {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                this.setOnSucceeded(closeEvent -> {
                    loadingStage.close();
                    this.cancel();
                    new GuessUI(network);
                });
                int max = batchSize * epochs;
                int current = 0;
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
        network = new Network(784, 10, hiddenNeurons);
        double[] weights = new LoadFile().loadWeights("Resources\\SaveFiles\\" + saveFile);
        network.setWeights(weights);
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
        int count = 0;
        for (int i = 0; i < saveFile.length(); i++) {
            if (saveFile.charAt(i) == '-') {
                count++;
            }
        }
        int numOfHiddenLayers = count - 1;
        int[] numOfHiddenNeurons = new int[numOfHiddenLayers];
        int start = 4;
        int end = 0;
        for (int i = 0; i < numOfHiddenLayers; i++) {
            for (int j = start; j < saveFile.length(); j++) {
                if (saveFile.charAt(j) == '-') {
                    end = j;
                    break;
                }
            }
            numOfHiddenNeurons[i] = Integer.parseInt(saveFile.substring(start, end));
            start = end + 1;
        }
        return numOfHiddenNeurons;
    }
}


