package htl.steyr.javafx_minesweeper_tplatzer.app;

/**
 * A simple launcher class to start the Minesweeper application.
 * <p>
 * This class exists to avoid potential JavaFX runtime issues that can occur
 * when directly launching the application from the `App` class.
 * It delegates the launch process to the `App` class.
 */
public class Launcher
{
    /**
     * The entry point of the application.
     * <p>
     * This method delegates execution to the {@link App#main(String[])} method,
     * serving as the launcher for the application.
     *
     * @param args The command-line arguments passed to the program.
     */
    public static void main(String[] args)
    {
        App.main(args); // Delegates to the main method of the App class.
    }
}
