import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    private static final int DEFAULT_PERCENT = 33;
    private static final int DEFAULT_HEIGHT = 400;
    private static final int DEFAULT_WIDTH = 800;
    private static final double DEFAULT_SPEED = 0.05;
    private static final Color DEFAULT_COLOR = Color.BLACK;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Conway's Game Of Life");

        Label percentBox = new Label("Select starting percent of Life:");
        Label percentText = new Label(DEFAULT_PERCENT + "%");
        percentText.setTextFill(Color.BLACK);

        Slider percentCheck = new Slider();
        percentCheck.setMin(0);
        percentCheck.setMax(100);
        percentCheck.setValue(DEFAULT_PERCENT);
        percentCheck.setShowTickLabels(true);
        percentCheck.setBlockIncrement(1);
        percentCheck.valueProperty().addListener((observable, oldValue, newValue) -> percentText.setText((int)newValue.doubleValue() + "%"));


        Label widthBox = new Label("Select width:");
        Label widthText = new Label(DEFAULT_WIDTH + " pixels");
        widthText.setTextFill(Color.BLACK);

        Slider widthCheck = new Slider();
        widthCheck.setMin(100);
        widthCheck.setMax(800);
        widthCheck.setValue(DEFAULT_WIDTH);
        widthCheck.setShowTickLabels(true);
        widthCheck.setBlockIncrement(50);
        widthCheck.valueProperty().addListener(((observable, oldValue, newValue) -> widthText.setText((int)newValue.doubleValue() + " pixels")));


        Label heightBox = new Label("Select height:");
        Label heightText = new Label(DEFAULT_HEIGHT + " pixels");
        heightText.setTextFill(Color.BLACK);

        Slider heightCheck = new Slider();
        heightCheck.setMin(100);
        heightCheck.setMax(400);
        heightCheck.setValue(DEFAULT_HEIGHT);
        heightCheck.setShowTickLabels(true);
        heightCheck.setBlockIncrement(50);
        heightCheck.valueProperty().addListener((((observable, oldValue, newValue) -> heightText.setText((int)newValue.doubleValue() + " pixels"))));


        Label speedBox = new Label("Select speed:");
        Label speedText = new Label(DEFAULT_SPEED + " seconds");
        speedText.setTextFill(Color.BLACK);

        Slider speedCheck = new Slider();
        speedCheck.setMin(0);
        speedCheck.setMax(2);
        speedCheck.setValue(DEFAULT_SPEED);
        speedCheck.setShowTickLabels(true);
        speedCheck.setBlockIncrement(0.01);
        speedCheck.valueProperty().addListener((((observable, oldValue, newValue) -> speedText.setText((int)newValue.doubleValue() + " seconds"))));


        CheckBox trippyBox = new CheckBox("Activate Trippy Mode");
        trippyBox.setSelected(false);
        trippyBox.setAllowIndeterminate(false);


        Label colorBox = new Label("Life color:");
        ColorPicker colorPicker = new ColorPicker(DEFAULT_COLOR);


        CheckBox loadGameBox = new CheckBox("Load Life from file?");
        loadGameBox.setSelected(false);
        loadGameBox.setAllowIndeterminate(false);

        TextField fileField = new TextField();


        Button button = new Button("Launch");
        button.setOnAction(event -> {
           primaryStage.close();

           Color color;
           if (trippyBox.isSelected()) {
               color = null;
           } else {
               color = colorPicker.getValue();
           }
            Board.init(fileField.getCharacters().toString(), !loadGameBox.isSelected(), (int)percentCheck.getValue(), (int)widthCheck.getValue(), (int)heightCheck.getValue(), speedCheck.getValue(), color);
        });


        VBox root = new VBox();
        root.setPadding(new Insets(20));
        root.setSpacing(10);
        root.getChildren().addAll(
                percentBox, percentCheck, percentText,
                widthBox, widthCheck, widthText,
                heightBox, heightCheck, heightText,
                speedBox, speedCheck, speedText,
                colorBox, colorPicker,
                trippyBox,
                loadGameBox, fileField,
                button);

        Scene scene = new Scene(root, 400, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
