package htl.steyr.javafx_minesweeper_tplatzer;

import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class App extends Application
{
    @Override
    public void start(Stage stage)
    {
        new MenuController(UserDataManager.loadUserData().getUsername(), "retro", false).start(stage);
    }

    public static void main(String[] args)
    {
        launch();
    }
}
