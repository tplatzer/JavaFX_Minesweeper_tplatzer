package htl.steyr.javafx_minesweeper_tplatzer;


import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class UsernameController extends Controller
{
    private static final int maxVBoxWidth = 1050;
    private static final int maxVBoxHeight = 525;
    private Stage stage;
    private Scene usernameScene;
    private final VBox root = new VBox();
    private String style;
    private TextField usernameTextField;
    private HBox buttonBox;
    private Button saveUsernameButton;
    private Button returnButton;
    private String username;
    private boolean muted;

    public UsernameController(String username, String style, boolean muted)
    {
        setUsername(username);
        setStyle(style);
        setMuted(muted);
    }

    public void start(Stage stage)
    {
        setStage(stage);
        initializeUserElements();
        if (!isMuted()) playBackgroundMusic("username-music", getStyle());

        initializeStage(getStage(), getStyle());
        initializeWindow();
    }

    private void initializeUserElements()
    {
        initializeUsernameTextField();
        initializeSaveUsernameButton();
        initializeReturnButton();
        initializeButtonBox();
    }

    private void initializeWindow()
    {
        getRoot().setSpacing(30);
        getRoot().setAlignment(Pos.CENTER);
        getRoot().setMinSize(UsernameController.getMaxVBoxWidth(), UsernameController.getMaxVBoxHeight());
        getRoot().setMaxSize(UsernameController.getMaxVBoxWidth(), UsernameController.getMaxVBoxHeight());
        getRoot().prefWidthProperty().bind(getStage().widthProperty());
        getRoot().prefHeightProperty().bind(getStage().heightProperty());
        getRoot().getChildren().addAll(
                getUsernameTextField(),
                getButtonBox());
        getRoot().getStyleClass().add("root-container");
        getRoot().getStylesheets().addAll(
                Objects.requireNonNull(getClass().getResource("/" + getStyle() + "/style/style.css")).toExternalForm(),
                Objects.requireNonNull(getClass().getResource("/" + getStyle() + "/style/usernameStyle.css")).toExternalForm());

        setUsernameScene(new Scene(getRoot()));
        switchScene(getStage(), getUsernameScene(), "Username", 200, 600);
    }

    private void initializeUsernameTextField()
    {
        setUsernameTextField(new TextField(getUsername()));
        getUsernameTextField().setFocusTraversable(false);
        getUsernameTextField().setPromptText("Enter your username (1-16 characters)");
        getUsernameTextField().setPrefWidth(300);
        getUsernameTextField().setOnKeyReleased(event -> validateInput());
    }

    private void initializeSaveUsernameButton()
    {
        setSaveUsernameButton(new Button("Save Username"));
        getSaveUsernameButton().setFocusTraversable(false);

        getSaveUsernameButton().setDisable(true);
        getSaveUsernameButton().setOnAction(event -> saveUsername());
    }

    private void initializeReturnButton()
    {
        setReturnButton(new Button("Return"));
        getReturnButton().setFocusTraversable(false);
        getReturnButton().setOnAction(event -> returnToMenu());
    }

    private void initializeButtonBox()
    {
        setButtonBox(new HBox(10));
        getButtonBox().setAlignment(Pos.CENTER);
        getButtonBox().getChildren().addAll(getSaveUsernameButton(), getReturnButton());
    }

    private void validateInput()
    {
        String input = getUsernameTextField().getText().trim();
        getSaveUsernameButton().setDisable(input.isEmpty() || input.length() > 16 || input.contains(" "));
    }

    private void saveUsername()
    {
        setUsername(getUsernameTextField().getText().trim());

        UserDataManager.saveUserData(UserDataManager.loadUserData().setUsername(getUsername()));

        returnToMenu();
    }

    private void returnToMenu()
    {
        stopBackgroundMusic();
        new MenuController(getUsername(), getStyle(), isMuted()).start(getStage());
    }

    public static int getMaxVBoxWidth()
    {
        return maxVBoxWidth;
    }

    public static int getMaxVBoxHeight()
    {
        return maxVBoxHeight;
    }

    public Stage getStage()
    {
        return stage;
    }

    public VBox getRoot()
    {
        return root;
    }

    public String getStyle()
    {
        return style;
    }

    public void setStyle(String style)
    {
        this.style = style;
    }

    public TextField getUsernameTextField()
    {
        return usernameTextField;
    }

    public void setUsernameTextField(TextField usernameTextField)
    {
        this.usernameTextField = usernameTextField;
    }

    public Button getSaveUsernameButton()
    {
        return saveUsernameButton;
    }

    public void setSaveUsernameButton(Button saveUsernameButton)
    {
        this.saveUsernameButton = saveUsernameButton;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public Scene getUsernameScene()
    {
        return usernameScene;
    }

    public void setUsernameScene(Scene usernameScene)
    {
        this.usernameScene = usernameScene;
    }

    public void setStage(Stage stage)
    {
        this.stage = stage;
    }

    public Button getReturnButton()
    {
        return returnButton;
    }

    public void setReturnButton(Button returnButton)
    {
        this.returnButton = returnButton;
    }

    public HBox getButtonBox()
    {
        return buttonBox;
    }

    public void setButtonBox(HBox buttonBox)
    {
        this.buttonBox = buttonBox;
    }

    public boolean isMuted()
    {
        return muted;
    }

    public void setMuted(boolean muted)
    {
        this.muted = muted;
    }
}
