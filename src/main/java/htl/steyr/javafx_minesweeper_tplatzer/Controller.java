package htl.steyr.javafx_minesweeper_tplatzer;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class Controller
{
    protected MusicPlayer backgroundMusicPlayer = new MusicPlayer();
    protected MusicPlayer soundEffectPlayer = new MusicPlayer();

    protected void initializeStage(Stage stage)
    {
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/img/icon.png")).toExternalForm()));
    }

    protected void switchScene(Stage stage, Scene newScene, String title, double newHeight, double newWidth)
    {
        stage.setScene(newScene);
        stage.setTitle(title);
        stage.setResizable(false);

        if (newHeight != -1)
        {
            stage.setHeight(newHeight);
        }
        if (newWidth != -1)
        {
            stage.setWidth(newWidth);
        }
        centerStage(stage);
    }

    protected void switchScene(Stage stage, Scene newScene, String gameMode, String title)
    {
        int windowWidth;
        int windowHeight;
        int buttonSize = 45;

        windowHeight = switch (gameMode)
        {
            case "beginner" ->
            {
                windowWidth = 9 * buttonSize + 5;
                yield 9 * buttonSize + 100;
            }
            case "advanced" ->
            {
                windowWidth = 16 * buttonSize + 50;
                yield 16 * buttonSize + 150;
            }
            case "pro" ->
            {
                windowWidth = 30 * buttonSize + 50;
                yield 16 * buttonSize + 150;
            }
            default -> throw new IllegalArgumentException("Invalid game mode: " + gameMode);
        };

        switchScene(stage, newScene, title, windowHeight, windowWidth);
    }

    protected Text initializeText(String id, String msg)
    {
        Text text = new Text();
        text.setText(msg);
        text.setId(id);
        text.getStyleClass().add("text");

        return text;
    }

    protected void playSoundEffect(String fileName)
    {
        getSoundEffectPlayer().playMusicOnce(fileName + ".wav", -30.0f);
    }

    protected void playBackgroundMusic(String fileName)
    {
        getBackgroundMusicPlayer().playMusic(fileName + ".wav", -30.0f);
    }

    protected void stopBackgroundMusic()
    {
        getBackgroundMusicPlayer().stopMusic();
    }

    private void centerStage(Stage stage)
    {
        double screenWidth = javafx.stage.Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = javafx.stage.Screen.getPrimary().getVisualBounds().getHeight();

        double stageWidth = stage.getWidth();
        double stageHeight = stage.getHeight();

        stage.setX((screenWidth - stageWidth) / 2);
        stage.setY((screenHeight - stageHeight) / 2);
    }

    protected static void checkIfInputStreamIsNotNull(InputStream inputStream, String fileName) throws FileNotFoundException
    {
        if (inputStream == null) throw new FileNotFoundException("File not found: " + fileName);
    }

    protected MusicPlayer getSoundEffectPlayer()
    {
        return soundEffectPlayer;
    }

    protected MusicPlayer getBackgroundMusicPlayer()
    {
        return backgroundMusicPlayer;
    }
}
