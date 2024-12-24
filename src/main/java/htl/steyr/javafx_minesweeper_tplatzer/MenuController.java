package htl.steyr.javafx_minesweeper_tplatzer;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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
    private VBox root = new VBox();
    private Text titleText;
    private Text choiceText;
    private Button beginnerButton;
    private Button advancedButton;
    private Button proButton;
    private HBox difficultyButtonBox = new HBox();
    private VBox chooseGameModeBox = new VBox();

    public void start(Stage stage)
    {
        setStage(stage);
        setDefaultValues();
        initializeUserElements();
        playBackgroundMusic("menu-music");

        initializeStage(getStage());
        initializeWindow();
        getStage().show();
    }

    private void setDefaultValues()
    {

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
        getRoot().getStylesheets().addAll(
                Objects.requireNonNull(getClass().getResource("/style/style.css")).toExternalForm(),
                Objects.requireNonNull(getClass().getResource("/style/menuStyle.css")).toExternalForm());

        setMenuScene(new Scene(getRoot()));
        switchScene(getStage(), getMenuScene(), 400, 800, "Menü");
    }

    private void handleButtonClick(ActionEvent event)
    {
        stopBackgroundMusic();

        Button clickedButton = (Button) event.getSource();
        String difficulty = clickedButton.getId();

        switch (difficulty)
        {
            case "beginner" -> new GameController("beginner").start(stage);
            case "advanced" -> new GameController("advanced").start(stage);
            case "pro" -> new GameController("pro").start(stage);
        }
    }

    private void initializeUserElements()
    {
        setTitleText(initializeText("titleText", "Bomben Räum Simulator"));
        setChoiceText(initializeText("choiceText", "Wähle einen Spielmodus"));
        initializeChooseGameModeBox();

    }

    private Button initializeDifficultyButton(String id)
    {
        Button button = new Button();
        //button.setMaxSize(getMaxButtonWidth(), getMaxButtonHeight());
        button.getStyleClass().add("button");
        button.setId(id);
        button.setText(id.toUpperCase());
        button.setFocusTraversable(false);
        button.setOnAction(this::handleButtonClick);
        button.getStylesheets().addAll(
                Objects.requireNonNull(getClass().getResource("/style/style.css")).toExternalForm(),
                Objects.requireNonNull(getClass().getResource("/style/menuStyle.css")).toExternalForm());
        HBox.setHgrow(button, Priority.ALWAYS);
        button.prefWidthProperty().bind(getDifficultyButtonBox().widthProperty().divide(4).subtract(20));
        button.prefHeightProperty()
                .bind(getDifficultyButtonBox().heightProperty().multiply(0.8));

        return button;
    }

    private void initializeChooseGameModeBox()
    {
        getChooseGameModeBox().setSpacing(10);
        getChooseGameModeBox().setAlignment(Pos.CENTER);
        getChooseGameModeBox().setMinSize(MenuController.getMaxVBoxWidth(), MenuController.getMaxVBoxHeight());
        getChooseGameModeBox().setMaxSize(MenuController.getMaxVBoxWidth(), MenuController.getMaxVBoxHeight());

        initializeDifficultyButtonBox();
        getChooseGameModeBox().getChildren().addAll(getChoiceText(), getDifficultyButtonBox());
    }

    private void initializeDifficultyButtonBox()
    {
        setBeginnerButton(initializeDifficultyButton("beginner"));
        setAdvancedButton(initializeDifficultyButton("advanced"));
        setProButton(initializeDifficultyButton("pro"));

        getDifficultyButtonBox().setSpacing(10);
        getDifficultyButtonBox().setAlignment(Pos.CENTER);
        getDifficultyButtonBox().getStyleClass().add("box");
        getDifficultyButtonBox().setId("difficultyButtonBox");

        getDifficultyButtonBox().getChildren().addAll(getBeginnerButton(), getAdvancedButton(), getProButton());
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

    public static int getMaxVBoxWidth() {return maxVBoxWidth;}

    public static int getMaxVBoxHeight() {return maxVBoxHeight;}

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

    public void setRoot(VBox root)
    {
        this.root = root;
    }

    public Scene getMenuScene()
    {
        return menuScene;
    }

    public void setMenuScene(Scene menuScene)
    {
        this.menuScene = menuScene;
    }

    public HBox getDifficultyButtonBox()
    {
        return difficultyButtonBox;
    }

    public void setDifficultyButtonBox(HBox difficultyButtonBox)
    {
        this.difficultyButtonBox = difficultyButtonBox;
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
}