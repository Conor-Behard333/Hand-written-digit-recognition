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
        primaryStage.setTitle("Instructions");
//        display info
        loadFile(true);
        new GuessUI(network);

        primaryStage.setOnHidden(onHiddenEvent -> {
            //load or train option
            Alert alert = new Function().conformationAlert("Load File?", "Do you want to load a preset configuration?");
            Optional<ButtonType> response = alert.showAndWait();
            if (response.get().getText().equalsIgnoreCase("Yes")) {
                loadFile(false);
                new GuessUI(network);
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
        textArea.setStyle("-fx-font-size: 1.5em;");
        Button ok = new Button("Ok");
        ok.setTranslateX(350);
        ok.setPrefWidth(300);
        ok.setPrefHeight(100);
        ok.setOnAction(actionEvent -> primaryStage.hide());
        VBox v = new VBox(textArea, ok);
        return new Scene(v, width, height);
    }

    private void loadFile(boolean startUp) {
        if (startUp) {
            File file = new File("Resources\\SaveFiles\\(784_100_10)[0.14].txt");
            int[] hiddenNeurons = getNumOfHiddenNeurons(file.getName());
            double learningRate = getLearningRate(file.getName());
            network = new Network(learningRate, 784, 10, hiddenNeurons);
            double[] weights = new LoadFile().loadWeights(file.getAbsolutePath());
            network.setWeights(weights);
        } else {
            String[] files = getFileNames();
            String saveFile = new Function().getChoiceAlert(files, "Choose config file", "Choose your preset:");
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