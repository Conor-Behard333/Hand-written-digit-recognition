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

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        new GuessUI();
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
}