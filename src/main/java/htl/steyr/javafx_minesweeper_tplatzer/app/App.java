package htl.steyr.javafx_minesweeper_tplatzer.app;

import htl.steyr.javafx_minesweeper_tplatzer.controller.MenuController;
import htl.steyr.javafx_minesweeper_tplatzer.model.UserDataManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The main entry point for the JavaFX Minesweeper application.
 * This class initializes the application and starts the user interface.
 */
public class App extends Application
{
    /**
     * The start method initializes and displays the main application window.
     *
     * @param stage The primary stage for this application, provided by the JavaFX runtime.
     */
    @Override
    public void start(Stage stage)
    {
        // Load user data and start the MenuController with the loaded username and default settings.
        new MenuController(UserDataManager.loadUserData().getUsername(), "retro", false).start(stage);
    }

    /**
     * The main method launches the application.
     *
     * @param args Command-line arguments passed to the application (not used in this implementation).
     */
    public static void main(String[] args)
    {
        launch(); // Launches the JavaFX application.
    }
}
