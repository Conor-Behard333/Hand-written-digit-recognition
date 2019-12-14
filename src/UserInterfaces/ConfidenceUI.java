package UserInterfaces;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.text.DecimalFormat;

class ConfidenceUI {
    private ProgressBar[] progressBars = new ProgressBar[10];
    private Label[] labels = new Label[10];

    void updateValues(double[] output) {
        DecimalFormat df = new DecimalFormat("#.##");
        for (int i = 0; i < output.length; i++) {
            progressBars[i].setProgress(output[i]);
            labels[i].setText(i + ": " + df.format((output[i]) * 100) + "%");
            progressBars[i].setStyle(getStyle(Double.parseDouble(df.format(output[i] * 100))));
        }
    }


    private String getStyle(double output) {
        if (output >= 0 && output < 20) {
            return "-fx-accent: #FF1000";
        } else if (output >= 20 && output < 40) {
            return "-fx-accent: #FF941E";
        } else if (output >= 40 && output < 80) {
            return "-fx-accent: #FFF41F";
        } else if (output >= 80 && output <= 100) {
            return "-fx-accent: #36FF00";
        }
        return null;
    }

    Scene getScene() {
        for (int i = 0; i < progressBars.length; i++) {
            labels[i] = new Label(i + ": ");
            labels[i].setFont(new Font(30));
            progressBars[i] = new ProgressBar(0);
            progressBars[i].setPrefWidth(200);
            progressBars[i].setPrefHeight(20);
        }
        VBox barsBox = new VBox(progressBars);
        VBox labelBox = new VBox(labels);
        barsBox.setTranslateY(13);
        barsBox.setSpacing(42);
        labelBox.setSpacing(17);
        labelBox.setTranslateX(15);

        HBox panel = new HBox(labelBox, barsBox);
        panel.setSpacing(50);
        return new Scene(panel, 400, 600);
    }
}