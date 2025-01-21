package htl.steyr.javafx_minesweeper_tplatzer.controller;

import htl.steyr.javafx_minesweeper_tplatzer.model.Cell;
import htl.steyr.javafx_minesweeper_tplatzer.model.UserData;
import htl.steyr.javafx_minesweeper_tplatzer.model.UserDataManager;
import htl.steyr.javafx_minesweeper_tplatzer.service.LeaderboardClient;
import htl.steyr.javafx_minesweeper_tplatzer.service.MusicPlayer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

/**
 * The {@code GameController} class manages the Minesweeper gameplay, including UI, game logic,
 * sound effects, and interactions with other components like leaderboard management and user data.
 * <p>
 * This class is responsible for handling the game loop, initializing the game board,
 * managing player interactions, and determining win or loss conditions.
 */
public class GameController extends Controller
{
    /**
     * Maximum width for the HBox layout in the game window.
     */
    private static final int maxHBoxWidth = 1000;

    /**
     * Maximum height for the HBox layout in the game window.
     */
    private static final int maxHBoxHeight = 700;

    /**
     * The {@link Scene} object representing the current game scene.
     */
    private Scene gameScene;

    /**
     * The root {@link VBox} container for the game's UI elements.
     */
    private final VBox root = new VBox();

    /**
     * Container for displaying game information such as the timer and remaining flags.
     */
    private HBox gameInfoBox;

    /**
     * Container for the remaining flag counter.
     */
    private HBox remainingFlagCounterBox;

    /**
     * Label displaying the number of remaining flags.
     */
    private Label remainingFlagCounterLabel;

    /**
     * Button to restart the game.
     */
    private Button restartGameButton;

    /**
     * Container for the game's timer.
     */
    private HBox timerBox;

    /**
     * Label displaying the elapsed game time.
     */
    private Label timerLabel;

    /**
     * Tracks the elapsed game time in seconds.
     */
    private int elapsedTime;

    /**
     * {@link Timeline} object for updating the timer at one-second intervals.
     */
    private Timeline timer;

    /**
     * The {@link GridPane} that represents the game field.
     */
    private GridPane gameField;

    /**
     * The primary {@link Stage} of the application.
     */
    private Stage stage;

    /**
     * The current difficulty level of the game (e.g., "beginner", "advanced", "pro").
     */
    private String difficulty;

    /**
     * The style / theme of the game (e.g., "retro", "modern").
     */
    private String style;

    /**
     * The total number of mines on the game board.
     */
    private int totalMines;

    /**
     * The number of rows on the game board.
     */
    private int rows;

    /**
     * The number of columns on the game board.
     */
    private int columns;

    /**
     * List of {@link Cell} objects representing all cells on the game board.
     */
    private List<Cell> cells;

    /**
     * Flag indicating whether the first click has been made.
     */
    private boolean firstClick;

    /**
     * Flag indicating whether the game is muted.
     */
    private boolean muted;

    /**
     * The username of the player.
     */
    private String username;


    /**
     * Constructs a new {@code GameController} with the specified parameters.
     * <p>
     * This constructor initializes the game controller with the given player username,
     * game difficulty, style, and sound settings.
     *
     * @param username   The username of the player.
     * @param difficulty The difficulty level of the game (e.g., "beginner", "advanced", "pro").
     * @param style      The visual style/theme of the game (e.g., "retro", "modern").
     * @param muted      {@code true} if the game should be muted, {@code false} otherwise.
     */
    public GameController(String username, String difficulty, String style, boolean muted)
    {
        setUsername(username); // Sets the player's username.
        setDifficulty(difficulty); // Sets the game's difficulty level.
        setStyle(style); // Sets the visual style of the game.
        setMuted(muted); // Sets whether the game is muted.
    }

    /**
     * Starts the game by setting up the stage, initializing game elements, and configuring the game window.
     * <p>
     * This method is the entry point for initializing a new game session. It sets default values,
     * initializes user interface elements, starts background music (if not muted),
     * and displays the main game window.
     *
     * @param stage The primary {@link Stage} on which the game is displayed.
     */
    public void start(Stage stage)
    {
        setStage(stage); // Sets the primary stage for the game.
        setDefaultValues(); // Resets game variables to their default values.
        initializeUserElements(); // Sets up game-specific UI elements like the game field and info box.
        if (!isMuted())
            playBackgroundMusic("background-music", getStyle()); // Plays background music if sound is enabled.

        initializeWindow(); // Configures and displays the main game window.
    }

    /**
     * Resets the game state to its default values.
     * <p>
     * This method initializes important game variables to ensure a clean slate
     * when starting or restarting the game.
     */
    private void setDefaultValues()
    {
        setElapsedTime(0); // Resets the timer to 0.
        setTotalMines(0);  // Resets the total mine count.
        setRows(0);        // Resets the number of rows in the game field.
        setColumns(0);     // Resets the number of columns in the game field.
        setFirstClick(true); // Ensures the first click flag is reset for a new game.
    }

    /**
     * Initializes and configures the main game window.
     * <p>
     * This method sets up the root container, styles, and layout of the game window.
     * It ensures that the window adjusts dynamically to the stage size and switches to the game scene.
     */
    private void initializeWindow()
    {
        getRoot().setSpacing(20); // Sets spacing between elements in the root container.
        getRoot().setAlignment(Pos.CENTER); // Centers all child elements in the root container.
        getRoot().setMinSize(GameController.getMaxHBoxWidth(), GameController.getMaxHBoxHeight()); // Sets the minimum size of the window.
        getRoot().setMaxSize(GameController.getMaxHBoxWidth(), GameController.getMaxHBoxHeight()); // Sets the maximum size of the window.
        getRoot().prefWidthProperty().bind(getStage().widthProperty()); // Binds the root's preferred width to the stage width.
        getRoot().prefHeightProperty().bind(getStage().heightProperty()); // Binds the root's preferred height to the stage height.
        getRoot().getChildren().addAll(getGameInfoBox(), getGameField()); // Adds the game info box and game field to the root container.
        getRoot().getStyleClass().add("root-container"); // Applies a CSS class to the root container.
        getRoot().getStylesheets().addAll(
                Objects.requireNonNull(getClass().getResource("/" + getStyle() + "/style/style.css")).toExternalForm(),
                Objects.requireNonNull(getClass().getResource("/" + getStyle() + "/style/gameStyle.css")).toExternalForm()); // Adds external stylesheets for the game.

        setGameScene(new Scene(getRoot())); // Creates a new scene with the configured root container.
        switchScene(getStage(), getGameScene(), getDifficulty(), "Bomb-Disposal-Simulator"); // Switches to the game scene with the specified title.
    }

    /**
     * Restarts the game by creating a new {@code GameController} instance and starting a new game session.
     * <p>
     * This method stops the background music and initializes a fresh game using the current
     * player's username, selected difficulty, style, and mute settings.
     */
    private void restartGame()
    {
        stopBackgroundMusic(); // Stops the currently playing background music.
        new GameController(getUsername(), getDifficulty(), getStyle(), isMuted()).start(getStage()); // Starts a new game instance with the same settings.
    }

    /**
     * Ends the current game and handles the endgame state based on whether the player won or lost.
     * <p>
     * This method stops the game timer, background music, disables all game interactions,
     * highlights incorrect flags, and triggers win or loss logic.
     *
     * @param won {@code true} if the player won the game, {@code false} if the player lost.
     */
    public void endGame(boolean won)
    {
        stopTimer(); // Stops the game timer.
        stopBackgroundMusic(); // Stops the background music.

        getRestartGameButton().setDisable(true); // Disables the restart button to prevent further interactions.
        for (Cell cell : getCells())
        {
            cell.getButton().setDisable(true); // Disables all cells on the game board.
        }

        showIncorrectFlags(); // Highlights any incorrectly flagged cells.

        if (won)
        {
            wonGame(); // Triggers the logic for winning the game.
        } else
        {
            lossGame(); // Triggers the logic for losing the game.
        }
    }

    /**
     * Handles the logic for when the player wins the game.
     * <p>
     * This method updates the player's best time, submits the best time to the leaderboard,
     * updates the restart button to reflect the win, and transitions back to the menu after
     * playing the victory sound (if not muted).
     */
    private void wonGame()
    {
        updateBestTime(); // Updates the player's best time for the current difficulty level.
        updateRestartGameButton("win"); // Updates the restart button to display a winning emoji.

        LeaderboardClient client = new LeaderboardClient(); // Creates a leaderboard client.
        try
        {
            client.submitBestTime(getUsername(), getElapsedTime(), getDifficulty()); // Submits the player's best time to the leaderboard.
        } catch (Exception e)
        {
            System.err.println(e.getMessage()); // Logs any errors that occur during submission.
        }

        if (isMuted())
        {
            // If the game is muted, transition back to the menu after a short delay.
            new Timeline(new KeyFrame(Duration.seconds(3), event -> switchToMenu())).play();
        } else
        {
            // Play a random win jingle and transition back to the menu after the sound finishes.
            String winJingle = getRandomWinJingle();
            playSoundEffect(winJingle, getStyle());

            Timeline delay = new Timeline(new KeyFrame(
                    Duration.seconds(MusicPlayer.getSoundEffectDuration(winJingle, getStyle()) - 0.5),
                    event -> switchToMenu()
            ));
            delay.play();
        }
    }

    /**
     * Handles the logic for when the player loses the game.
     * <p>
     * This method updates the restart button to reflect the loss, reveals unflagged bombs,
     * and transitions back to the menu after playing sound effects (if not muted).
     */
    private void lossGame()
    {
        updateRestartGameButton("lose"); // Updates the restart button to display a losing emoji.

        // Collects all bomb cells that are not flagged.
        List<Cell> bombCells = new ArrayList<>();
        for (Cell cell : getCells())
        {
            if (cell.isBomb() && !cell.isFlagged())
            {
                bombCells.add(cell);
            }
        }

        if (isMuted())
        {
            // If the game is muted, silently reveal unflagged bombs and transition back to the menu.
            for (Cell bombCell : bombCells)
            {
                bombCell.silentBombReveal(false);
            }
            new Timeline(new KeyFrame(Duration.seconds(3), event -> switchToMenu())).play();
        } else
        {
            // Play a bomb explosion sound and reveal bombs with animation.
            String bombExplosionSound = getRandomBombExplosionSound();
            playSoundEffect(bombExplosionSound, getStyle());

            Timeline revealBombsTimeLine = new Timeline();
            for (Cell bombCell : bombCells)
            {
                revealBombsTimeLine.getKeyFrames().add(
                        new KeyFrame(
                                Duration.seconds(MusicPlayer.getSoundEffectDuration(bombExplosionSound, getStyle()) - 0.5),
                                event -> bombCell.silentBombReveal(false)
                        )
                );
            }

            revealBombsTimeLine.setOnFinished(event ->
            {
                // After bombs are revealed, play a losing jingle and transition to the menu.
                String loseJingle = getRandomLoseJingle();
                playSoundEffect(loseJingle, getStyle());

                new Timeline(new KeyFrame(
                        Duration.seconds(MusicPlayer.getSoundEffectDuration(loseJingle, getStyle()) - 0.5),
                        ev -> switchToMenu()
                )).play();
            });

            revealBombsTimeLine.play(); // Starts the bomb reveal timeline.
        }
    }

    /**
     * Highlights incorrectly flagged cells on the game board.
     * <p>
     * This method iterates through all cells and identifies those that are flagged
     * but do not contain a bomb. These cells are marked with a "false-flag" icon
     * and their button styles are updated to indicate they have been revealed.
     */
    private void showIncorrectFlags()
    {
        for (Cell cell : getCells())
        {
            if (cell.isFlagged() && !cell.isBomb())
            {
                cell.setIconForButton("false-flag"); // Sets an icon to indicate the flag was incorrect.
                cell.getButton().getStyleClass().add("cell-button-revealed"); // Updates the button style to show it as revealed.
            }
        }
    }

    /**
     * Updates the player's best time for the current difficulty level if the current time is better.
     * <p>
     * This method loads the player's user data, compares the elapsed game time with the stored best time
     * for the current difficulty, and updates it if the current time is faster. The updated data is then saved.
     */
    private void updateBestTime()
    {
        UserData userData = UserDataManager.loadUserData(); // Loads the player's user data.

        // Compares and updates the best time based on the current difficulty level.
        switch (getDifficulty())
        {
            case "beginner" ->
            {
                if (getElapsedTime() < userData.getBeginnerBestTime())
                {
                    userData.setBeginnerBestTime(getElapsedTime()); // Updates the beginner best time.
                }
            }
            case "advanced" ->
            {
                if (getElapsedTime() < userData.getAdvancedBestTime())
                {
                    userData.setAdvancedBestTime(getElapsedTime()); // Updates the advanced best time.
                }
            }
            case "pro" ->
            {
                if (getElapsedTime() < userData.getProBestTime())
                {
                    userData.setProBestTime(getElapsedTime()); // Updates the pro best time.
                }
            }
        }

        UserDataManager.saveUserData(userData); // Saves the updated user data.
    }

    /**
     * Switches from the current game to the main menu.
     * <p>
     * This method creates a new instance of the {@code MenuController} with the current player's settings
     * (username, style, and mute status) and starts the main menu scene.
     */
    private void switchToMenu()
    {
        new MenuController(getUsername(), getStyle(), isMuted()).start(getStage()); // Starts the main menu with the current settings.
    }

    /**
     * Checks whether the player has won the game by revealing all non-bomb cells.
     * <p>
     * This method iterates through all cells on the game board and determines if all cells
     * that do not contain bombs have been revealed. If this condition is met, the game is ended
     * with a win state.
     */
    public void checkWinCondition()
    {
        // Checks if all non-bomb cells have been revealed.
        boolean allNotBombCellsRevealed = getCells().stream()
                .filter(cell -> !cell.isBomb()) // Filters out bomb cells.
                .allMatch(Cell::isRevealed); // Ensures all remaining cells are revealed.

        if (allNotBombCellsRevealed)
        {
            endGame(true); // Ends the game with a win state if the condition is met.
        }
    }

    /**
     * Initializes the user interface elements for the game.
     * <p>
     * This method sets up the main components of the game's user interface, including
     * the game information box (e.g., timer and flags) and the game field (grid of cells).
     */
    private void initializeUserElements()
    {
        initializeGameInfoBox(); // Sets up the game information box with elements like the timer and flag counter.
        initializeGameField();   // Sets up the game field (grid of cells) for the current difficulty level.
    }

    /**
     * Initializes the game information box that displays key gameplay information.
     * <p>
     * This method sets up a horizontal box (`HBox`) to organize and display elements such as
     * the remaining flags counter, restart button, and timer. Spacers are used to ensure proper alignment.
     */
    private void initializeGameInfoBox()
    {
        setGameInfoBox(new HBox()); // Creates a new HBox for the game information box.
        getGameInfoBox().setAlignment(Pos.CENTER); // Centers all elements within the box.
        getGameInfoBox().getStyleClass().add("game-info-box"); // Applies a style class to the box.

        // Adds spacers to ensure even distribution of elements.
        Region leftSpacer = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS); // Allows the left spacer to grow horizontally.
        HBox.setHgrow(rightSpacer, Priority.ALWAYS); // Allows the right spacer to grow horizontally.

        // Initializes the individual components of the game info box.
        initializeRemainingFlagsCounterBox(); // Sets up the box for the remaining flags counter.
        initializeRestartGameButton(); // Sets up the restart game button.
        initializeTimerBox(); // Sets up the timer display.

        // Adds all components and spacers to the game information box.
        getGameInfoBox().getChildren().addAll(
                getRemainingFlagCounterBox(), // Box displaying the remaining flags.
                leftSpacer,                  // Spacer to separate elements.
                getRestartGameButton(),      // Restart button.
                rightSpacer,                 // Spacer to separate elements.
                getTimerBox()                // Box displaying the timer.
        );
    }

    /**
     * Initializes the box displaying the remaining flags counter.
     * <p>
     * This method creates a horizontal box (`HBox`) to hold the label showing the number
     * of remaining flags. The box is styled and aligned for proper display.
     */
    private void initializeRemainingFlagsCounterBox()
    {
        setRemainingFlagCounterBox(new HBox()); // Creates a new HBox for the remaining flags counter.
        getRemainingFlagCounterBox().setAlignment(Pos.CENTER); // Centers the content within the box.
        getRemainingFlagCounterBox().getStyleClass().add("info-box"); // Applies a style class to the box.

        initializeRemainingFlagsCounterLabel(); // Initializes the label displaying the remaining flags.

        getRemainingFlagCounterBox().getChildren().add(getRemainingFlagCounterLabel()); // Adds the label to the box.
    }

    /**
     * Initializes the label displaying the remaining flags count.
     * <p>
     * This method creates a {@link Label} to show the number of remaining flags,
     * applies styles to it, and sets its initial value based on the total number of mines.
     */
    private void initializeRemainingFlagsCounterLabel()
    {
        setRemainingFlagCounterLabel(new Label(formatCounter(getTotalMines()))); // Creates the label with the initial count of total mines.
        getRemainingFlagCounterLabel().getStyleClass().add("info-label"); // Adds a style class for consistent styling.
        getRemainingFlagCounterLabel().getStyleClass().add("flag-label"); // Adds a style class for consistent styling.
        getRemainingFlagCounterLabel().setId("counter-red"); // Sets the label's ID to indicate it starts in the "red" state.
    }

    /**
     * Updates the label displaying the number of remaining flags.
     * <p>
     * This method calculates the number of remaining flags by subtracting the count of flagged cells
     * from the total number of mines. It updates the label's text and styling accordingly.
     * If the remaining flags count reaches zero, a click event listener is added to reveal all unflagged cells.
     */
    public void updateRemainingFlagsCounter()
    {
        // Calculates the remaining bombs by subtracting flagged cells from the total mines.
        int remainingBombs = getTotalMines() - (int) getCells().stream().filter(Cell::isFlagged).count();

        getRemainingFlagCounterLabel().setText(formatCounter(remainingBombs)); // Updates the label with the remaining bombs count.

        // Changes the label's style based on the remaining bombs count.
        if (remainingBombs == 0)
        {
            getRemainingFlagCounterLabel().setId("counter-teal"); // Changes the label to teal when no flags remain.
        } else
        {
            getRemainingFlagCounterLabel().setId("counter-red"); // Sets the label to red otherwise.
        }

        // Adds a click event listener to reveal all unflagged cells if there are no remaining bombs.
        getRemainingFlagCounterLabel().setOnMouseClicked(event ->
        {
            if (remainingBombs == 0)
            {
                revealAllUnflaggedCellsWithSound(); // Reveals all unflagged cells with sound effects.
            }
        });
    }

    /**
     * Reveals all unflagged and unrevealed cells on the game board with sound effects.
     * <p>
     * This method stops the background music, reveals all cells that are not flagged
     * and have not been previously revealed, and determines whether the player has won
     * or lost the game based on the revealed cells.
     */
    private void revealAllUnflaggedCellsWithSound()
    {
        stopBackgroundMusic(); // Stops the background music before revealing cells.

        // Reveals all cells that are not flagged and have not been revealed yet.
        getCells().stream()
                .filter(cell -> !cell.isFlagged() && !cell.isRevealed())
                .forEach(cell -> cell.reveal(true)); // Reveal each cell with animation/sound.

        // Ends the game based on whether any revealed cells are bombs.
        endGame(getCells().stream()
                .filter(Cell::isRevealed) // Filters revealed cells.
                .noneMatch(Cell::isBomb)); // If no revealed cells are bombs, the player wins.
    }

    /**
     * Initializes the restart game button.
     * <p>
     * This method creates and configures a button that allows the player to restart the game.
     * It sets the button's style, initial state, and action handler for restarting the game.
     */
    private void initializeRestartGameButton()
    {
        setRestartGameButton(new Button()); // Creates a new button for restarting the game.
        getRestartGameButton().setFocusTraversable(false); // Disables keyboard focus traversal for the button.
        getRestartGameButton().getStyleClass().add("restart-button"); // Adds a CSS class for styling the button.

        updateRestartGameButton("neutral"); // Sets the button's initial state to "neutral".

        // Assigns an action handler to restart the game when the button is clicked.
        getRestartGameButton().setOnAction(event -> restartGame());
    }

    /**
     * Updates the appearance of the restart game button based on the game status.
     * <p>
     * This method changes the text (emoji) displayed on the restart button to visually represent
     * the current game state, such as a win, loss, nervousness, or default (neutral) state.
     *
     * @param status The current game status, which determines the button's emoji:
     *               <ul>
     *                  <li>{@code "win"}: Displays a sunglasses emoji ("😎") for a win.</li>
     *                  <li>{@code "lose"}: Displays a dizzy face emoji ("😵") for a loss.</li>
     *                  <li>{@code "nervous"}: Displays a nervous face emoji ("😯") for nervousness.</li>
     *                  <li>Any other value: Displays a smiling face emoji ("🙂") as the default state.</li>
     *               </ul>
     */
    private void updateRestartGameButton(String status)
    {
        switch (status)
        {
            case "win" -> getRestartGameButton().setText("😎"); // Sets the button text to a win emoji.
            case "lose" -> getRestartGameButton().setText("😵"); // Sets the button text to a loss emoji.
            case "nervous" -> getRestartGameButton().setText("😯"); // Sets the button text to a nervous emoji.
            default -> getRestartGameButton().setText("🙂"); // Sets the button text to a neutral emoji.
        }
    }

    /**
     * Initializes the timer box that displays the elapsed time during the game.
     * <p>
     * This method creates a horizontal box (`HBox`) to hold the timer label,
     * aligns it at the center, and applies a style class for consistent styling.
     */
    private void initializeTimerBox()
    {
        setTimerBox(new HBox()); // Creates a new HBox for the timer display.
        getTimerBox().setAlignment(Pos.CENTER); // Centers the content within the box.
        getTimerBox().getStyleClass().add("info-box"); // Applies a CSS style class for the timer box.

        initializeTimerLabel(); // Initializes the label that displays the elapsed time.
    }

    /**
     * Initializes the label displaying the elapsed game time.
     * <p>
     * This method creates a {@link Label} to show the current game timer, applies a style class
     * for consistent styling, and adds the label to the timer box.
     */
    private void initializeTimerLabel()
    {
        setTimerLabel(new Label(formatTime(getElapsedTime()))); // Creates the label and sets its initial text to the formatted elapsed time.
        getTimerLabel().getStyleClass().add("info-label"); // Applies a style class for consistent styling.
        getTimerLabel().getStyleClass().add("time-label"); // Applies a style class for consistent styling.

        getTimerBox().getChildren().add(getTimerLabel()); // Adds the timer label to the timer box.
    }

    /**
     * Starts the game timer.
     * <p>
     * This method initializes a {@link Timeline} that increments the elapsed game time every second
     * and updates the timer label with the formatted time. The timer runs indefinitely until it is stopped.
     */
    public void startTimer()
    {
        // Creates a new Timeline that updates the elapsed time and timer label every second.
        setTimer(new Timeline(new KeyFrame(Duration.seconds(1), event ->
        {
            setElapsedTime(getElapsedTime() + 1); // Increments the elapsed time by one second.
            getTimerLabel().setText(formatTime(getElapsedTime())); // Updates the timer label with the formatted time.
        })));

        getTimer().setCycleCount(Timeline.INDEFINITE); // Sets the timer to run indefinitely.
        getTimer().play(); // Starts the timer.
    }

    /**
     * Stops the game timer.
     * <p>
     * This method checks if the timer is running and stops it to prevent further updates
     * to the elapsed game time.
     */
    private void stopTimer()
    {
        // Ensures that the timer is not null before attempting to stop it.
        if (getTimer() != null)
        {
            getTimer().stop(); // Stops the timer.
        }
    }

    /**
     * Initializes the game field based on the selected difficulty level.
     * <p>
     * This method determines the number of rows, columns, and total mines for the game field
     * based on the current difficulty setting. After determining these parameters, it calls
     * {@code initializeField} to set up the field.
     *
     * @throws IllegalArgumentException if the difficulty level is not recognized.
     */
    private void initializeGameField()
    {
        int rows;
        int columns;

        // Determines the field size and total mines based on the difficulty level.
        switch (getDifficulty())
        {
            case "beginner":
                rows = 8;
                columns = 8;
                setTotalMines(10); // Sets total mines for beginner difficulty.
                break;
            case "advanced":
                rows = 16;
                columns = 16;
                setTotalMines(40); // Sets total mines for advanced difficulty.
                break;
            case "pro":
                rows = 16;
                columns = 30;
                setTotalMines(99); // Sets total mines for pro difficulty.
                break;
            default:
                throw new IllegalArgumentException("Unknown Difficulty: " + getDifficulty()); // Handles invalid difficulty levels.
        }

        initializeField(rows, columns); // Calls the method to set up the game field with the determined parameters.
    }

    /**
     * Initializes the game field with the specified number of rows and columns.
     * <p>
     * This method sets up the grid for the game field, creates all cells, randomly places bombs,
     * calculates adjacent bomb counts for each cell, and adds the cells to the grid layout.
     *
     * @param rows    The number of rows in the game field.
     * @param columns The number of columns in the game field.
     */
    private void initializeField(int rows, int columns)
    {
        setRows(rows); // Sets the number of rows in the game field.
        setColumns(columns); // Sets the number of columns in the game field.
        setCells(new ArrayList<>()); // Initializes the list of cells.
        initializeGameFieldGridPane(); // Configures the grid layout for the game field.

        // Creates cells for each position in the grid.
        for (int row = 0; row < rows; row++)
        {
            for (int col = 0; col < columns; col++)
            {
                Cell cell = new Cell(getStyle(), false, this, row, col); // Creates a new cell.

                // Adds mouse press and release event handlers for the cell button.
                cell.getButton().setOnMousePressed(event ->
                {
                    if (event.isPrimaryButtonDown() || event.isSecondaryButtonDown())
                    {
                        updateRestartGameButton("nervous"); // Updates the restart button to "nervous" on mouse press.
                    }
                });

                cell.getButton().setOnMouseReleased(event ->
                {
                    if (!event.isPrimaryButtonDown() || !event.isSecondaryButtonDown())
                    {
                        updateRestartGameButton("neutral"); // Resets the restart button to "neutral" on mouse release.
                    }
                });

                getCells().add(cell); // Adds the cell to the list of cells.
            }
        }

        // Randomly places bombs in the field.
        Random random = new Random();
        int bombsPlaced = 0;
        while (bombsPlaced < getTotalMines())
        {
            int randomRow = random.nextInt(rows);
            int randomCol = random.nextInt(columns);

            Cell cell = getCellAt(randomRow, randomCol); // Retrieves a cell at a random position.
            if (!cell.isBomb())
            { // Ensures the cell does not already contain a bomb.
                cell.setBomb(true); // Places a bomb in the cell.
                bombsPlaced++;
            }
        }

        // Calculates the number of adjacent bombs for each cell.
        for (int row = 0; row < rows; row++)
        {
            for (int col = 0; col < columns; col++)
            {
                Cell cell = getCellAt(row, col);
                if (!cell.isBomb())
                {
                    cell.setAdjacentBombs(countAdjacentBombs(row, col)); // Sets the number of adjacent bombs for the cell.
                }
            }
        }

        // Adds cells to the grid layout.
        for (int row = 0; row < rows; row++)
        {
            for (int col = 0; col < columns; col++)
            {
                Cell cell = getCellAt(row, col);
                getGameField().add(cell.getButton(), col, row); // Adds the cell's button to the grid layout.
            }
        }

        updateRemainingFlagsCounter(); // Updates the flag counter after initializing the field.
    }

    /**
     * Initializes the {@link GridPane} layout for the game field.
     * <p>
     * This method creates a new {@code GridPane}, centers its alignment, applies styles,
     * and configures column and row constraints to ensure all cells are evenly distributed.
     */
    private void initializeGameFieldGridPane()
    {
        setGameField(new GridPane()); // Creates a new GridPane for the game field.
        getGameField().setAlignment(Pos.CENTER); // Centers the grid within its container.
        getGameField().getStyleClass().add("game-field"); // Applies a style class to the grid.

        double gap = 0; // Sets the horizontal and vertical gaps between cells.
        getGameField().setHgap(gap); // Sets the horizontal gap between cells.
        getGameField().setVgap(gap); // Sets the vertical gap between cells.

        // Configures column constraints to evenly distribute the columns.
        for (int i = 0; i < getColumns(); i++)
        {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPercentWidth(100d / getColumns()); // Ensures columns are evenly sized.
            getGameField().getColumnConstraints().add(colConstraints); // Adds the column constraints to the grid.
        }

        // Configures row constraints to evenly distribute the rows.
        for (int i = 0; i < getRows(); i++)
        {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100d / getRows()); // Ensures rows are evenly sized.
            getGameField().getRowConstraints().add(rowConstraints); // Adds the row constraints to the grid.
        }
    }

    /**
     * Counts the number of bombs adjacent to a specified cell.
     * <p>
     * This method checks all neighboring cells within a 3x3 grid around the given cell's
     * position (including diagonals) and counts how many of them contain bombs.
     *
     * @param row The row index of the cell to check.
     * @param col The column index of the cell to check.
     * @return The number of adjacent cells containing bombs.
     */
    private int countAdjacentBombs(int row, int col)
    {
        int count = 0;

        // Iterates through the 3x3 grid around the specified cell.
        for (int r = row - 1; r <= row + 1; r++)
        {
            for (int c = col - 1; c <= col + 1; c++)
            {
                // Checks if the neighboring cell is within bounds and contains a bomb.
                if (isInBounds(r, c) && getCellAt(r, c).isBomb())
                {
                    count++; // Increments the count if a bomb is found.
                }
            }
        }

        return count; // Returns the total number of adjacent bombs.
    }

    /**
     * Checks whether a specified cell position is within the bounds of the game field.
     *
     * @param row The row index of the cell to check.
     * @param col The column index of the cell to check.
     * @return {@code true} if the cell is within the bounds of the game field; {@code false} otherwise.
     */
    public boolean isInBounds(int row, int col)
    {
        return row >= 0 && row < getGameField().getRowCount() // Checks if the row index is within bounds.
                && col >= 0 && col < getGameField().getColumnCount(); // Checks if the column index is within bounds.
    }

    /**
     * Retrieves the cell at the specified row and column in the game field.
     *
     * @param row The row index of the cell to retrieve.
     * @param col The column index of the cell to retrieve.
     * @return The {@link Cell} at the specified position.
     * @throws IndexOutOfBoundsException if the specified row or column is out of bounds.
     */
    public Cell getCellAt(int row, int col)
    {
        int columns = getGameField().getColumnCount(); // Gets the number of columns in the game field.
        return getCells().get(row * columns + col); // Calculates the 1D index and retrieves the cell.
    }

    /**
     * Formats the given time as a three-digit string.
     * <p>
     * This method ensures that the time is always displayed with at least three digits,
     * padding with leading zeros if necessary.
     *
     * @param time The time value to format (in seconds).
     * @return A three-digit string representation of the time.
     */
    private String formatTime(int time)
    {
        return String.format("%04d", time); // Formats the time as a four-digit string with leading zeros.
    }

    /**
     * Formats the given counter value as a string with leading zeros.
     * <p>
     * This method handles both positive and negative values. Positive numbers are displayed
     * as three digits with leading zeros, while negative numbers include a leading minus sign
     * and are displayed as two digits with leading zeros after the minus sign.
     *
     * @param count The counter value to format.
     * @return A string representation of the counter, formatted with leading zeros.
     */
    private String formatCounter(int count)
    {
        if (count < 0)
        {
            return String.format("-%02d", Math.abs(count)); // Formats negative numbers as "-XX" with two digits.
        } else
        {
            return String.format("%03d", count); // Formats positive numbers as "XXX" with three digits.
        }
    }

    /**
     * Generates a random win jingle filename.
     * <p>
     * This method randomly selects between two win jingles, returning the filename
     * of either "win-jingle-1" or "win-jingle-2".
     *
     * @return A string representing the filename of a randomly chosen win jingle.
     */
    private String getRandomWinJingle()
    {
        return "win-jingle-" + (Math.random() < 0.5 ? "1" : "2"); // Randomly returns "win-jingle-1" or "win-jingle-2".
    }

    /**
     * Generates a random lose jingle filename.
     * <p>
     * This method randomly selects between two lose jingles, returning the filename
     * of either "lose-jingle-1" or "lose-jingle-2".
     *
     * @return A string representing the filename of a randomly chosen lose jingle.
     */
    private String getRandomLoseJingle()
    {
        return "lose-jingle-" + (Math.random() < 0.5 ? "1" : "2"); // Randomly returns "lose-jingle-1" or "lose-jingle-2".
    }

    /**
     * Generates a random bomb explosion sound filename.
     * <p>
     * This method randomly selects between two bomb explosion sounds, returning the filename
     * of either "bomb-explosion-1" or "bomb-explosion-2".
     *
     * @return A string representing the filename of a randomly chosen bomb explosion sound.
     */
    private String getRandomBombExplosionSound()
    {
        return "bomb-explosion-" + (Math.random() < 0.5 ? "1" : "2"); // Randomly returns "bomb-explosion-1" or "bomb-explosion-2".
    }

    /**
     * Gets the maximum width for the HBox layout in the game window.
     *
     * @return The maximum width for HBox elements, in pixels.
     */
    public static int getMaxHBoxWidth()
    {
        return maxHBoxWidth;
    }

    /**
     * Gets the maximum height for the HBox layout in the game window.
     *
     * @return The maximum height for HBox elements, in pixels.
     */
    public static int getMaxHBoxHeight()
    {
        return maxHBoxHeight;
    }

    /**
     * Gets the current difficulty level of the game.
     *
     * @return A string representing the difficulty level (e.g., "beginner", "advanced", "pro").
     */
    public String getDifficulty()
    {
        return difficulty;
    }

    /**
     * Sets the difficulty level of the game.
     *
     * @param difficulty A string representing the difficulty level (e.g., "beginner", "advanced", "pro").
     */
    public void setDifficulty(String difficulty)
    {
        this.difficulty = difficulty;
    }

    /**
     * Gets the {@link Scene} object representing the current game scene.
     *
     * @return The current game scene.
     */
    public Scene getGameScene()
    {
        return gameScene;
    }

    /**
     * Sets the current game scene.
     *
     * @param gameScene The {@link Scene} object representing the game scene.
     */
    public void setGameScene(Scene gameScene)
    {
        this.gameScene = gameScene;
    }

    /**
     * Gets the root container of the game UI.
     *
     * @return The root {@link VBox} containing all game elements.
     */
    public VBox getRoot()
    {
        return root;
    }

    /**
     * Gets the current stage of the game.
     *
     * @return The {@link Stage} object representing the game window.
     */
    public Stage getStage()
    {
        return stage;
    }

    /**
     * Sets the current stage for the game.
     *
     * @param stage The {@link Stage} object representing the game window.
     */
    public void setStage(Stage stage)
    {
        this.stage = stage;
    }

    /**
     * Gets the game information box containing elements such as the timer and remaining flags counter.
     *
     * @return The {@link HBox} containing game information elements.
     */
    public HBox getGameInfoBox()
    {
        return gameInfoBox;
    }

    /**
     * Sets the game information box.
     *
     * @param gameInfoBox The {@link HBox} containing game information elements.
     */
    public void setGameInfoBox(HBox gameInfoBox)
    {
        this.gameInfoBox = gameInfoBox;
    }

    /**
     * Gets the box displaying the remaining flags counter.
     *
     * @return The {@link HBox} for the remaining flags counter.
     */
    public HBox getRemainingFlagCounterBox()
    {
        return remainingFlagCounterBox;
    }

    /**
     * Sets the box displaying the remaining flags counter.
     *
     * @param remainingFlagCounterBox The {@link HBox} for the remaining flags counter.
     */
    public void setRemainingFlagCounterBox(HBox remainingFlagCounterBox)
    {
        this.remainingFlagCounterBox = remainingFlagCounterBox;
    }

    /**
     * Gets the box displaying the game timer.
     *
     * @return The {@link HBox} for the game timer.
     */
    public HBox getTimerBox()
    {
        return timerBox;
    }

    /**
     * Sets the box displaying the game timer.
     *
     * @param timerBox The {@link HBox} for the game timer.
     */
    public void setTimerBox(HBox timerBox)
    {
        this.timerBox = timerBox;
    }

    /**
     * Gets the restart game button.
     *
     * @return The {@link Button} used to restart the game.
     */
    public Button getRestartGameButton()
    {
        return restartGameButton;
    }

    /**
     * Sets the restart game button.
     *
     * @param restartGameButton The {@link Button} used to restart the game.
     */
    public void setRestartGameButton(Button restartGameButton)
    {
        this.restartGameButton = restartGameButton;
    }

    /**
     * Gets the {@link GridPane} representing the game field.
     *
     * @return The game field as a {@link GridPane}.
     */
    public GridPane getGameField()
    {
        return gameField;
    }

    /**
     * Sets the {@link GridPane} representing the game field.
     *
     * @param gameField The game field to set.
     */
    public void setGameField(GridPane gameField)
    {
        this.gameField = gameField;
    }

    /**
     * Gets the {@link Label} displaying the timer.
     *
     * @return The timer label.
     */
    public Label getTimerLabel()
    {
        return timerLabel;
    }

    /**
     * Sets the {@link Label} displaying the timer.
     *
     * @param timerLabel The timer label to set.
     */
    public void setTimerLabel(Label timerLabel)
    {
        this.timerLabel = timerLabel;
    }

    /**
     * Gets the elapsed time in seconds.
     *
     * @return The elapsed time as an integer.
     */
    public int getElapsedTime()
    {
        return elapsedTime;
    }

    /**
     * Sets the elapsed time in seconds.
     *
     * @param elapsedTime The elapsed time to set.
     */
    public void setElapsedTime(int elapsedTime)
    {
        this.elapsedTime = elapsedTime;
    }

    /**
     * Gets the {@link Timeline} object controlling the game timer.
     *
     * @return The game timer.
     */
    public Timeline getTimer()
    {
        return timer;
    }

    /**
     * Sets the {@link Timeline} object controlling the game timer.
     *
     * @param timer The game timer to set.
     */
    public void setTimer(Timeline timer)
    {
        this.timer = timer;
    }

    /**
     * Gets the {@link Label} displaying the remaining flag counter.
     *
     * @return The remaining flag counter label.
     */
    public Label getRemainingFlagCounterLabel()
    {
        return remainingFlagCounterLabel;
    }

    /**
     * Sets the {@link Label} displaying the remaining flag counter.
     *
     * @param remainingFlagCounterLabel The remaining flag counter label to set.
     */
    public void setRemainingFlagCounterLabel(Label remainingFlagCounterLabel)
    {
        this.remainingFlagCounterLabel = remainingFlagCounterLabel;
    }

    /**
     * Gets the total number of mines in the game field.
     *
     * @return The total number of mines as an integer.
     */
    public int getTotalMines()
    {
        return totalMines;
    }

    /**
     * Sets the total number of mines in the game field.
     *
     * @param totalMines The total number of mines to set.
     */
    public void setTotalMines(int totalMines)
    {
        this.totalMines = totalMines;
    }

    /**
     * Gets the list of all {@link Cell} objects in the game field.
     *
     * @return A list of {@link Cell} objects.
     */
    public List<Cell> getCells()
    {
        return cells;
    }

    /**
     * Sets the list of all {@link Cell} objects in the game field.
     *
     * @param cells The list of {@link Cell} objects to set.
     */
    public void setCells(List<Cell> cells)
    {
        this.cells = cells;
    }

    /**
     * Gets the number of rows in the game field.
     *
     * @return The number of rows as an integer.
     */
    public int getRows()
    {
        return rows;
    }

    /**
     * Sets the number of rows in the game field.
     *
     * @param rows The number of rows to set.
     */
    public void setRows(int rows)
    {
        this.rows = rows;
    }

    /**
     * Gets the number of columns in the game field.
     *
     * @return The number of columns as an integer.
     */
    public int getColumns()
    {
        return columns;
    }

    /**
     * Sets the number of columns in the game field.
     *
     * @param columns The number of columns to set.
     */
    public void setColumns(int columns)
    {
        this.columns = columns;
    }

    /**
     * Checks if the current click is the first click of the game.
     *
     * @return {@code true} if it is the first click, {@code false} otherwise.
     */
    public boolean isFirstClick()
    {
        return firstClick;
    }

    /**
     * Sets whether the current click is the first click of the game.
     *
     * @param firstClick {@code true} if it is the first click, {@code false} otherwise.
     */
    public void setFirstClick(boolean firstClick)
    {
        this.firstClick = firstClick;
    }

    /**
     * Checks if the game is muted.
     *
     * @return {@code true} if the game is muted, {@code false} otherwise.
     */
    public boolean isMuted()
    {
        return muted;
    }

    /**
     * Sets whether the game is muted.
     *
     * @param muted {@code true} to mute the game, {@code false} to enable sound.
     */
    public void setMuted(boolean muted)
    {
        this.muted = muted;
    }

    /**
     * Gets the current visual style of the game.
     *
     * @return A string representing the visual style (e.g., "retro", "modern").
     */
    public String getStyle()
    {
        return style;
    }

    /**
     * Sets the visual style of the game.
     *
     * @param style A string representing the visual style (e.g., "retro", "modern").
     */
    public void setStyle(String style)
    {
        this.style = style;
    }

    /**
     * Gets the username of the current player.
     *
     * @return A string representing the player's username.
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Sets the username of the current player.
     *
     * @param username A string representing the player's username.
     */
    public void setUsername(String username)
    {
        this.username = username;
    }
}
