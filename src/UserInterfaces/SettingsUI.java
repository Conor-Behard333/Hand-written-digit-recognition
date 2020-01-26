package UserInterfaces;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class SettingsUI {
    private ArrayList<TextArea> textAreas = new ArrayList<>();
    
    private int batchSize = -1;
    private int epochs = -1;
    private int[] hiddenNeurons = null;
    private double learningRate = -1;
    
    /*
     *
     */
    SettingsUI(Stage stage) {
        Pane pane = new Pane(getVBox(stage));
        Scene scene = new Scene(pane, 400, 370);
        stage.setTitle("Neural Network - Settings");
        stage.setResizable(false);
        stage.setScene(scene);
    }
    
    /*
     * Creates the labels and input boxes for the interface and places them in a H box
     */
    private HBox getHBox(String label, String tooltip, int x) {
        Label tempLabel = new Label(label);
        tempLabel.setTranslateX(5);
        tempLabel.setTranslateY(20);
        
        TextArea tempText = new TextArea();
        tempText.setPrefSize(200, 25);
        tempText.setTranslateX(x);
        
        Font font = new Font(22);
        tempText.setFont(font);
        tempLabel.setFont(font);
        
        tempText.setTooltip(new Tooltip(tooltip));
        HBox hBox = new HBox(tempLabel, tempText);
        hBox.setTranslateY(5);
        textAreas.add(tempText);
        return hBox;
    }
    
    /*
     * Creates each row of the inputs for the interface
     */
    private VBox getVBox(Stage stage) {
        HBox hiddenNeuronsHBox = getHBox("Hidden Neurons: ", "Integer between 1 and 150\nSeparate values with a comma", 25);
        HBox batchSizeHBox = getHBox("Batch Size: ", "Integer between 1 and 60,000", 86);
        HBox epochsHBox = getHBox("Epochs: ", "Integer between 1 and 10", 116);
        HBox lrHBox = getHBox("Learning rate: ", "Decimal between 0.0 and 1.0", 57);
        
        Button confirm = getConfirmButton(stage);
        VBox vBox = new VBox(hiddenNeuronsHBox, batchSizeHBox, epochsHBox, lrHBox, confirm);
        vBox.setSpacing(10);
        return vBox;
    }
    
    /*
     * Returns a confirm button that when clicked checks if the user has entered valid inputs and then closes the window
     */
    private Button getConfirmButton(Stage stage) {
        Button confirm = new Button("Confirm");
        confirm.setFont(new Font(22));
        confirm.setPrefSize(150, 20);
        confirm.setTranslateX(125);
        confirm.setTranslateY(8);
        confirm.setOnAction(event -> {
            /*
             * Checks each input box for valid inputs and throws exceptions if the user has not
             * entered the appropriate data type with an error explaining why
             */
            
            try {
                getNeurons(textAreas.get(0).getText());
                if (checkValid(Integer.parseInt(textAreas.get(1).getText()), 60000)) {
                    batchSize = Integer.parseInt(textAreas.get(1).getText());
                } else {
                    throw new Throwable("Invalid input for batch size - Integer value must be between 1 and 60000");
                }
                if (checkValid(Integer.parseInt(textAreas.get(2).getText()), 10)) {
                    epochs = Integer.parseInt(textAreas.get(2).getText());
                } else {
                    throw new Throwable("Invalid input for epochs - Integer value must be between 1 and 10");
                }
                if (checkValid(Double.parseDouble(textAreas.get(3).getText()))) {
                    learningRate = Double.parseDouble(textAreas.get(3).getText());
                } else {
                    throw new Throwable("Invalid input for learning rate - Decimal value must be between 0.0 and 1.0");
                }
                stage.close();
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid input - Must be a number").showAndWait();
            } catch (Throwable throwable) {
                new Alert(Alert.AlertType.ERROR, throwable.getMessage()).showAndWait();
            }
        });
        return confirm;
    }
    
    /*
     * Checks if the integer values are valid (in range)
     */
    private boolean checkValid(int input, int upperBound) {
        return input >= 1 && input <= upperBound;
    }
    
    /*
     * Checks if the double values are valid (in range)
     */
    private boolean checkValid(double input) {
        return input > 0 && input <= 1;
    }
    
    /*
     * Converts the user's input from a string into an int and adds each one into an int array
     */
    private void getNeurons(String input) throws Throwable {
        ArrayList<Integer> temp = new ArrayList<>();
        input = removeWhitespace(input) + ",";
        Pattern pattern = Pattern.compile("-?\\d+,");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            String num = input.substring(matcher.start(), matcher.end() - 1);
            temp.add(Integer.parseInt(num));
        }
        hiddenNeurons = temp.stream().mapToInt(i -> i).toArray();
        for (int hiddenNeuron : hiddenNeurons) {
            if (hiddenNeuron < 1 || hiddenNeuron > 150) {
                throw new Throwable("Hidden neurons must be between 1 and 150");
            }
        }
    }
    
    private String removeWhitespace(String input) {
        StringBuilder newInput = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) != ' ') {
                newInput.append(input.charAt(i));
            }
        }
        return newInput.toString();
    }
    
    public int getBatchSize() {
        return batchSize;
    }
    
    public int getEpochs() {
        return epochs;
    }
    
    public int[] getHiddenNeurons() {
        return hiddenNeurons;
    }
    
    public double getLearningRate() {
        return learningRate;
    }
}