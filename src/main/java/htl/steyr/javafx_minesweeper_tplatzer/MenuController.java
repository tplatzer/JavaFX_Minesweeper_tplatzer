package htl.steyr.javafx_minesweeper_tplatzer;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * @ToDo Anzeige der besten Zeit für jeden Spielmodus unter dem jeweiligen Spielmodus
 */

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
    private Button beginnerButton;
    private Button advancedButton;
    private Button proButton;
    private HBox difficultyBox;
    private VBox chooseGameModeBox;

    public void start(Stage stage)
    {
        setStage(stage);
        initializeUserElements();
        playBackgroundMusic("menu-music");

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
        getRoot().getChildren().addAll(getTitleText(), getChooseGameModeBox());
        getRoot().getStyleClass().add("root-container");
        getRoot().getStylesheets().addAll(
                Objects.requireNonNull(getClass().getResource("/style/style.css")).toExternalForm(),
                Objects.requireNonNull(getClass().getResource("/style/menuStyle.css")).toExternalForm());

        setMenuScene(new Scene(getRoot()));
        switchScene(getStage(), getMenuScene(), "Menü", 400, 800);
    }

    private void startGame(String difficulty)
    {
        new GameController(difficulty).start(getStage());
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
        getChooseGameModeBox().getChildren().addAll(getChoiceText(), getDifficultyBox());
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

        difficultyBox.getChildren().addAll(difficultyButton, bestTimeBox);

        return difficultyBox;
    }


    private int loadBestTime(String difficulty)
    {
        BestTimes bestTimes = BestTimeManager.loadBestTimes();
        return switch (difficulty)
        {
            case "beginner" -> bestTimes.getBeginnerBestTime();
            case "advanced" -> bestTimes.getAdvancedBestTime();
            case "pro" -> bestTimes.getProBestTime();
            default -> Integer.MAX_VALUE;
        };
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

    public Button getBeginnerButton()
    {
        return beginnerButton;
    }

    public void setBeginnerButton(Button beginnerButton)
    {
        this.beginnerButton = beginnerButton;
    }

    public Button getAdvancedButton()
    {
        return advancedButton;
    }

    public void setAdvancedButton(Button advancedButton)
    {
        this.advancedButton = advancedButton;
    }

    public Button getProButton()
    {
        return proButton;
    }

    public void setProButton(Button proButton)
    {
        this.proButton = proButton;
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
}
