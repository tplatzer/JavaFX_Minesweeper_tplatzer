package htl.steyr.javafx_minesweeper_tplatzer.controller;

import htl.steyr.javafx_minesweeper_tplatzer.model.UserData;
import htl.steyr.javafx_minesweeper_tplatzer.model.UserDataManager;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Controller class for managing the main menu UI in the Minesweeper game.
 * <p>
 * This class handles the main menu functionality, including:
 * <ul>
 *     <li>Starting the game with selected difficulty</li>
 *     <li>Accessing the leaderboard</li>
 *     <li>Changing the username</li>
 *     <li>Toggling the visual style and sound effects</li>
 *     <li>Resetting local best times</li>
 * </ul>
 */
public class MenuController extends Controller
{
    /**
     * The maximum width for HBox containers in the menu.
     */
    private static final int maxHBoxWidth = 1000;

    /**
     * The maximum height for HBox containers in the menu.
     */
    private static final int maxHBoxHeight = 700;

    /**
     * The maximum width for VBox containers in the menu.
     */
    private static final int maxVBoxWidth = 1000;

    /**
     * The maximum height for VBox containers in the menu.
     */
    private static final int maxVBoxHeight = 200;

    /**
     * The maximum width for buttons in the menu.
     */
    private static final int maxButtonWidth = 200;

    /**
     * The maximum height for buttons in the menu.
     */
    private static final int maxButtonHeight = 100;

    // UI components.
    /**
     * The {@link Stage} representing the menu window.
     */
    private Stage stage;

    /**
     * The {@link Scene} for the menu UI.
     */
    private Scene menuScene;

    /**
     * The root container for all menu components, represented as a {@link VBox}.
     */
    private final VBox root = new VBox();

    /**
     * A {@link VBox} container for the header section of the menu, including the title and username.
     */
    private VBox headerContainer;

    /**
     * A {@link HBox} container for the menu title.
     */
    private HBox headerBox;

    /**
     * The {@link Label} displaying the title of the menu.
     */
    private Label titleLabel;

    /**
     * A {@link HBox} container for the username section.
     */
    private HBox usernameBox;

    /**
     * The {@link Label} displaying the current username.
     */
    private Label usernameLabel;

    /**
     * A {@link Text} element displaying the "Choose-a-Gamemode" message.
     */
    private Text choiceText;

    /**
     * A {@link HBox} container for difficulty selection buttons.
     */
    private HBox difficultyBox;

    /**
     * A {@link VBox} container for the game mode selection section.
     */
    private VBox chooseGameModeBox;

    /**
     * A {@link HBox} container for control buttons like "Mute SFX" and "Choose Style".
     */
    private HBox controlButtonsBox;

    /**
     * The {@link Button} for toggling the visual style (e.g., "retro" or "modern").
     */
    private Button chooseStyleButton;

    /**
     * The {@link Button} for opening the leaderboard window.
     */
    private Button leaderboardButton;

    /**
     * The {@link Button} for toggling sound effects.
     */
    private Button muteSfxButton;

    /**
     * A {@link HBox} container for the footer section, including the reset and leaderboard buttons.
     */
    private HBox footerBox;

    /**
     * The {@link Button} for resetting local best times.
     */
    private Button resetLocalBestTimesButton;

    /**
     * The current visual style of the menu (e.g., "retro" or "modern").
     */
    private String style;

    /**
     * Indicates whether sound effects are muted.
     */
    private boolean muted;

    /**
     * The username of the player.
     */
    private String username;


    /**
     * Constructs a new {@code MenuController} with the specified username, style, and mute settings.
     * <p>
     * This constructor initializes the controller with the player's username, the chosen visual style,
     * and whether sound effects are muted.
     *
     * @param username The username of the player.
     * @param style    The visual style of the menu (e.g., "retro", "modern").
     * @param muted    {@code true} if sound effects are muted, {@code false} otherwise.
     */
    public MenuController(String username, String style, boolean muted)
    {
        setUsername(username); // Sets the player's username.
        setStyle(style);       // Sets the menu's visual style.
        setMuted(muted);       // Sets whether sound effects are muted.
    }

    /**
     * Starts the main menu UI.
     * <p>
     * This method sets the {@link Stage} for the menu, initializes all UI components,
     * plays the background music if sound effects are not muted, and displays the menu window.
     *
     * @param stage The {@link Stage} where the menu will be displayed.
     */
    public void start(Stage stage)
    {
        setStage(stage); // Sets the stage for the menu.
        initializeUserElements(); // Initializes all user interface components.
        if (!isMuted()) playBackgroundMusic("menu-music", getStyle()); // Plays menu background music if not muted.

        initializeStage(getStage(), getStyle()); // Configures the stage with the selected style.
        initializeWindow(); // Sets up the menu window layout and appearance.
        getStage().show(); // Displays the menu window.
    }

    /**
     * Initializes the layout and appearance of the main menu window.
     * <p>
     * This method configures the root container, sets the layout of all components (header, game mode selection, footer),
     * applies stylesheets for visual styling, and prepares the scene for display.
     */
    private void initializeWindow()
    {
        getRoot().setSpacing(30); // Sets spacing between child elements in the root container.
        getRoot().setAlignment(Pos.CENTER); // Aligns all child elements in the center of the root container.
        getRoot().setMinSize(MenuController.getMaxHBoxWidth(), MenuController.getMaxHBoxHeight()); // Sets minimum dimensions for the root.
        getRoot().setMaxSize(MenuController.getMaxHBoxWidth(), MenuController.getMaxHBoxHeight()); // Sets maximum dimensions for the root.
        getRoot().prefWidthProperty().bind(getStage().widthProperty()); // Binds the root width to the stage width.
        getRoot().prefHeightProperty().bind(getStage().heightProperty()); // Binds the root height to the stage height.

        // Adds UI components to the root container.
        getRoot().getChildren().addAll(
                getHeaderBox(), // Header section containing the title and username.
                new Region(), // Spacer region for layout flexibility.
                getChooseGameModeBox(), // Section for selecting game modes.
                getFooterBox() // Footer section with additional controls.
        );

        // Applies CSS styles to the root container.
        getRoot().getStyleClass().add("root-container");
        getRoot().getStylesheets().addAll(
                Objects.requireNonNull(getClass().getResource("/" + getStyle() + "/style/style.css")).toExternalForm(),
                Objects.requireNonNull(getClass().getResource("/" + getStyle() + "/style/menuStyle.css")).toExternalForm()
        );

        // Creates and assigns a new scene to the menu.
        setMenuScene(new Scene(getRoot()));

        // Displays the menu scene in the stage.
        switchScene(getStage(), getMenuScene(), "Menu", 500, 1000);
    }

    /**
     * Starts a new game with the specified difficulty.
     * <p>
     * This method stops the background music of the menu and initializes a new instance of
     * {@link GameController} with the given difficulty level, the current username, style, and mute settings.
     * The game is displayed in the same {@link Stage}.
     *
     * @param difficulty The selected difficulty level (e.g., "beginner", "advanced", "pro").
     */
    private void startGame(String difficulty)
    {
        stopBackgroundMusic(); // Stops the menu background music.
        new GameController(getUsername(), difficulty, getStyle(), isMuted()).start(getStage()); // Starts a new game.
    }

    /**
     * Initializes the user interface elements of the menu.
     * <p>
     * This method sets up the primary sections of the menu, including:
     * <ul>
     *     <li>The header section (title and username)</li>
     *     <li>The game mode selection box</li>
     *     <li>The footer section (control buttons and other utilities)</li>
     * </ul>
     */
    private void initializeUserElements()
    {
        initializeHeaderBox(); // Sets up the header section of the menu.
        initializeChooseGameModeBox(); // Sets up the game mode selection section.
        initializeFooterBox(); // Sets up the footer section of the menu.
    }

    /**
     * Initializes the header section of the menu.
     * <p>
     * This method sets up the header, which includes:
     * <ul>
     *     <li>The username display (aligned to the top right, clickable to change the username)</li>
     *     <li>The title of the menu ("Bomb-Disposal-Simulator")</li>
     * </ul>
     * The header is added as the first child of the root container.
     */
    private void initializeHeaderBox()
    {
        setHeaderContainer(new VBox()); // Creates a VBox container for the header.
        getHeaderContainer().setAlignment(Pos.TOP_RIGHT); // Aligns the header elements to the top right.
        getHeaderContainer().prefWidthProperty().bind(getRoot().widthProperty()); // Binds the header width to the root width.
        getHeaderContainer().setSpacing(5); // Sets spacing between header elements.

        // Configures the username label.
        setUsernameLabel(new Label(formatUsername(getUsername()))); // Formats and sets the username label.
        getUsernameLabel().setId("usernameLabel"); // Assigns an ID for CSS styling.
        getUsernameLabel().setAlignment(Pos.TOP_RIGHT); // Aligns the username label to the top right.

        // Adds a click event to the username label to allow changing the username.
        getUsernameLabel().setOnMouseClicked(event -> changeUserName());

        // Configures the username box to contain the username label.
        setUsernameBox(new HBox(getUsernameLabel()));
        getUsernameBox().setAlignment(Pos.TOP_RIGHT); // Aligns the username box to the top right.
        getUsernameBox().prefWidthProperty().bind(getHeaderContainer().widthProperty()); // Binds the box width to the header container width.

        // Configures the title box.
        setHeaderBox(new HBox()); // Creates an HBox for the menu title.
        getHeaderBox().setSpacing(10); // Sets spacing between elements in the header box.
        getHeaderBox().setAlignment(Pos.CENTER); // Aligns the title in the center.
        getHeaderBox().prefWidthProperty().bind(getRoot().widthProperty()); // Binds the width of the header box to the root width.

        // Adds the menu title.
        setTitleLabel(new Label("Bomb-Disposal-Simulator")); // Sets the menu title.
        getTitleLabel().setId("titleText"); // Assigns an ID for CSS styling.
        getTitleLabel().setAlignment(Pos.CENTER); // Aligns the title text in the center.
        getHeaderBox().getChildren().add(getTitleLabel()); // Adds the title to the header box.

        // Adds the username box and title box to the header container.
        getHeaderContainer().getChildren().addAll(getUsernameBox(), getHeaderBox());

        // Adds the header container as the first child of the root container.
        getRoot().getChildren().addFirst(getHeaderContainer());
    }

    /**
     * Changes the username of the player.
     * <p>
     * This method stops the menu background music and opens the username selection menu
     * by initializing a new {@link UsernameMenuController}.
     * The current username, style, and mute settings are passed to the controller.
     */
    private void changeUserName()
    {
        stopBackgroundMusic(); // Stops the background music of the menu.
        // Opens the username selection menu with the current settings.
        new UsernameMenuController(getUsername(), getStyle(), isMuted()).start(getStage());
    }

    /**
     * Initializes the game mode selection box in the menu.
     * <p>
     * This method sets up a {@link VBox} container for selecting game modes and control buttons.
     * It includes a title, difficulty selection buttons, and additional control options (e.g., style, mute).
     */
    private void initializeChooseGameModeBox()
    {
        setChooseGameModeBox(new VBox()); // Creates a new VBox container for the game mode selection.

        // Configures the layout and dimensions of the game mode box.
        getChooseGameModeBox().setSpacing(10); // Sets spacing between child elements.
        getChooseGameModeBox().setAlignment(Pos.CENTER); // Centers all child elements.
        getChooseGameModeBox().setMinSize(MenuController.getMaxVBoxWidth(), MenuController.getMaxVBoxHeight()); // Sets the minimum size.
        getChooseGameModeBox().setMaxSize(MenuController.getMaxVBoxWidth(), MenuController.getMaxVBoxHeight()); // Sets the maximum size.

        // Adds a title to the game mode box.
        setChoiceText(initializeText("choiceText", "Choose-a-Gamemode")); // Initializes the title text.
        initializeDifficultyBoxes(); // Sets up the buttons for selecting difficulty levels.
        initializeControlButtonsBox(); // Sets up additional control buttons like "Mute" and "Style".

        // Adds the title, difficulty selection, and control buttons to the game mode box.
        getChooseGameModeBox().getChildren().addAll(
                getChoiceText(), // Title of the section.
                getDifficultyBox(), // Difficulty selection buttons.
                getControlButtonsBox() // Control buttons for additional options.
        );
    }

    /**
     * Initializes the control buttons box in the menu.
     * <p>
     * This method sets up a {@link HBox} container for the control buttons, which include:
     * <ul>
     *     <li>A button to toggle sound effects (Mute SFX)</li>
     *     <li>A button to toggle the visual style (Choose Style)</li>
     * </ul>
     */
    private void initializeControlButtonsBox()
    {
        // Initializes individual control buttons.
        initializeMuteSfxButton(); // Sets up the "Mute SFX" button.
        initializeChooseStyleButton(); // Sets up the "Choose Style" button.

        setControlButtonsBox(new HBox()); // Creates a new HBox container for the control buttons.
        getControlButtonsBox().setSpacing(10); // Sets spacing between the buttons.
        getControlButtonsBox().setAlignment(Pos.CENTER); // Centers the buttons horizontally in the box.

        // Adds the control buttons to the container.
        getControlButtonsBox().getChildren().addAll(getMuteSfxButton(), getChooseStyleButton());
    }

    /**
     * Initializes the difficulty selection boxes in the menu.
     * <p>
     * This method sets up an {@link HBox} container to hold the buttons for selecting game difficulty levels.
     * Each button is associated with a difficulty ("beginner", "advanced", "pro") and displays the best time for that level.
     */
    private void initializeDifficultyBoxes()
    {
        setDifficultyBox(new HBox()); // Creates a new HBox container for the difficulty selection.
        getDifficultyBox().setAlignment(Pos.CENTER); // Aligns the buttons in the center.
        getDifficultyBox().setSpacing(10); // Sets spacing between the difficulty buttons.
        getDifficultyBox().getStyleClass().add("box"); // Applies a CSS style class to the container.
        getDifficultyBox().setId("difficulty-button-box"); // Sets an ID for the container for additional styling.

        // Adds difficulty selection buttons for "beginner", "advanced", and "pro".
        getDifficultyBox().getChildren().addAll(
                createDifficultyBox("beginner", loadBestTime("beginner")), // Beginner button with best time.
                createDifficultyBox("advanced", loadBestTime("advanced")), // Advanced button with best time.
                createDifficultyBox("pro", loadBestTime("pro")) // Pro button with best time.
        );
    }

    /**
     * Initializes the footer section of the menu.
     * <p>
     * This method sets up an {@link HBox} container for the footer, which includes:
     * <ul>
     *     <li>A button to reset local best times</li>
     *     <li>A button to open the leaderboard</li>
     *     <li>A spacer to ensure proper alignment</li>
     * </ul>
     */
    private void initializeFooterBox()
    {
        setFooterBox(new HBox()); // Creates a new HBox container for the footer section.
        getFooterBox().setSpacing(10); // Sets spacing between elements in the footer.
        getFooterBox().setAlignment(Pos.CENTER_RIGHT); // Aligns the footer elements to the right.

        // Creates a spacer region to push buttons to the right.
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS); // Sets the spacer to grow and fill available space.

        // Initializes footer buttons.
        initializeResetLocalBestTimesButton(); // Creates the "Reset Local Best Times" button.
        initializeLeaderboardButton(); // Creates the "Leaderboard" button.

        // Configures the size of the reset button.
        getResetLocalBestTimesButton().setMaxSize(getMaxButtonWidth(), getMaxButtonHeight());

        // Adds buttons and spacer to the footer container.
        getFooterBox().getChildren().addAll(getResetLocalBestTimesButton(), spacer, getLeaderboardButton());
    }

    /**
     * Creates a difficulty selection box for a specific game difficulty.
     * <p>
     * This method generates a {@link VBox} containing:
     * <ul>
     *     <li>A button for selecting the specified difficulty level</li>
     *     <li>A label displaying the best time for that difficulty</li>
     * </ul>
     * The button and label are styled and aligned appropriately.
     *
     * @param difficulty The difficulty level (e.g., "beginner", "advanced", "pro").
     * @param bestTime   The player's best time for the specified difficulty.
     * @return A {@link VBox} containing the button and best time label for the specified difficulty.
     */
    private VBox createDifficultyBox(String difficulty, int bestTime)
    {
        // Create the container for the difficulty box.
        VBox difficultyBox = new VBox();
        difficultyBox.setSpacing(10); // Sets spacing between the button and the label.
        difficultyBox.setAlignment(Pos.CENTER); // Centers the elements in the box.

        // Create and configure the difficulty button.
        Button difficultyButton = new Button(difficulty.toUpperCase()); // Button text is the difficulty in uppercase.
        difficultyButton.setMaxSize(getMaxButtonWidth(), getMaxButtonHeight()); // Sets maximum size for the button.
        difficultyButton.getStyleClass().add("button"); // Applies a CSS style class for styling.
        difficultyButton.setId(difficulty); // Sets an ID for CSS or event handling.
        difficultyButton.setFocusTraversable(false); // Prevents the button from being focused when tabbing through UI elements.
        difficultyButton.setOnAction(event -> startGame(difficulty)); // Sets an action to start the game when clicked.
        HBox.setHgrow(difficultyButton, Priority.ALWAYS); // Allows the button to grow horizontally.
        difficultyButton.prefWidthProperty().bind(getDifficultyBox().widthProperty().divide(4).subtract(20)); // Dynamically adjusts the button width.
        difficultyButton.prefHeightProperty().bind(getDifficultyBox().heightProperty().multiply(0.3)); // Dynamically adjusts the button height.

        // Create and configure the label for the best time.
        Label bestTimeLabel = new Label(formatBestTime(bestTime)); // Formats the best time as a string.
        bestTimeLabel.getStyleClass().add("info-label"); // Applies a CSS style class for the label.
        bestTimeLabel.getStyleClass().add("time-label"); // Applies a CSS style class for the label.

        // Create a container for the best time label.
        VBox bestTimeBox = new VBox(bestTimeLabel);
        bestTimeBox.setAlignment(Pos.CENTER); // Centers the label within the box.
        bestTimeBox.getStyleClass().add("info-box"); // Applies a CSS style class for the box.
        bestTimeBox.setMaxSize(25, 25); // Sets the maximum size of the box.
        bestTimeBox.setId(difficulty); // Sets an ID for styling or event handling.

        // Add the button and best time box to the difficulty container.
        difficultyBox.getChildren().addAll(difficultyButton, bestTimeBox);

        return difficultyBox; // Return the configured difficulty box.
    }

    /**
     * Initializes the "Mute SFX" button in the menu.
     * <p>
     * This method creates and styles a button that allows the player to toggle sound effects on or off.
     * The button's appearance updates dynamically based on whether sound effects are currently muted.
     * Clicking the button triggers the {@link #toggleMute()} method to switch the mute state.
     */
    private void initializeMuteSfxButton()
    {
        // Create the "Mute SFX" button and assign its text.
        setMuteSfxButton(new Button("Mute SFX"));

        // Apply CSS styles to the button.
        getMuteSfxButton().getStyleClass().add("button"); // General button styling.
        getMuteSfxButton().getStyleClass().add("mute-button"); // Additional styling for mute buttons.

        // Update the button's appearance based on the mute state.
        if (isMuted())
        {
            getMuteSfxButton().getStyleClass().add("selected"); // Add "selected" style if muted.
        } else
        {
            getMuteSfxButton().getStyleClass().remove("selected"); // Remove "selected" style if not muted.
        }

        // Set the action to toggle the mute state when the button is clicked.
        getMuteSfxButton().setOnAction(event -> toggleMute());
    }

    /**
     * Initializes the "Choose Style" button in the menu.
     * <p>
     * This method creates and styles a button that allows the player to toggle between different visual styles
     * (e.g., "retro" or "modern"). The button's label reflects the current style in uppercase.
     * Clicking the button triggers the {@link #toggleStyle()} method to switch the style.
     */
    private void initializeChooseStyleButton()
    {
        // Create the "Choose Style" button and set its label to the current style in uppercase.
        setChooseStyleButton(new Button(getStyle().toUpperCase()));

        // Apply CSS styles to the button.
        getChooseStyleButton().getStyleClass().add("button"); // General button styling.
        getChooseStyleButton().getStyleClass().add("style-button"); // Additional styling for style buttons.

        // Set the action to toggle the style when the button is clicked.
        getChooseStyleButton().setOnAction(event -> toggleStyle());
    }

    /**
     * Initializes the "Leaderboard" button in the menu.
     * <p>
     * This method creates and styles a button that allows the player to view the global leaderboard.
     * Clicking the button triggers the {@link #showLeaderboardWindow()} method to open the leaderboard window.
     */
    private void initializeLeaderboardButton()
    {
        // Create the "Leaderboard" button with its label.
        setLeaderboardButton(new Button("Leaderboard"));

        // Set the action to display the leaderboard window when the button is clicked.
        getLeaderboardButton().setOnAction(event -> showLeaderboardWindow());

        // Apply a CSS style class to the button.
        getLeaderboardButton().getStyleClass().add("button");
    }

    /**
     * Displays the leaderboard window.
     * <p>
     * This method creates a new instance of {@link LeaderboardController}, passing the current style,
     * and starts it to show the global leaderboard in a new window.
     */
    private void showLeaderboardWindow()
    {
        new LeaderboardController(getStyle()).start(); // Creates and starts the leaderboard window with the current style.
    }

    /**
     * Toggles the visual style of the menu between "retro" and "modern".
     * <p>
     * This method switches the current style, updates the "Choose Style" button label to reflect the new style,
     * stops the background music, and restarts the menu with the updated settings.
     */
    private void toggleStyle()
    {
        // Switches the current style between "retro" and "modern".
        if ("retro".equals(getStyle()))
        {
            setStyle("modern"); // Changes style to "modern".
        } else
        {
            setStyle("retro"); // Changes style to "retro".
        }

        // Updates the "Choose Style" button label to reflect the current style.
        getChooseStyleButton().setText(getStyle().toUpperCase());

        // Stops the background music for the menu.
        stopBackgroundMusic();

        // Restarts the menu with the updated style, username, and mute settings.
        new MenuController(getUsername(), getStyle(), isMuted()).start(getStage());
    }

    /**
     * Toggles the mute state for sound effects and background music.
     * <p>
     * This method switches the current mute state, updates the "Mute SFX" button's appearance to reflect the new state,
     * stops or starts the background music based on the mute status, and ensures consistent UI feedback.
     */
    private void toggleMute()
    {
        // Switches the mute state (true -> false, false -> true).
        setMuted(!isMuted());

        // If muted, stop background music and update the button style.
        if (isMuted())
        {
            stopBackgroundMusic(); // Stops the background music.
            getMuteSfxButton().getStyleClass().add("selected"); // Adds the "selected" style to indicate mute.
        }
        // If not muted, play background music and update the button style.
        else
        {
            playBackgroundMusic("menu-music", getStyle()); // Starts playing the background music.
            getMuteSfxButton().getStyleClass().remove("selected"); // Removes the "selected" style.
        }
    }

    /**
     * Initializes the "Reset Local Best Times" button in the menu.
     * <p>
     * This method creates and styles a button that allows the player to reset their local best times.
     * Clicking the button triggers the {@link #resetBestTimes()} method to clear the stored best times.
     */
    private void initializeResetLocalBestTimesButton()
    {
        // Create the "Reset Local Best Times" button with its label.
        setResetLocalBestTimesButton(new Button("Reset Local Best Times"));

        // Apply CSS styles to the button.
        getResetLocalBestTimesButton().getStyleClass().add("button"); // General button styling.
        getResetLocalBestTimesButton().getStyleClass().add("reset-button"); // Specific styling for the reset button.

        // Set the button's maximum size.
        getResetLocalBestTimesButton().setMaxSize(getMaxButtonWidth(), getMaxButtonHeight());

        // Set the action to reset local best times when the button is clicked.
        getResetLocalBestTimesButton().setOnAction(event -> resetBestTimes());
    }

    /**
     * Loads the best time for a specific difficulty level from the user's data.
     * <p>
     * This method retrieves the player's best time for the given difficulty by accessing the saved user data.
     * If the difficulty is invalid, it returns {@link Integer#MAX_VALUE} as a fallback.
     *
     * @param difficulty The difficulty level for which to retrieve the best time
     *                   (e.g., "beginner", "advanced", "pro").
     * @return The best time for the specified difficulty, or {@link Integer#MAX_VALUE} if the difficulty is invalid.
     */
    private int loadBestTime(String difficulty)
    {
        // Load the saved user data.
        UserData userData = UserDataManager.loadUserData();

        // Return the best time for the specified difficulty, or a fallback value for invalid difficulties.
        return switch (difficulty)
        {
            case "beginner" -> userData.getBeginnerBestTime(); // Best time for beginner.
            case "advanced" -> userData.getAdvancedBestTime(); // Best time for advanced.
            case "pro" -> userData.getProBestTime(); // Best time for pro.
            default -> Integer.MAX_VALUE; // Fallback for invalid difficulty.
        };
    }

    /**
     * Resets the player's local best times.
     * <p>
     * This method clears all saved best times by creating a new {@link UserData} object with only the username retained.
     * The updated data is saved, and the menu is restarted to reflect the changes.
     * Background music is stopped before restarting the menu.
     */
    private void resetBestTimes()
    {
        // Save new user data with only the username retained, resetting all best times.
        UserDataManager.saveUserData(new UserData(UserDataManager.loadUserData().getUsername()));

        // Stop the background music before restarting the menu.
        stopBackgroundMusic();

        // Restart the menu with the current settings (username, style, and mute state).
        new MenuController(getUsername(), getStyle(), isMuted()).start(getStage());
    }

    /**
     * Formats the username to a fixed width for consistent display.
     * <p>
     * This method ensures the username is aligned to a width of 16 characters by padding it with spaces
     * if necessary. This is useful for maintaining a uniform layout in the UI.
     *
     * @param username The username to format.
     * @return The formatted username as a string padded to 16 characters.
     */
    private String formatUsername(String username)
    {
        return String.format("%16s", username); // Pads the username with spaces to a fixed width of 16 characters.
    }

    /**
     * Formats the best time for display in the menu.
     * <p>
     * This method converts the best time into a three-digit string. If the best time equals
     * {@link Integer#MAX_VALUE}, it indicates no valid time is set and returns a placeholder ("---").
     *
     * @param bestTime The best time to format.
     * @return A string representation of the best time, or "---" if no valid time is available.
     */
    private String formatBestTime(int bestTime)
    {
        if (bestTime == Integer.MAX_VALUE)
        {
            return "----"; // Placeholder for unset or invalid best times.
        }
        return String.format("%04d", bestTime); // Formats the time as a three-digit number.
    }

    /**
     * Gets the maximum width for HBox containers in the menu.
     *
     * @return The maximum width for HBox containers.
     */
    public static int getMaxHBoxWidth()
    {
        return maxHBoxWidth;
    }

    /**
     * Gets the maximum height for HBox containers in the menu.
     *
     * @return The maximum height for HBox containers.
     */
    public static int getMaxHBoxHeight()
    {
        return maxHBoxHeight;
    }

    /**
     * Gets the maximum width for buttons in the menu.
     *
     * @return The maximum width for buttons.
     */
    public static int getMaxButtonWidth()
    {
        return maxButtonWidth;
    }

    /**
     * Gets the maximum height for buttons in the menu.
     *
     * @return The maximum height for buttons.
     */
    public static int getMaxButtonHeight()
    {
        return maxButtonHeight;
    }

    /**
     * Gets the maximum width for VBox containers in the menu.
     *
     * @return The maximum width for VBox containers.
     */
    public static int getMaxVBoxWidth()
    {
        return maxVBoxWidth;
    }

    /**
     * Gets the maximum height for VBox containers in the menu.
     *
     * @return The maximum height for VBox containers.
     */
    public static int getMaxVBoxHeight()
    {
        return maxVBoxHeight;
    }

    /**
     * Gets the current stage of the menu.
     *
     * @return The {@link Stage} instance used for the menu.
     */
    public Stage getStage()
    {
        return stage;
    }

    /**
     * Sets the current stage of the menu.
     *
     * @param stage The {@link Stage} instance to set for the menu.
     */
    public void setStage(Stage stage)
    {
        this.stage = stage;
    }

    /**
     * Gets the root container for the menu layout.
     *
     * @return The {@link VBox} root container.
     */
    public VBox getRoot()
    {
        return root;
    }

    /**
     * Gets the current scene of the menu.
     *
     * @return The {@link Scene} instance representing the menu.
     */
    public Scene getMenuScene()
    {
        return menuScene;
    }

    /**
     * Sets the current scene of the menu.
     *
     * @param menuScene The {@link Scene} instance to set for the menu.
     */
    public void setMenuScene(Scene menuScene)
    {
        this.menuScene = menuScene;
    }

    /**
     * Gets the title label of the menu.
     *
     * @return The {@link Label} instance representing the title of the menu.
     */
    public Label getTitleLabel()
    {
        return titleLabel;
    }

    /**
     * Sets the title label of the menu.
     *
     * @param titleLabel The {@link Label} instance to set as the menu title.
     */
    public void setTitleLabel(Label titleLabel)
    {
        this.titleLabel = titleLabel;
    }

    /**
     * Gets the choice text displayed in the menu.
     *
     * @return The {@link Text} instance representing the choice text.
     */
    public Text getChoiceText()
    {
        return choiceText;
    }

    /**
     * Sets the choice text displayed in the menu.
     *
     * @param choiceText The {@link Text} instance to set as the choice text.
     */
    public void setChoiceText(Text choiceText)
    {
        this.choiceText = choiceText;
    }

    /**
     * Gets the container for game mode selection.
     *
     * @return The {@link VBox} instance containing the game mode options.
     */
    public VBox getChooseGameModeBox()
    {
        return chooseGameModeBox;
    }

    /**
     * Sets the container for game mode selection.
     *
     * @param chooseGameModeBox The {@link VBox} instance to set for game mode options.
     */
    public void setChooseGameModeBox(VBox chooseGameModeBox)
    {
        this.chooseGameModeBox = chooseGameModeBox;
    }

    /**
     * Gets the difficulty selection box.
     *
     * @return The {@link HBox} instance containing the difficulty buttons.
     */
    public HBox getDifficultyBox()
    {
        return difficultyBox;
    }

    /**
     * Sets the difficulty selection box.
     *
     * @param difficultyBox The {@link HBox} instance to set for the difficulty buttons.
     */
    public void setDifficultyBox(HBox difficultyBox)
    {
        this.difficultyBox = difficultyBox;
    }

    /**
     * Checks if the sound effects and background music are muted.
     *
     * @return {@code true} if muted, {@code false} otherwise.
     */
    public boolean isMuted()
    {
        return muted;
    }

    /**
     * Sets the mute state for sound effects and background music.
     *
     * @param muted {@code true} to mute, {@code false} to unmute.
     */
    public void setMuted(boolean muted)
    {
        this.muted = muted;
    }

    /**
     * Gets the "Mute SFX" button.
     *
     * @return The {@link Button} instance representing the "Mute SFX" button.
     */
    public Button getMuteSfxButton()
    {
        return muteSfxButton;
    }

    /**
     * Sets the "Mute SFX" button.
     *
     * @param muteSfxButton The {@link Button} instance to set as the "Mute SFX" button.
     */
    public void setMuteSfxButton(Button muteSfxButton)
    {
        this.muteSfxButton = muteSfxButton;
    }

    /**
     * Gets the "Reset Local Best Times" button.
     *
     * @return The {@link Button} instance representing the "Reset Local Best Times" button.
     */
    public Button getResetLocalBestTimesButton()
    {
        return resetLocalBestTimesButton;
    }

    /**
     * Sets the "Reset Local Best Times" button.
     *
     * @param resetLocalBestTimesButton The {@link Button} instance to set as the "Reset Local Best Times" button.
     */
    public void setResetLocalBestTimesButton(Button resetLocalBestTimesButton)
    {
        this.resetLocalBestTimesButton = resetLocalBestTimesButton;
    }

    /**
     * Gets the current visual style of the menu (e.g., "retro" or "modern").
     *
     * @return The current visual style as a {@link String}.
     */
    public String getStyle()
    {
        return style;
    }

    /**
     * Sets the visual style of the menu.
     *
     * @param style The visual style to set (e.g., "retro" or "modern").
     */
    public void setStyle(String style)
    {
        this.style = style;
    }

    /**
     * Gets the "Choose Style" button.
     *
     * @return The {@link Button} instance representing the "Choose Style" button.
     */
    public Button getChooseStyleButton()
    {
        return chooseStyleButton;
    }

    /**
     * Sets the "Choose Style" button.
     *
     * @param chooseStyleButton The {@link Button} instance to set as the "Choose Style" button.
     */
    public void setChooseStyleButton(Button chooseStyleButton)
    {
        this.chooseStyleButton = chooseStyleButton;
    }

    /**
     * Gets the container for control buttons in the menu (e.g., "Mute SFX", "Choose Style").
     *
     * @return The {@link HBox} instance representing the control buttons box.
     */
    public HBox getControlButtonsBox()
    {
        return controlButtonsBox;
    }

    /**
     * Sets the container for control buttons in the menu.
     *
     * @param controlButtonsBox The {@link HBox} instance to set as the control buttons box.
     */
    public void setControlButtonsBox(HBox controlButtonsBox)
    {
        this.controlButtonsBox = controlButtonsBox;
    }

    /**
     * Gets the "Leaderboard" button.
     *
     * @return The {@link Button} instance representing the "Leaderboard" button.
     */
    public Button getLeaderboardButton()
    {
        return leaderboardButton;
    }

    /**
     * Sets the "Leaderboard" button.
     *
     * @param leaderboardButton The {@link Button} instance to set as the "Leaderboard" button.
     */
    public void setLeaderboardButton(Button leaderboardButton)
    {
        this.leaderboardButton = leaderboardButton;
    }

    /**
     * Gets the footer box of the menu layout.
     *
     * @return The {@link HBox} instance representing the footer box.
     */
    public HBox getFooterBox()
    {
        return footerBox;
    }

    /**
     * Sets the footer box of the menu layout.
     *
     * @param footerBox The {@link HBox} instance to set as the footer box.
     */
    public void setFooterBox(HBox footerBox)
    {
        this.footerBox = footerBox;
    }

    /**
     * Gets the header box of the menu layout.
     *
     * @return The {@link HBox} instance representing the header box.
     */
    public HBox getHeaderBox()
    {
        return headerBox;
    }

    /**
     * Sets the header box of the menu layout.
     *
     * @param headerBox The {@link HBox} instance to set as the header box.
     */
    public void setHeaderBox(HBox headerBox)
    {
        this.headerBox = headerBox;
    }

    /**
     * Gets the label that displays the username in the menu.
     *
     * @return The {@link Label} instance representing the username.
     */
    public Label getUsernameLabel()
    {
        return usernameLabel;
    }

    /**
     * Sets the label that displays the username in the menu.
     *
     * @param usernameLabel The {@link Label} instance to set as the username label.
     */
    public void setUsernameLabel(Label usernameLabel)
    {
        this.usernameLabel = usernameLabel;
    }

    /**
     * Gets the username of the current player.
     *
     * @return The username as a {@link String}.
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Sets the username of the current player.
     *
     * @param username The username to set.
     */
    public void setUsername(String username)
    {
        this.username = username;
    }

    /**
     * Gets the container for the header elements in the menu.
     *
     * @return The {@link VBox} instance representing the header container.
     */
    public VBox getHeaderContainer()
    {
        return headerContainer;
    }

    /**
     * Sets the container for the header elements in the menu.
     *
     * @param headerContainer The {@link VBox} instance to set as the header container.
     */
    public void setHeaderContainer(VBox headerContainer)
    {
        this.headerContainer = headerContainer;
    }

    /**
     * Gets the box containing the username label in the header.
     *
     * @return The {@link HBox} instance representing the username box.
     */
    public HBox getUsernameBox()
    {
        return usernameBox;
    }

    /**
     * Sets the box containing the username label in the header.
     *
     * @param usernameBox The {@link HBox} instance to set as the username box.
     */
    public void setUsernameBox(HBox usernameBox)
    {
        this.usernameBox = usernameBox;
    }
}
