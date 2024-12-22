package htl.steyr.javafx_minesweeper_tplatzer;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Objects;

public class GameController extends Controller
{
    private static final int maxHBoxWidth = 1000;
    private static final int maxHBoxHeight = 700;
    private static final int maxButtonWidth = 200;
    private static final int maxButtonHeight = 100;
    private Scene gameScene;
    private VBox root = new VBox();
    private Stage stage;
    private String difficulty;

    public GameController(String difficulty)
    {
        setDifficulty(difficulty);
    }

    public void start(Stage stage)
    {
        setStage(stage);
        /*setDefaultValues();
        initializeUserElements();
        playBackgroundMusic();*/

        initializeWindow();
    }

    private void initializeWindow()
    {
        getRoot().setSpacing(20);
        getRoot().setAlignment(Pos.CENTER);
        getRoot().setMinSize(GameController.getMaxHBoxWidth(), GameController.getMaxHBoxHeight());
        getRoot().setMaxSize(GameController.getMaxHBoxWidth(), GameController.getMaxHBoxHeight());
        getRoot().prefWidthProperty().bind(getStage().widthProperty());
        getRoot().prefHeightProperty().bind(getStage().heightProperty());
        getRoot().getChildren().addAll(new Text(getDifficulty()));
        getRoot().getStylesheets().addAll(
                Objects.requireNonNull(getClass().getResource("/style/style.css")).toExternalForm(),
                Objects.requireNonNull(getClass().getResource("/style/gameStyle.css")).toExternalForm());

        setGameScene(new Scene(getRoot()));

        switchScene(getStage(), getGameScene(), 900, 1200, "Bomben Räum Simulator");
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

    public String getDifficulty()
    {
        return difficulty;
    }

    public void setDifficulty(String difficulty)
    {
        this.difficulty = difficulty;
    }

    public Scene getGameScene()
    {
        return gameScene;
    }

    public void setGameScene(Scene gameScene)
    {
        this.gameScene = gameScene;
    }

    public VBox getRoot()
    {
        return root;
    }

    public void setRoot(VBox root)
    {
        this.root = root;
    }

    public Stage getStage()
    {
        return stage;
    }

    public void setStage(Stage stage)
    {
        this.stage = stage;
    }
}
