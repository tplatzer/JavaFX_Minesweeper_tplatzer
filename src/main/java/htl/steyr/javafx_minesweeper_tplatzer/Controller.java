package htl.steyr.javafx_minesweeper_tplatzer;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Objects;

public class Controller
{
    protected void initializeStage(Stage stage)
    {
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/img/icon.png")).toExternalForm()));
    }

    protected void switchScene(Stage stage, Scene newScene, double newHeight, String title)
    {
        stage.setScene(newScene);
        stage.setTitle(title);

        stage.setHeight(newHeight);

        centerStage(stage);
    }

    protected Text initializeText(String id, String msg)
    {
        Text text = new Text();
        text.setText(msg);
        text.setId(id);
        text.getStyleClass().add("text");

        return text;
    }

    private void centerStage(Stage stage) {
        double screenWidth = javafx.stage.Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = javafx.stage.Screen.getPrimary().getVisualBounds().getHeight();

        double stageWidth = stage.getWidth();
        double stageHeight = stage.getHeight();

        stage.setX((screenWidth - stageWidth) / 2);
        stage.setY((screenHeight - stageHeight) / 2);
    }
}
