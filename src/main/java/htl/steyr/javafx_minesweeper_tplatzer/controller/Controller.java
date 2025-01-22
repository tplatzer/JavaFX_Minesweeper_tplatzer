package htl.steyr.javafx_minesweeper_tplatzer.controller;

import htl.steyr.javafx_minesweeper_tplatzer.service.MusicPlayer;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

/**
 * The base controller class that provides common functionality for other controllers in the Minesweeper application.
 * <p>
 * This class includes utility methods for managing scenes, stages, text elements, and audio playback.
 */
public class Controller
{
    /**
     * The background music player instance used to play looping background music.
     */
    private final MusicPlayer backgroundMusicPlayer = new MusicPlayer();

    /**
     * The sound effect player instance used to play short sound effects.
     */
    private final MusicPlayer soundEffectPlayer = new MusicPlayer();


    /**
     * Initializes the given {@link Stage} with a specific style.
     * <p>
     * This method sets the application icon for the stage based on the provided visual style.
     *
     * @param stage The {@link Stage} to be initialized.
     * @param style The visual style of the game (e.g., "retro", "modern"), used to locate the icon resource.
     * @throws NullPointerException if the resource for the icon cannot be found.
     */
    protected void initializeStage(Stage stage, String style)
    {
        // Sets the application icon for the stage using the provided style.
        stage.getIcons().add(new Image(Objects.requireNonNull(
                getClass().getResource("/" + style + "/img/icon.png")).toExternalForm()));
    }

    /**
     * Switches the scene displayed on the specified {@link Stage}.
     * <p>
     * This method updates the stage to show a new scene, sets the title, adjusts the size
     * if specified, and centers the stage on the screen.
     *
     * @param stage     The {@link Stage} to update.
     * @param newScene  The new {@link Scene} to display on the stage.
     * @param title     The title of the stage window.
     * @param newHeight The new height of the stage in pixels, or {@code -1} to keep the current height.
     * @param newWidth  The new width of the stage in pixels, or {@code -1} to keep the current width.
     */
    protected void switchScene(Stage stage, Scene newScene, String title, double newHeight, double newWidth)
    {
        stage.setScene(newScene); // Sets the new scene on the stage.
        stage.setTitle(title); // Updates the window title.
        stage.setResizable(false); // Disables resizing of the stage.

        // Updates the height if a valid value is provided.
        if (newHeight != -1)
        {
            stage.setHeight(newHeight);
        }

        // Updates the width if a valid value is provided.
        if (newWidth != -1)
        {
            stage.setWidth(newWidth);
        }

        centerStage(stage); // Centers the stage on the screen.
    }

    /**
     * Switches the scene of the given {@link Stage} to a new {@link Scene} while configuring
     * the window dimensions and title based on the specified game mode and style.
     * <p>
     * This method adjusts the window's width and height dynamically based on the selected
     * game mode (e.g., "beginner", "advanced", "pro") and applies specific offsets for the
     * given style (e.g., "retro" or modern).
     *
     * @param stage    The {@link Stage} whose scene will be switched.
     * @param newScene The {@link Scene} to be displayed in the specified stage.
     * @param gameMode The game mode that determines the dimensions of the window.
     *                 Must be one of "beginner", "advanced", or "pro".
     * @param title    The title to be set for the stage window.
     * @param style    The style applied to the UI, affecting the window's height offset.
     *                 Accepts "retro" for retro styles or other values for modern styles.
     * @throws IllegalArgumentException If the specified game mode is invalid.
     */
    protected void switchScene(Stage stage, Scene newScene, String gameMode, String title, String style)
    {
        int windowWidth;
        int windowHeight;
        int buttonSize = 45; // The size of a single button in pixels.

        // Calculate the base height, which adjusts for the style.
        final int height = 16 * buttonSize + ((style.equals("retro")) ? 210 : 230);

        // Determine the window's width and height based on the game mode.
        windowHeight = switch (gameMode) {
            case "beginner" -> {
                windowWidth = 9 * buttonSize + 5; // Width for beginner mode.
                yield 9 * buttonSize + ((style.equals("retro")) ? 160 : 190); // Height for beginner mode.
            }
            case "advanced" -> {
                windowWidth = 16 * buttonSize + 50; // Width for advanced mode.
                yield height; // Height for advanced mode.
            }
            case "pro" -> {
                windowWidth = 30 * buttonSize + 50;
                yield height; // Height for pro mode.
            }
            default -> throw new IllegalArgumentException("Invalid game mode: " + gameMode); // Handles invalid game modes.
        };

        // Call the overloaded switchScene method with the calculated dimensions.
        switchScene(stage, newScene, title, windowHeight, windowWidth);
    }

    /**
     * Creates and initializes a {@link Text} element with the specified ID and message.
     * <p>
     * This method sets the text content, assigns an ID for identification, and applies a
     * predefined style class to the text element.
     *
     * @param id  The ID to assign to the text element (used for CSS styling or identification).
     * @param msg The message to display in the text element.
     * @return A {@link Text} object initialized with the specified properties.
     */
    protected Text initializeText(String id, String msg)
    {
        Text text = new Text(); // Creates a new Text element.
        text.setText(msg); // Sets the displayed message.
        text.setId(id); // Assigns an ID to the text for identification and styling.
        text.getStyleClass().add("text"); // Adds a CSS style class for consistent appearance.

        return text; // Returns the initialized Text object.
    }

    /**
     * Plays a sound effect using the sound effect player.
     * <p>
     * This method constructs the file path for the sound effect by appending the file extension
     * {@code .wav} to the provided file name, adjusts the volume, and plays the sound effect once
     * using the specified style.
     *
     * @param fileName The name of the sound effect file (without the extension).
     * @param style    The visual style used to locate the sound effect (e.g., "retro", "modern").
     */
    protected void playSoundEffect(String fileName, String style)
    {
        getSoundEffectPlayer().playMusicOnce(fileName + ".wav", -30.0f, style); // Plays the sound effect with reduced volume.
    }

    /**
     * Plays background music using the background music player.
     * <p>
     * This method constructs the file path for the music file by appending the file extension
     * {@code .wav} to the provided file name, adjusts the volume, and plays the background music
     * in a loop using the specified style.
     *
     * @param fileName The name of the background music file (without the extension).
     * @param style    The visual style used to locate the background music (e.g., "retro", "modern").
     */
    protected void playBackgroundMusic(String fileName, String style)
    {
        getBackgroundMusicPlayer().playMusic(fileName + ".wav", -30.0f, style); // Plays the music in a loop with reduced volume.
    }

    /**
     * Stops the background music.
     * <p>
     * This method uses the background music player to stop any currently playing music.
     * It is typically called when the background music needs to be paused or replaced.
     */
    protected void stopBackgroundMusic()
    {
        getBackgroundMusicPlayer().stopMusic(); // Stops the currently playing background music.
    }

    /**
     * Centers the specified {@link Stage} on the screen.
     * <p>
     * This method calculates the center position of the screen and updates the stage's
     * X and Y coordinates to place it in the center.
     *
     * @param stage The {@link Stage} to center on the screen.
     */
    private void centerStage(Stage stage)
    {
        // Gets the screen's width and height.
        double screenWidth = javafx.stage.Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = javafx.stage.Screen.getPrimary().getVisualBounds().getHeight();

        // Gets the stage's width and height.
        double stageWidth = stage.getWidth();
        double stageHeight = stage.getHeight();

        // Calculates and sets the stage's position to center it on the screen.
        stage.setX((screenWidth - stageWidth) / 2);
        stage.setY((screenHeight - stageHeight) / 2);
    }

    /**
     * Validates that the provided {@link InputStream} is not null.
     * <p>
     * This method checks if the given {@code InputStream} is null and throws a {@link FileNotFoundException}
     * if the file cannot be found or loaded.
     *
     * @param inputStream The {@link InputStream} to validate.
     * @param fileName    The name of the file associated with the {@code InputStream} (used in the exception message).
     * @throws FileNotFoundException if the {@code InputStream} is null, indicating that the file could not be found.
     */
    public static void checkIfInputStreamIsNotNull(InputStream inputStream, String fileName) throws FileNotFoundException
    {
        if (inputStream == null) { // Checks if the InputStream is null.
            throw new FileNotFoundException("File not found: " + fileName); // Throws an exception with the file name.
        }
    }

    /**
     * Gets the sound effect player instance.
     *
     * @return The sound effect {@link MusicPlayer}.
     */
    protected MusicPlayer getSoundEffectPlayer()
    {
        return soundEffectPlayer;
    }

    /**
     * Gets the background music player instance.
     *
     * @return The background music {@link MusicPlayer}.
     */
    protected MusicPlayer getBackgroundMusicPlayer()
    {
        return backgroundMusicPlayer;
    }
}
