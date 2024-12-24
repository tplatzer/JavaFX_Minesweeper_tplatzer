package htl.steyr.javafx_minesweeper_tplatzer;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Objects;

public class Controller
{
    protected MusicPlayer backgroundMusicPlayer = new MusicPlayer();
    protected MusicPlayer soundEffectPlayer = new MusicPlayer();

    protected void initializeStage(Stage stage)
    {
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/img/icon.png")).toExternalForm()));
    }

    protected void switchScene(Stage stage, Scene newScene, double newHeight, double newWidth, String title)
    {
        stage.setScene(newScene);
        stage.setTitle(title);

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
        getSoundEffectPlayer().playMusicShort(fileName + ".wav", -30.0f);
    }

    protected void playBackgroundMusic(String fileName)
    {
        getBackgroundMusicPlayer().playMusic(fileName + ".wav", -30.0f);
    }

    protected void stopBackgroundMusic()
    {
        getBackgroundMusicPlayer().stopMusic();
    }

    private void centerStage(Stage stage) {
        double screenWidth = javafx.stage.Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = javafx.stage.Screen.getPrimary().getVisualBounds().getHeight();

        double stageWidth = stage.getWidth();
        double stageHeight = stage.getHeight();

        stage.setX((screenWidth - stageWidth) / 2);
        stage.setY((screenHeight - stageHeight) / 2);
    }

    protected MusicPlayer getSoundEffectPlayer()
    {
        return soundEffectPlayer;
    }

    protected void setSoundEffectPlayer(MusicPlayer soundEffectPlayer)
    {
        this.soundEffectPlayer = soundEffectPlayer;
    }

    protected MusicPlayer getBackgroundMusicPlayer()
    {
        return backgroundMusicPlayer;
    }

    protected void setBackgroundMusicPlayer(MusicPlayer backgroundMusicPlayer)
    {
        this.backgroundMusicPlayer = backgroundMusicPlayer;
    }
}
