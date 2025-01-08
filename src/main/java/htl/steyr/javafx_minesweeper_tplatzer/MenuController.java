package htl.steyr.javafx_minesweeper_tplatzer;

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

public class MenuController extends Controller
{
    private static final int maxHBoxWidth = 1000;
    private static final int maxHBoxHeight = 700;
    private static final int maxVBoxWidth = 1000;
    private static final int maxVBoxHeight = 200;
    private static final int maxButtonWidth = 200;
    private static final int maxButtonHeight = 100;
    private Stage stage;
    private Scene menuScene;
    private final VBox root = new VBox();
    private VBox headerContainer;
    private HBox headerBox;
    private Label titleLabel;
    private HBox usernameBox;
    private Label usernameLabel;
    private Text choiceText;
    private HBox difficultyBox;
    private VBox chooseGameModeBox;
    private HBox controlButtonsBox;
    private Button chooseStyleButton;
    private Button leaderboardButton;
    private Button muteSfxButton;
    private HBox footerBox;
    private Button resetLocalBestTimesButton;
    private String style;
    private boolean muted;
    private String username;

    public MenuController(String username, String style, boolean muted)
    {
        setUsername(username);
        setStyle(style);
        setMuted(muted);
    }

    public void start(Stage stage)
    {
        setStage(stage);
        initializeUserElements();
        if (!isMuted()) playBackgroundMusic("menu-music", getStyle());

        initializeStage(getStage(), getStyle());
        initializeWindow();
        getStage().show();
    }

    private void initializeWindow()
    {
        getRoot().setSpacing(30);
        getRoot().setAlignment(Pos.CENTER);
        getRoot().setMinSize(MenuController.getMaxHBoxWidth(), MenuController.getMaxHBoxHeight());
        getRoot().setMaxSize(MenuController.getMaxHBoxWidth(), MenuController.getMaxHBoxHeight());
        getRoot().prefWidthProperty().bind(getStage().widthProperty());
        getRoot().prefHeightProperty().bind(getStage().heightProperty());
        getRoot().getChildren().addAll(
                getHeaderBox(),
                new Region(),
                getChooseGameModeBox(),
                getFooterBox());
        getRoot().getStyleClass().add("root-container");
        getRoot().getStylesheets().addAll(
                Objects.requireNonNull(getClass().getResource("/" + getStyle() + "/style/style.css")).toExternalForm(),
                Objects.requireNonNull(getClass().getResource("/" + getStyle() + "/style/menuStyle.css")).toExternalForm());

        setMenuScene(new Scene(getRoot()));
        switchScene(getStage(), getMenuScene(), "Menu", 500, 1000);
    }

    private void startGame(String difficulty)
    {
        stopBackgroundMusic();
        new GameController(getUsername(), difficulty, getStyle(), isMuted()).start(getStage());
    }

    private void initializeUserElements()
    {
        initializeHeaderBox();
        initializeChooseGameModeBox();
        initializeFooterBox();
    }

    private void initializeHeaderBox()
    {
        setHeaderContainer(new VBox());
        getHeaderContainer().setAlignment(Pos.TOP_RIGHT);
        getHeaderContainer().prefWidthProperty().bind(getRoot().widthProperty());
        getHeaderContainer().setSpacing(5);

        setUsernameLabel(new Label(formatUsername(getUsername())));
        getUsernameLabel().setId("usernameLabel");
        getUsernameLabel().setAlignment(Pos.TOP_RIGHT);

        getUsernameLabel().setOnMouseClicked(event -> changeUserName());

        setUsernameBox(new HBox(getUsernameLabel()));
        getUsernameBox().setAlignment(Pos.TOP_RIGHT);
        getUsernameBox().prefWidthProperty().bind(getHeaderContainer().widthProperty());

        setHeaderBox(new HBox());
        getHeaderBox().setSpacing(10);
        getHeaderBox().setAlignment(Pos.CENTER);
        getHeaderBox().prefWidthProperty().bind(getRoot().widthProperty());

        setTitleLabel(new Label("Bomb-Disposal-Simulator"));
        getTitleLabel().setId("titleText");
        getTitleLabel().setAlignment(Pos.CENTER);

        getHeaderBox().getChildren().add(getTitleLabel());

        getHeaderContainer().getChildren().addAll(getUsernameBox(), getHeaderBox());

        getRoot().getChildren().addFirst(getHeaderContainer());

    }

    private void changeUserName()
    {
        stopBackgroundMusic();
        new UsernameMenuController(getUsername(), getStyle(), isMuted()).start(getStage());
    }

    private void initializeChooseGameModeBox()
    {
        setChooseGameModeBox(new VBox());

        getChooseGameModeBox().setSpacing(10);
        getChooseGameModeBox().setAlignment(Pos.CENTER);
        getChooseGameModeBox().setMinSize(MenuController.getMaxVBoxWidth(), MenuController.getMaxVBoxHeight());
        getChooseGameModeBox().setMaxSize(MenuController.getMaxVBoxWidth(), MenuController.getMaxVBoxHeight());

        setChoiceText(initializeText("choiceText", "Choose-a-Gamemode"));
        initializeDifficultyBoxes();
        initializeControlButtonsBox();

        getChooseGameModeBox().getChildren().addAll(
                getChoiceText(),
                getDifficultyBox(),
                getControlButtonsBox());
    }

    private void initializeControlButtonsBox()
    {
        initializeMuteSfxButton();
        initializeChooseStyleButton();

        setControlButtonsBox(new HBox());
        getControlButtonsBox().setSpacing(10);
        getControlButtonsBox().setAlignment(Pos.CENTER);

        getControlButtonsBox().getChildren().addAll(getMuteSfxButton(), getChooseStyleButton());
    }

    private void initializeDifficultyBoxes()
    {
        setDifficultyBox(new HBox());
        getDifficultyBox().setAlignment(Pos.CENTER);
        getDifficultyBox().setSpacing(10);
        getDifficultyBox().getStyleClass().add("box");
        getDifficultyBox().setId("difficulty-button-box");

        getDifficultyBox().getChildren().addAll(
                createDifficultyBox("beginner", loadBestTime("beginner")),
                createDifficultyBox("advanced", loadBestTime("advanced")),
                createDifficultyBox("pro", loadBestTime("pro")));
    }

    private void initializeFooterBox()
    {
        setFooterBox(new HBox());
        getFooterBox().setSpacing(10);
        getFooterBox().setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        initializeResetLocalBestTimesButton();
        initializeLeaderboardButton();

        getResetLocalBestTimesButton().setMaxSize(getMaxButtonWidth(), getMaxButtonHeight());
        getFooterBox().getChildren().addAll(getResetLocalBestTimesButton(), spacer, getLeaderboardButton());

    }

    private VBox createDifficultyBox(String difficulty, int bestTime)
    {
        VBox difficultyBox = new VBox();
        difficultyBox.setSpacing(10);
        difficultyBox.setAlignment(Pos.CENTER);

        Button difficultyButton = new Button(difficulty.toUpperCase());
        difficultyButton.setMaxSize(getMaxButtonWidth(), getMaxButtonHeight());
        difficultyButton.getStyleClass().add("button");
        difficultyButton.setId(difficulty);
        difficultyButton.setFocusTraversable(false);
        difficultyButton.setOnAction(event -> startGame(difficulty));
        HBox.setHgrow(difficultyButton, Priority.ALWAYS);
        difficultyButton.prefWidthProperty().bind(getDifficultyBox().widthProperty().divide(4).subtract(20));
        difficultyButton.prefHeightProperty().bind(getDifficultyBox().heightProperty().multiply(0.3));

        Label bestTimeLabel = new Label(formatBestTime(bestTime));
        bestTimeLabel.getStyleClass().add("info-label");

        VBox bestTimeBox = new VBox(bestTimeLabel);
        bestTimeBox.setAlignment(Pos.CENTER);
        bestTimeBox.getStyleClass().add("info-box");
        bestTimeBox.setMaxSize(25, 25);
        bestTimeBox.setId(difficulty);

        difficultyBox.getChildren().addAll(difficultyButton, bestTimeBox);

        return difficultyBox;
    }

    private void initializeMuteSfxButton()
    {
        setMuteSfxButton(new Button("Mute SFX"));
        getMuteSfxButton().getStyleClass().add("button");
        getMuteSfxButton().getStyleClass().add("mute-button");

        if (isMuted()) getMuteSfxButton().getStyleClass().add("selected");
        else getMuteSfxButton().getStyleClass().remove("selected");

        getMuteSfxButton().setOnAction(event -> toggleMute());
    }

    private void initializeChooseStyleButton()
    {
        setChooseStyleButton(new Button(getStyle().toUpperCase()));
        getChooseStyleButton().getStyleClass().add("button");
        getChooseStyleButton().getStyleClass().add("style-button");

        getChooseStyleButton().setOnAction(event -> toggleStyle());
    }

    private void initializeLeaderboardButton()
    {
        setLeaderboardButton(new Button("Leaderboard"));
        getLeaderboardButton().setOnAction(event -> showLeaderboardWindow());
        getLeaderboardButton().getStyleClass().add("button");
    }

    private void showLeaderboardWindow()
    {
        new LeaderboardController(getStyle()).start();
    }

    private void toggleStyle()
    {
        if ("retro".equals(getStyle()))
        {
            setStyle("modern");
        } else
        {
            setStyle("retro");
        }

        getChooseStyleButton().setText(getStyle().toUpperCase());

        stopBackgroundMusic();

        new MenuController(getUsername(), getStyle(), isMuted()).start(getStage());
    }

    private void toggleMute()
    {
        setMuted(!isMuted());
        if (isMuted())
        {
            stopBackgroundMusic();
            getMuteSfxButton().getStyleClass().add("selected");
        } else
        {
            playBackgroundMusic("menu-music", getStyle());
            getMuteSfxButton().getStyleClass().remove("selected");
        }
    }

    private void initializeResetLocalBestTimesButton()
    {
        setResetLocalBestTimesButton(new Button("Reset Local Best Times"));
        getResetLocalBestTimesButton().getStyleClass().add("button");
        getResetLocalBestTimesButton().getStyleClass().add("reset-button");
        getResetLocalBestTimesButton().setMaxSize(getMaxButtonWidth(), getMaxButtonHeight());
        getResetLocalBestTimesButton().setOnAction(event -> resetBestTimes());
    }

    private int loadBestTime(String difficulty)
    {
        UserData userData = UserDataManager.loadUserData();

        return switch (difficulty)
        {
            case "beginner" -> userData.getBeginnerBestTime();
            case "advanced" -> userData.getAdvancedBestTime();
            case "pro" -> userData.getProBestTime();
            default -> Integer.MAX_VALUE;
        };
    }

    private void resetBestTimes()
    {
        UserDataManager.saveUserData(new UserData(UserDataManager.loadUserData().getUsername()));

        stopBackgroundMusic();
        new MenuController(getUsername(), getStyle(), isMuted()).start(getStage());
    }

    private String formatUsername(String username)
    {
        return String.format("%16s", username);
    }

    private String formatBestTime(int bestTime)
    {
        if (bestTime == Integer.MAX_VALUE)
        {
            return "---";
        }
        return String.format("%03d", bestTime);
    }

    public static int getMaxHBoxWidth()
    {
        return maxHBoxWidth;
    }

    public static int getMaxHBoxHeight()
    {
        return maxHBoxHeight;
    }

    public static int getMaxButtonWidth()
    {
        return maxButtonWidth;
    }

    public static int getMaxButtonHeight()
    {
        return maxButtonHeight;
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

    public void setStage(Stage stage)
    {
        this.stage = stage;
    }

    public VBox getRoot()
    {
        return root;
    }

    public Scene getMenuScene()
    {
        return menuScene;
    }

    public void setMenuScene(Scene menuScene)
    {
        this.menuScene = menuScene;
    }

    public Label getTitleLabel()
    {
        return titleLabel;
    }

    public void setTitleLabel(Label titleLabel)
    {
        this.titleLabel = titleLabel;
    }

    public Text getChoiceText()
    {
        return choiceText;
    }

    public void setChoiceText(Text choiceText)
    {
        this.choiceText = choiceText;
    }

    public VBox getChooseGameModeBox()
    {
        return chooseGameModeBox;
    }

    public void setChooseGameModeBox(VBox chooseGameModeBox)
    {
        this.chooseGameModeBox = chooseGameModeBox;
    }

    public HBox getDifficultyBox()
    {
        return difficultyBox;
    }

    public void setDifficultyBox(HBox difficultyBox)
    {
        this.difficultyBox = difficultyBox;
    }

    public boolean isMuted()
    {
        return muted;
    }

    public void setMuted(boolean muted)
    {
        this.muted = muted;
    }

    public Button getMuteSfxButton()
    {
        return muteSfxButton;
    }

    public void setMuteSfxButton(Button muteSfxButton)
    {
        this.muteSfxButton = muteSfxButton;
    }

    public Button getResetLocalBestTimesButton()
    {
        return resetLocalBestTimesButton;
    }

    public void setResetLocalBestTimesButton(Button resetLocalBestTimesButton)
    {
        this.resetLocalBestTimesButton = resetLocalBestTimesButton;
    }

    public String getStyle()
    {
        return style;
    }

    public void setStyle(String style)
    {
        this.style = style;
    }

    public Button getChooseStyleButton()
    {
        return chooseStyleButton;
    }

    public void setChooseStyleButton(Button chooseStyleButton)
    {
        this.chooseStyleButton = chooseStyleButton;
    }

    public HBox getControlButtonsBox()
    {
        return controlButtonsBox;
    }

    public void setControlButtonsBox(HBox controlButtonsBox)
    {
        this.controlButtonsBox = controlButtonsBox;
    }

    public Button getLeaderboardButton()
    {
        return leaderboardButton;
    }

    public void setLeaderboardButton(Button leaderboardButton)
    {
        this.leaderboardButton = leaderboardButton;
    }

    public HBox getFooterBox()
    {
        return footerBox;
    }

    public void setFooterBox(HBox footerBox)
    {
        this.footerBox = footerBox;
    }

    public HBox getHeaderBox()
    {
        return headerBox;
    }

    public void setHeaderBox(HBox headerBox)
    {
        this.headerBox = headerBox;
    }

    public Label getUsernameLabel()
    {
        return usernameLabel;
    }

    public void setUsernameLabel(Label usernameLabel)
    {
        this.usernameLabel = usernameLabel;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public VBox getHeaderContainer()
    {
        return headerContainer;
    }

    public void setHeaderContainer(VBox headerContainer)
    {
        this.headerContainer = headerContainer;
    }

    public HBox getUsernameBox()
    {
        return usernameBox;
    }

    public void setUsernameBox(HBox usernameBox)
    {
        this.usernameBox = usernameBox;
    }
}
