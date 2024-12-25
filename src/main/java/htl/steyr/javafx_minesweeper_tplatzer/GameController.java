package htl.steyr.javafx_minesweeper_tplatzer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

public class GameController extends Controller
{
    private static final int maxHBoxWidth = 1000;
    private static final int maxHBoxHeight = 700;
    private Scene gameScene;
    private VBox root = new VBox();
    private HBox gameInfoBox;
    private HBox remainingNotMarkedBombsCounterBox;
    private Label remainingNotMarkedBombsLabel;
    private Button restartGameButton;
    private HBox timerBox;
    private Label timerLabel;
    private int elapsedTime = 0;
    private Timeline timer;
    private GridPane gameField;
    private Stage stage;
    private String difficulty;
    private int totalMines = 0;
    private List<Cell> cells;

    public GameController(String difficulty)
    {
        setDifficulty(difficulty);
    }

    public void start(Stage stage)
    {
        setStage(stage);
        setDefaultValues();
        initializeUserElements();
        //playBackgroundMusic("background-music");

        initializeWindow();
    }

    private void setDefaultValues()
    {
        setElapsedTime(0);
        setTotalMines(0);
    }

    private void initializeWindow()
    {
        getRoot().setSpacing(20);
        getRoot().setAlignment(Pos.CENTER);
        getRoot().setMinSize(GameController.getMaxHBoxWidth(), GameController.getMaxHBoxHeight());
        getRoot().setMaxSize(GameController.getMaxHBoxWidth(), GameController.getMaxHBoxHeight());
        getRoot().prefWidthProperty().bind(getStage().widthProperty());
        getRoot().prefHeightProperty().bind(getStage().heightProperty());
        getRoot().getChildren().addAll(getGameInfoBox(), getGameField());
        getRoot().getStyleClass().add("root-container");
        getRoot().getStylesheets().addAll(
                Objects.requireNonNull(getClass().getResource("/style/style.css")).toExternalForm(),
                Objects.requireNonNull(getClass().getResource("/style/gameStyle.css")).toExternalForm());

        setGameScene(new Scene(getRoot()));

        switchScene(getStage(), getGameScene(), 900, 1200, "Bomben Räum Simulator");
    }

    private void handleButtonClick(ActionEvent event)
    {

    }

    private void restartGame()
    {

    }

    private void initializeUserElements()
    {
        initializeGameInfoBox();
        initializeGameField();
    }

    private void initializeGameInfoBox()
    {
        setGameInfoBox(new HBox());

        initializeRemainingNotMarkedBombsCounterBox();
        initializeRestartGameButton();
        initializeTimerBox();

        getGameInfoBox().getChildren().addAll(getRemainingNotMarkedBombsCounterBox(), getRestartGameButton(), getTimerBox());
    }

    private void initializeRemainingNotMarkedBombsCounterBox()
    {
        setRemainingNotMarkedBombsCounterBox(new HBox());
        getRemainingNotMarkedBombsCounterBox().setAlignment(Pos.CENTER);
        getRemainingNotMarkedBombsCounterBox().getStyleClass().add("info-box");

        initializeRemainingNotMarkedBombsLabel();

        getRemainingNotMarkedBombsCounterBox().getChildren().add(getRemainingNotMarkedBombsLabel());
    }

    private void initializeRemainingNotMarkedBombsLabel()
    {
        setRemainingNotMarkedBombsLabel(new Label(formatCounter(getTotalMines())));
        getRemainingNotMarkedBombsLabel().getStyleClass().add("info-label");
    }

    private void updateRemainingNotMarkedBombsCounter()
    {
        getRemainingNotMarkedBombsLabel().setText(formatCounter(getRemainingNotMarkedBombs()));
    }

    private void initializeRestartGameButton()
    {
        setRestartGameButton(new Button());
        getRestartGameButton().getStyleClass().add("restart-button");
        getRestartGameButton().setFocusTraversable(false);

        updateRestartGameButton("neutral");

        getRestartGameButton().setOnAction(event -> restartGame());
    }

    private void updateRestartGameButton(String status)
    {
        switch (status)
        {
            case "win" -> getRestartGameButton().setText("😎");
            case "lose" -> getRestartGameButton().setText("😵");
            default -> getRestartGameButton().setText("🙂");
        }
    }

    private void initializeTimerBox()
    {
        setTimerBox(new HBox());
        getTimerBox().setAlignment(Pos.CENTER);
        getTimerBox().getStyleClass().add("info-box");

        initializeTimerLabel();
    }

    private void initializeTimerLabel()
    {
        setTimerLabel(new Label(formatTime(getElapsedTime())));
        getTimerLabel().getStyleClass().add("info-label");

        getTimerBox().getChildren().add(getTimerLabel());

        startTimer();
    }

    private void startTimer()
    {
        setTimer(new Timeline(new KeyFrame(Duration.seconds(1), event ->
        {
            setElapsedTime(getElapsedTime() + 1);
            getTimerLabel().setText(formatTime(getElapsedTime()));
        })));
        getTimer().setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void stopTimer()
    {
        if (timer != null)
        {
            timer.stop();
        }
    }

    private void resetTimer()
    {
        stopTimer();
        setElapsedTime(0);
        getTimerLabel().setText(formatTime(getElapsedTime()));
        startTimer();
    }

    private void initializeGameField()
    {
        int rows;
        int columns;

        switch (getDifficulty()) {
            case "beginner":
                rows = 8;
                columns = 8;
                setTotalMines(10);
                break;
            case "advanced":
                rows = 16;
                columns = 16;
                setTotalMines(40);
                break;
            case "pro":
                rows = 16;
                columns = 30;
                setTotalMines(99);
                break;
            default:
                throw new IllegalArgumentException("Unknown Difficulty: " + getDifficulty());
        }

        initializeField(rows, columns);
    }

    private void initializeField(int rows, int columns)
    {
        setCells(new ArrayList<>());

        initializeGameFieldGridPane();

        for (int i = 0; i < rows * columns; i++)
        {
            getCells().add(new Cell(i < getTotalMines()));
        }
        Collections.shuffle(getCells());

        for (int row = 0; row < rows; row++)
        {
            for (int col = 0; col < columns; col++)
            {
                Cell cell = getCells().get(row * columns + col);
                getGameField().add(cell.getButton(), col, row);
            }
        }

        updateRemainingNotMarkedBombsCounter();
    }

    private void initializeGameFieldGridPane()
    {
        setGameField(new GridPane());
        getGameField().setAlignment(Pos.CENTER);
        getGameField().getStyleClass().add("game-field");
    }

    private void placeFlag(Cell cell)
    {
        if (!cell.isFlagged() && !cell.isRevealed())
        {
            cell.setFlagged(true);
            setPlacedFlags(getPlacedFlags() + 1);
            updateRemainingNotMarkedBombsCounter();
        }
    }

    private void removeFlag(Cell cell)
    {
        if (cell.isFlagged() && !cell.isRevealed())
        {
            cell.setFlagged(false);
            setPlacedFlags(getPlacedFlags() - 1);
            updateRemainingNotMarkedBombsCounter();
        }
    }

    private int getRemainingNotMarkedBombs() {
        int correctFlags = 0;
        for (Cell cell : getCells()) {
            if (cell.isFlagged() && cell.isBomb()) {
                correctFlags++;
            }
        }
        return getTotalMines() - correctFlags;
    }

    private String formatCounter(int count)
    {
        return String.format("%03d", count);
    }

    private String formatTime(int time) {
        return String.format("%03d", time);
    }

    public static int getMaxHBoxWidth()
    {
        return maxHBoxWidth;
    }

    public static int getMaxHBoxHeight()
    {
        return maxHBoxHeight;
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

    public HBox getGameInfoBox()
    {
        return gameInfoBox;
    }

    public void setGameInfoBox(HBox gameInfoBox)
    {
        this.gameInfoBox = gameInfoBox;
    }

    public HBox getRemainingNotMarkedBombsCounterBox()
    {
        return remainingNotMarkedBombsCounterBox;
    }

    public void setRemainingNotMarkedBombsCounterBox(HBox remainingNotMarkedBombsCounterBox)
    {
        this.remainingNotMarkedBombsCounterBox = remainingNotMarkedBombsCounterBox;
    }

    public HBox getTimerBox()
    {
        return timerBox;
    }

    public void setTimerBox(HBox timerBox)
    {
        this.timerBox = timerBox;
    }

    public Button getRestartGameButton()
    {
        return restartGameButton;
    }

    public void setRestartGameButton(Button restartGameButton)
    {
        this.restartGameButton = restartGameButton;
    }

    public GridPane getGameField()
    {
        return gameField;
    }

    public void setGameField(GridPane gameField)
    {
        this.gameField = gameField;
    }

    public Label getTimerLabel()
    {
        return timerLabel;
    }

    public void setTimerLabel(Label timerLabel)
    {
        this.timerLabel = timerLabel;
    }

    public int getElapsedTime()
    {
        return elapsedTime;
    }

    public void setElapsedTime(int elapsedTime)
    {
        this.elapsedTime = elapsedTime;
    }

    public Timeline getTimer()
    {
        return timer;
    }

    public void setTimer(Timeline timer)
    {
        this.timer = timer;
    }

    public Label getRemainingNotMarkedBombsLabel()
    {
        return remainingNotMarkedBombsLabel;
    }

    public void setRemainingNotMarkedBombsLabel(Label remainingNotMarkedBombsLabel)
    {
        this.remainingNotMarkedBombsLabel = remainingNotMarkedBombsLabel;
    }

    public int getTotalMines()
    {
        return totalMines;
    }

    public void setTotalMines(int totalMines)
    {
        this.totalMines = totalMines;
    }

    public List<Cell> getCells()
    {
        return cells;
    }

    public void setCells(List<Cell> cells)
    {
        this.cells = cells;
    }
}
