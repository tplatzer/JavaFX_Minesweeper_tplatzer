package htl.steyr.javafx_minesweeper_tplatzer.controller;

import htl.steyr.javafx_minesweeper_tplatzer.model.UserDataManager;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Controller for the "Username Menu" of the Minesweeper application.
 * <p>
 * This menu allows the user to change their username, ensuring valid input
 * (1-16 characters, no spaces). The updated username is saved in the user data.
 * The menu provides options to return to the main menu or save the new username.
 */
public class UsernameMenuController extends Controller
{
    /**
     * The maximum width of the VBox container in the Username Menu.
     */
    private static final int maxVBoxWidth = 1050;

    /**
     * The maximum height of the VBox container in the Username Menu.
     */
    private static final int maxVBoxHeight = 525;

    /**
     * The {@link Stage} instance representing the window for the Username Menu.
     */
    private Stage stage;

    /**
     * The {@link Scene} instance representing the layout of the Username Menu.
     */
    private Scene usernameScene;

    /**
     * The root container for the layout of the Username Menu.
     * This {@link VBox} organizes all elements vertically.
     */
    private final VBox root = new VBox();

    /**
     * The current visual style of the menu (e.g., "retro" or "modern").
     */
    private String style;

    /**
     * The {@link TextField} for entering the username in the Username Menu.
     */
    private TextField usernameTextField;

    /**
     * The {@link HBox} container for the buttons in the Username Menu.
     * This organizes the buttons horizontally.
     */
    private HBox buttonBox;

    /**
     * The {@link Button} used to save the entered username.
     */
    private Button saveUsernameButton;

    /**
     * The {@link Button} used to return to the main menu.
     */
    private Button returnButton;

    /**
     * The username entered by the user.
     */
    private String username;

    /**
     * Indicates whether sound effects and background music are muted.
     */
    private boolean muted;


    /**
     * Constructs a new {@code UsernameMenuController} with the specified username, style, and mute settings.
     * <p>
     * This constructor initializes the controller with the player's username, the chosen visual style,
     * and whether sound effects are muted.
     *
     * @param username The username of the player.
     * @param style    The visual style of the menu (e.g., "retro", "modern").
     * @param muted    {@code true} if sound effects are muted, {@code false} otherwise.
     */
    public UsernameMenuController(String username, String style, boolean muted)
    {
        setUsername(username); // Sets the player's username.
        setStyle(style);       // Sets the menu's visual style.
        setMuted(muted);       // Sets whether sound effects are muted.
    }

    /**
     * Starts the {@code UsernameMenuController} and initializes the username menu interface.
     * <p>
     * This method sets up the stage and initializes all required UI elements for the username menu.
     * If the sound is not muted, background music specific to the username menu is played.
     * The stage is prepared and the window layout is initialized.
     *
     * @param stage The {@link Stage} instance used to display the username menu.
     */
    public void start(Stage stage)
    {
        setStage(stage); // Sets the stage for this controller.
        initializeUserElements(); // Initializes UI elements for the username menu.

        // Plays background music if not muted.
        if (!isMuted()) playBackgroundMusic("username-menu-music", getStyle());

        initializeStage(getStage(), getStyle()); // Sets up the stage with the chosen style.
        initializeWindow(); // Initializes and configures the window layout.
    }

    /**
     * Initializes all user interface elements for the username menu.
     * <p>
     * This method sets up the text field for entering the username, the save button,
     * the return button, and the container for organizing these buttons.
     */
    private void initializeUserElements()
    {
        initializeUsernameTextField(); // Sets up the text field for entering the username.
        initializeSaveUsernameButton(); // Sets up the button for saving the username.
        initializeReturnButton(); // Sets up the button for returning to the previous menu.
        initializeButtonBox(); // Organizes the buttons in a container.
    }

    /**
     * Initializes and configures the main window layout for the username menu.
     * <p>
     * This method sets up the root container, applies layout properties, adds user interface elements
     * such as the username text field and button box, and loads the appropriate stylesheets.
     * Finally, it sets the scene and switches to the username menu.
     */
    private void initializeWindow()
    {
        getRoot().setSpacing(30); // Adds spacing between elements in the root container.
        getRoot().setAlignment(Pos.CENTER); // Aligns all elements in the center of the root container.
        getRoot().setMinSize(UsernameMenuController.getMaxVBoxWidth(), UsernameMenuController.getMaxVBoxHeight()); // Sets the minimum size.
        getRoot().setMaxSize(UsernameMenuController.getMaxVBoxWidth(), UsernameMenuController.getMaxVBoxHeight()); // Sets the maximum size.
        getRoot().prefWidthProperty().bind(getStage().widthProperty()); // Binds the root's width to the stage's width.
        getRoot().prefHeightProperty().bind(getStage().heightProperty()); // Binds the root's height to the stage's height.

        // Adds the username text field and button box to the root container.
        getRoot().getChildren().addAll(getUsernameTextField(), getButtonBox());

        // Applies CSS styles to the root container.
        getRoot().getStyleClass().add("root-container");
        getRoot().getStylesheets().addAll(Objects.requireNonNull(getClass().getResource("/" + getStyle() + "/style/style.css")).toExternalForm(), Objects.requireNonNull(getClass().getResource("/" + getStyle() + "/style/usernameStyle.css")).toExternalForm());

        // Creates a new scene with the root container and switches to the username menu.
        setUsernameScene(new Scene(getRoot()));
        switchScene(getStage(), getUsernameScene(), "Username Menu", 200, 600);
    }

    /**
     * Initializes the username text field for the username menu.
     * <p>
     * This method sets up the text field where the user can enter their username.
     * It configures placeholder text, input validation, and visual properties such as width.
     */
    private void initializeUsernameTextField()
    {
        setUsernameTextField(new TextField(getUsername())); // Sets the text field with the current username.
        getUsernameTextField().setFocusTraversable(false); // Prevents the text field from gaining focus automatically.
        getUsernameTextField().setPromptText("Enter your username (1-16 characters)"); // Sets placeholder text.
        getUsernameTextField().setPrefWidth(300); // Sets the preferred width of the text field.
        getUsernameTextField().setOnKeyReleased(event -> validateInput()); // Adds input validation on key release.
    }

    /**
     * Initializes the "Save Username" button in the username menu.
     * <p>
     * This method sets up the button for saving the entered username.
     * It configures properties such as focus behavior and enables the button's action
     * to save the username when clicked. Initially, the button is disabled.
     */
    private void initializeSaveUsernameButton()
    {
        setSaveUsernameButton(new Button("Save Username")); // Creates a new button with the label "Save Username".
        getSaveUsernameButton().setFocusTraversable(false); // Prevents the button from gaining focus automatically.

        getSaveUsernameButton().setDisable(true); // Disables the button initially until input validation is satisfied.
        getSaveUsernameButton().setOnAction(event -> saveUsername()); // Sets the action to save the username when clicked.
    }

    /**
     * Initializes the "Return" button in the username menu.
     * <p>
     * This method sets up the button that allows the user to return to the main menu.
     * It configures properties such as focus behavior and enables the button's action
     * to navigate back to the previous menu when clicked.
     */
    private void initializeReturnButton()
    {
        setReturnButton(new Button("Return")); // Creates a new button with the label "Return".
        getReturnButton().setFocusTraversable(false); // Prevents the button from gaining focus automatically.
        getReturnButton().setOnAction(event -> returnToMenu()); // Sets the action to navigate back to the main menu.
    }

    /**
     * Initializes the button box container for the username menu.
     * <p>
     * This method creates a horizontal box layout to organize the "Save Username" and "Return" buttons.
     * It sets spacing between the buttons and aligns them at the center of the container.
     */
    private void initializeButtonBox()
    {
        setButtonBox(new HBox(10)); // Creates a horizontal box layout with 10px spacing between elements.
        getButtonBox().setAlignment(Pos.CENTER); // Aligns the buttons in the center of the container.
        getButtonBox().getChildren().addAll(getSaveUsernameButton(), getReturnButton()); // Adds the "Save Username" and "Return" buttons to the container.
    }

    /**
     * Validates the input in the username text field.
     * <p>
     * This method checks whether the entered username meets the following criteria:
     * <ul>
     *   <li>It is not empty.</li>
     *   <li>Its length does not exceed 16 characters.</li>
     *   <li>It does not contain any spaces.</li>
     * </ul>
     * If the input is invalid, the "Save Username" button is disabled.
     */
    private void validateInput()
    {
        String input = getUsernameTextField().getText().trim(); // Retrieves and trims the text in the username text field.

        // Disables the "Save Username" button if the input is invalid.
        getSaveUsernameButton().setDisable(input.isEmpty() || input.length() > 16 || input.contains(" "));
    }

    /**
     * Saves the entered username and returns to the main menu.
     * <p>
     * This method retrieves the text from the username text field, trims any unnecessary whitespace,
     * and updates the username. The updated username is then saved using the {@code UserDataManager}.
     * After saving, the user is redirected to the main menu.
     */
    private void saveUsername()
    {
        // Updates the username with the trimmed input from the text field.
        setUsername(getUsernameTextField().getText().trim());

        // Saves the updated username to the user's data.
        UserDataManager.saveUserData(UserDataManager.loadUserData().setUsername(getUsername()));

        // Returns to the main menu after saving the username.
        returnToMenu();
    }

    /**
     * Returns to the main menu.
     * <p>
     * This method stops the background music currently playing in the username menu
     * and initializes a new {@code MenuController} to display the main menu.
     */
    private void returnToMenu()
    {
        // Stops the background music playing in the username menu.
        stopBackgroundMusic();

        // Starts the main menu with the current username, style, and mute settings.
        new MenuController(getUsername(), getStyle(), isMuted()).start(getStage());
    }

    /**
     * Gets the maximum width of the VBox layout for the username menu.
     *
     * @return The maximum width as an integer.
     */
    public static int getMaxVBoxWidth()
    {
        return maxVBoxWidth;
    }

    /**
     * Gets the maximum height of the VBox layout for the username menu.
     *
     * @return The maximum height as an integer.
     */
    public static int getMaxVBoxHeight()
    {
        return maxVBoxHeight;
    }

    /**
     * Gets the {@link Stage} used to display the username menu.
     *
     * @return The {@link Stage} instance.
     */
    public Stage getStage()
    {
        return stage;
    }

    /**
     * Gets the root {@link VBox} container of the username menu layout.
     *
     * @return The {@link VBox} instance.
     */
    public VBox getRoot()
    {
        return root;
    }

    /**
     * Gets the visual style currently applied to the username menu.
     *
     * @return The visual style as a {@link String} (e.g., "retro", "modern").
     */
    public String getStyle()
    {
        return style;
    }

    /**
     * Sets the visual style for the username menu.
     *
     * @param style The visual style as a {@link String} (e.g., "retro", "modern").
     */
    public void setStyle(String style)
    {
        this.style = style;
    }

    /**
     * Gets the text field used for entering the username.
     *
     * @return The {@link TextField} instance.
     */
    public TextField getUsernameTextField()
    {
        return usernameTextField;
    }

    /**
     * Sets the text field used for entering the username.
     *
     * @param usernameTextField The {@link TextField} instance to set.
     */
    public void setUsernameTextField(TextField usernameTextField)
    {
        this.usernameTextField = usernameTextField;
    }

    /**
     * Gets the "Save Username" button in the username menu.
     *
     * @return The {@link Button} instance.
     */
    public Button getSaveUsernameButton()
    {
        return saveUsernameButton;
    }

    /**
     * Sets the "Save Username" button in the username menu.
     *
     * @param saveUsernameButton The {@link Button} instance to set.
     */
    public void setSaveUsernameButton(Button saveUsernameButton)
    {
        this.saveUsernameButton = saveUsernameButton;
    }

    /**
     * Gets the username entered or currently set in the username menu.
     *
     * @return The username as a {@link String}.
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Sets the username for the username menu.
     *
     * @param username The username as a {@link String}.
     */
    public void setUsername(String username)
    {
        this.username = username;
    }

    /**
     * Gets the {@link Scene} used for the username menu.
     *
     * @return The {@link Scene} instance representing the username menu.
     */
    public Scene getUsernameScene()
    {
        return usernameScene;
    }

    /**
     * Sets the {@link Scene} used for the username menu.
     *
     * @param usernameScene The {@link Scene} instance to set for the username menu.
     */
    public void setUsernameScene(Scene usernameScene)
    {
        this.usernameScene = usernameScene;
    }

    /**
     * Sets the {@link Stage} used for displaying the username menu.
     *
     * @param stage The {@link Stage} instance to set.
     */
    public void setStage(Stage stage)
    {
        this.stage = stage;
    }

    /**
     * Gets the "Return" button used in the username menu.
     *
     * @return The {@link Button} instance representing the "Return" button.
     */
    public Button getReturnButton()
    {
        return returnButton;
    }

    /**
     * Sets the "Return" button used in the username menu.
     *
     * @param returnButton The {@link Button} instance to set as the "Return" button.
     */
    public void setReturnButton(Button returnButton)
    {
        this.returnButton = returnButton;
    }

    /**
     * Gets the {@link HBox} container used for organizing buttons in the username menu.
     *
     * @return The {@link HBox} instance representing the button box.
     */
    public HBox getButtonBox()
    {
        return buttonBox;
    }

    /**
     * Sets the {@link HBox} container used for organizing buttons in the username menu.
     *
     * @param buttonBox The {@link HBox} instance to set as the button box.
     */
    public void setButtonBox(HBox buttonBox)
    {
        this.buttonBox = buttonBox;
    }

    /**
     * Checks if the sound effects are muted in the username menu.
     *
     * @return {@code true} if sound effects are muted, {@code false} otherwise.
     */
    public boolean isMuted()
    {
        return muted;
    }

    /**
     * Sets whether the sound effects should be muted in the username menu.
     *
     * @param muted {@code true} to mute sound effects, {@code false} to enable them.
     */
    public void setMuted(boolean muted)
    {
        this.muted = muted;
    }
}
