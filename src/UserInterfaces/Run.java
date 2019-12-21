package UserInterfaces;

import javafx.application.Application;
import javafx.stage.Stage;

public class Run extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        new GuessUI();
    }
}