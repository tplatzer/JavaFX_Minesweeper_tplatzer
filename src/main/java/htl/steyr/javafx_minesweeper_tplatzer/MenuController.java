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
    private Text titleText;
    private Text choiceText;
    private HBox difficultyBox;
    private VBox chooseGameModeBox;
    private Button muteSfxButton;
    private Button resetBestTimesButton;
    private boolean muted;

    public MenuController(boolean muted)
    {
        setMuted(muted);
    }

    public void start(Stage stage)
    {
        setStage(stage);
        initializeUserElements();
        if (!isMuted()) playBackgroundMusic("menu-music");

        initializeStage(getStage());
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
                getTitleText(),
                new Region(),
                getChooseGameModeBox());
        getRoot().getStyleClass().add("root-container");
        getRoot().getStylesheets().addAll(
                Objects.requireNonNull(getClass().getResource("/retro/style/style.css")).toExternalForm(),
                Objects.requireNonNull(getClass().getResource("/retro/style/menuStyle.css")).toExternalForm());

        setMenuScene(new Scene(getRoot()));
        switchScene(getStage(), getMenuScene(), "Menu", 400, 800);
    }

    private void startGame(String difficulty)
    {
        stopBackgroundMusic();
        new GameController(difficulty, isMuted()).start(getStage());
    }

    private void initializeUserElements()
    {
        setTitleText(initializeText("titleText", "Bomb-Disposal-Simulator"));
        setChoiceText(initializeText("choiceText", "Choose-a-Gamemode"));
        initializeChooseGameModeBox();

    }

    private void initializeChooseGameModeBox()
    {
        setChooseGameModeBox(new VBox());

        getChooseGameModeBox().setSpacing(10);
        getChooseGameModeBox().setAlignment(Pos.CENTER);
        getChooseGameModeBox().setMinSize(MenuController.getMaxVBoxWidth(), MenuController.getMaxVBoxHeight());
        getChooseGameModeBox().setMaxSize(MenuController.getMaxVBoxWidth(), MenuController.getMaxVBoxHeight());

        initializeDifficultyBoxes();
        initializeMuteSfxButton();
        initializeResetBestTimesButton();

        getChooseGameModeBox().getChildren().addAll(
                getChoiceText(),
                getDifficultyBox(),
                getMuteSfxButton(),
                getResetBestTimesButton());
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

    private void toggleMute()
    {
        setMuted(!isMuted());
        if (isMuted())
        {
            stopBackgroundMusic();
            getMuteSfxButton().getStyleClass().add("selected");
        } else
        {
            playBackgroundMusic("menu-music");
            getMuteSfxButton().getStyleClass().remove("selected");
        }
    }

    private void initializeResetBestTimesButton()
    {
        setResetBestTimesButton(new Button("Reset Best Times"));
        getResetBestTimesButton().getStyleClass().add("button");
        getResetBestTimesButton().getStyleClass().add("reset-button");
        getResetBestTimesButton().setMaxSize(getMaxButtonWidth(), getMaxButtonHeight());
        getResetBestTimesButton().setOnAction(event -> resetBestTimes());
    }

    private int loadBestTime(String difficulty)
    {
        BestTimes bestTimes = BestTimesManager.loadBestTimes();

        return switch (difficulty)
        {
            case "beginner" -> bestTimes.getBeginnerBestTime();
            case "advanced" -> bestTimes.getAdvancedBestTime();
            case "pro" -> bestTimes.getProBestTime();
            default -> Integer.MAX_VALUE;
        };
    }

    private void resetBestTimes()
    {
        BestTimes bestTimes = new BestTimes();
        BestTimesManager.saveBestTimes(bestTimes);

        stopBackgroundMusic();
        new MenuController(isMuted()).start(getStage());
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

    public Text getTitleText()
    {
        return titleText;
    }

    public void setTitleText(Text titleText)
    {
        this.titleText = titleText;
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

    public Button getResetBestTimesButton()
    {
        return resetBestTimesButton;
    }

    public void setResetBestTimesButton(Button resetBestTimesButton)
    {
        this.resetBestTimesButton = resetBestTimesButton;
    }
}
