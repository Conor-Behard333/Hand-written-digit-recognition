package UserInterfaces;

import NeuralNetwork.Function;
import NeuralNetwork.Network;
import ProcessingData.ImageConverter;
import ProcessingData.SaveFile;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
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
import java.util.Optional;

public class GuessUI {
    GuessUI(Network network) {
        ConfidenceUI confidenceUI = new ConfidenceUI();
        Stage prediction = new Stage();
        prediction.setResizable(false);
        prediction.setScene(confidenceUI.getScene());
        prediction.setX(1415);
        prediction.setY(135);
        prediction.show();

        Stage guess = new Stage();

        guess.setTitle("Neural Network - Hand Written Digit Recognition");
        guess.setResizable(false);

        Canvas draw = new Canvas(405, 520);
        GraphicsContext draw_gc = draw.getGraphicsContext2D();
        draw.setTranslateX(20);
        draw.setTranslateY(20);

        Text number = new Text("");
        number.setFont(Font.font("Dialog", FontWeight.NORMAL, FontPosture.REGULAR, 500));
        number.setX(100);
        number.setY(450);

        BorderPane borderPane = new BorderPane();
        FlowPane flowPane = addButtons(draw, draw_gc, number, network, confidenceUI);
        Pane drawPane = addDrawCanvas(draw, draw_gc);
        Pane imagePane = new Pane(number);

        borderPane.setLeft(drawPane);
        borderPane.setBottom(flowPane);
        borderPane.setRight(imagePane);
        imagePane.setPrefSize(465, 520);

        Scene scene = new Scene(borderPane, 900, 600);
        drawPane.getStyleClass().add("canvas");
        imagePane.getStyleClass().add("canvas");
        scene.getStylesheets().add("Styles.css");
        guess.setScene(scene);
        guess.show();
    }

    private Pane addDrawCanvas(Canvas draw, GraphicsContext gc) {
        Pane pane = new Pane();
        gc.setLineWidth(30);
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
        return pane;
    }

    private FlowPane addButtons(Canvas draw, GraphicsContext draw_gc, Text number, Network network, ConfidenceUI confidenceUI) {
        int width = 227, height = 50;

        ToggleButton train = new ToggleButton("Train");
        train.setPrefSize(width, height);

        Button clearCanvas = new Button("Clear");
        clearCanvas.setPrefSize(width, height);
        clearCanvas.setOnAction(event -> {
            draw_gc.clearRect(0, 0, draw.getWidth(), draw.getHeight());
            number.setText("");
        });

        Button save = new Button("Save");
        save.setPrefSize(width, height);
        save.setOnAction(event -> {
            Alert alert = new Function().conformationAlert("Save File?", "Do you want to save the current configuration?");
            Optional<ButtonType> response = alert.showAndWait();
            if (response.get().getText().equalsIgnoreCase("yes")) {
                File folder = new File("Resources\\SaveFiles");
                double[] weights = network.getWeights();
                new SaveFile().save("Resources\\SaveFiles\\" + "(" + (folder.listFiles().length + 1) + ")" + network.getConfig(), weights);
                Alert done = new Alert(Alert.AlertType.INFORMATION);
                done.setTitle("File saved!");
                done.setContentText("The file has been saved and named: " + network.getConfig());
                done.showAndWait();
            }
        });

        Button guessButton = new Button("Guess");
        guessButton.setPrefSize(width, height);
        guessButton.setOnAction(event -> {
            File file = new File("Resources\\image.png");
            try {
                WritableImage writableImage = new WritableImage((int) Math.round(draw.getWidth()), (int) Math.round(draw.getHeight()));
                draw.snapshot(null, writableImage);
                invertPixelValues(writableImage, draw);
                BufferedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            ImageConverter imageConverter = new ImageConverter();
            double[] input = imageConverter.getInput();
            double[] output = network.feedForward(input);
            int guess = network.getGuess(output);
            confidenceUI.updateValues(output);
            number.setText(Integer.toString(guess));

            if (train.isSelected()) {
                Alert alert = new Function().conformationAlert(null, "Did it guess right?");
                Optional<ButtonType> response = alert.showAndWait();
                if (response.get().getText().equalsIgnoreCase("Yes")){
                    for (int i = 0; i < 10; i++) {
                        network.train(input, new Function().getTarget(guess));
                    }
                }else if (response.get().getText().equalsIgnoreCase("no")){
                    String[] targetValues = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};//available epoch values
                    String target = new Function().getChoiceAlert(targetValues, "Train", "Which number did you draw");
                    for (int i = 0; i < 10; i++) {
                        network.train(input, new Function().getTarget(Integer.parseInt(target)));
                    }
                }else {
                    System.exit(0);
                }
            }
        });

        FlowPane flowPane = new FlowPane();
        flowPane.getChildren().addAll(train, clearCanvas, save, guessButton);
        return flowPane;
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
}
