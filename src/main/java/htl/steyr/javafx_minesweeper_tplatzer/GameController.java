package htl.steyr.javafx_minesweeper_tplatzer;

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
 * @ToDo
 *
 * Zahlen Schriftwart größer
 * Bomben Counter wird Hellblau, wenn der Counter auf 0 ist und dann kann man draufklicken um alle nicht geflaggten Cellen zu revealen.
 * Alle falsch gesetzten Flags werden zu durchgestrichenen Bomben, wenn eine Bombe aufgdeckt wird (das Game ended)
 *
 * felder mit Bomben werden beim Vollständigen aufdecken Rot (Wenn Game Over)
 *
 * der Flage Counter soll 099 anzeichen und wenn es negativ wird dann -01 (also dreistellig wenn positiv und 2 stellig mit minus wenn negativ)
 */

public class GameController extends Controller
{
    private static final int maxHBoxWidth = 1000;
    private static final int maxHBoxHeight = 700;
    private Scene gameScene;
    private final VBox root = new VBox();
    private HBox gameInfoBox;
    private HBox remainingFlagCounterBox;
    private Label remainingFlagCounterLabel;
    private Button restartGameButton;
    private HBox timerBox;
    private Label timerLabel;
    private int elapsedTime;
    private Timeline timer;
    private GridPane gameField;
    private Stage stage;
    private String difficulty;
    private int totalMines;
    private int rows;
    private int columns;
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
        playBackgroundMusic("background-music");

        initializeWindow();
    }

    private void setDefaultValues()
    {
        setElapsedTime(0);
        setTotalMines(0);
        setRows(0);
        setColumns(0);
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
        switchScene(getStage(), getGameScene(), getDifficulty(), "Bomben Räum Simulator");
    }

    private void restartGame()
    {
        stopBackgroundMusic();
        new GameController(getDifficulty()).start(getStage());
    }

    protected void endGame(boolean won)
    {
        stopTimer();
        stopBackgroundMusic();

        getRestartGameButton().setDisable(true);
        for (Cell cell : getCells())
        {
            cell.getButton().setDisable(true);
        }

        if (won)
        {
            wonGame();
        } else
        {
            lossGame();
        }
    }

    private void wonGame()
    {
        updateRestartGameButton("win");
        String winJingle = getRandomWinJingle();
        playSoundEffect(winJingle);

        Timeline delay = new Timeline(new KeyFrame(Duration.seconds(MusicPlayer.getSoundEffectDuration(winJingle) - 0.5), event -> switchToMenu()));
        delay.play();
    }

    private void lossGame()
    {
        updateRestartGameButton("lose");

        List<Cell> bombCells = new ArrayList<>();
        for (Cell cell : getCells())
        {
            if (cell.isBomb())
            {
                bombCells.add(cell);
            }
        }

        String bombExplosionSound = getRandomBombExplosionSound();
        playSoundEffect(bombExplosionSound);

        Timeline revealBombsTimeLine = new Timeline();
        for (Cell bombCell : bombCells)
        {
            revealBombsTimeLine.getKeyFrames().add(new KeyFrame(Duration.seconds(MusicPlayer.getSoundEffectDuration(bombExplosionSound) - 0.5), event -> bombCell.silentReveal()));
        }

        revealBombsTimeLine.setOnFinished(event ->
        {
            String loseJingle = getRandomLoseJingle();
            playSoundEffect(loseJingle);

            new Timeline(new KeyFrame(Duration.seconds(MusicPlayer.getSoundEffectDuration(loseJingle) - 0.5), ev -> switchToMenu())).play();
        });

        revealBombsTimeLine.play();
    }

    private void switchToMenu()
    {
        new MenuController().start(getStage());
    }

    protected void checkWinCondition()
    {
        boolean allNotBombCellsRevealed = getCells().stream()
                .filter(cell -> !cell.isBomb())
                .allMatch(Cell::isRevealed);

        if (allNotBombCellsRevealed)
        {
            endGame(true);
        }
    }

    private void initializeUserElements()
    {
        initializeGameInfoBox();
        initializeGameField();
    }

    private void initializeGameInfoBox()
    {
        setGameInfoBox(new HBox());
        getGameInfoBox().setAlignment(Pos.CENTER);
        getGameInfoBox().getStyleClass().add("game-info-box");

        Region leftSpacer = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        initializeRemainingFlagsCounterBox();
        initializeRestartGameButton();
        initializeTimerBox();

        getGameInfoBox().getChildren().addAll(
                getRemainingFlagCounterBox(),
                leftSpacer,
                getRestartGameButton(),
                rightSpacer,
                getTimerBox());
    }

    private void initializeRemainingFlagsCounterBox()
    {
        setRemainingFlagCounterBox(new HBox());
        getRemainingFlagCounterBox().setAlignment(Pos.CENTER);
        getRemainingFlagCounterBox().getStyleClass().add("info-box");

        initializeRemainingFlagsCounterLabel();

        getRemainingFlagCounterBox().getChildren().add(getRemainingFlagCounterLabel());
    }

    private void initializeRemainingFlagsCounterLabel()
    {
        setRemainingFlagCounterLabel(new Label(formatCounter(getTotalMines())));
        getRemainingFlagCounterLabel().getStyleClass().add("info-label");
        getRemainingFlagCounterLabel().setId("counter-red");
    }

    protected void updateRemainingFlagsCounter()
    {
        int remainingBombs = getTotalMines() - (int) getCells().stream().filter(Cell::isFlagged).count();

        getRemainingFlagCounterLabel().setText(formatCounter(remainingBombs));

        if (remainingBombs == 0) getRemainingFlagCounterLabel().setId("counter-teal");
        else getRemainingFlagCounterLabel().setId("counter-red");
    }

    private void initializeRestartGameButton()
    {
        setRestartGameButton(new Button());
        getRestartGameButton().setFocusTraversable(false);
        getRestartGameButton().getStyleClass().add("restart-button");

        updateRestartGameButton("neutral");

        getRestartGameButton().setOnAction(event -> restartGame());
    }

    private void updateRestartGameButton(String status)
    {
        switch (status)
        {
            case "win" -> getRestartGameButton().setText("😎");
            case "lose" -> getRestartGameButton().setText("😵");
            case "nervous" -> getRestartGameButton().setText("😯");
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
        getTimer().play();
    }

    private void stopTimer()
    {
        if (getTimer() != null)
        {
            getTimer().stop();
        }
    }

    private void initializeGameField()
    {
        int rows;
        int columns;

        switch (getDifficulty())
        {
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
        setRows(rows);
        setColumns(columns);
        setCells(new ArrayList<>());
        initializeGameFieldGridPane();

        double buttonWidth = (double) getMaxHBoxHeight() / columns;
        double buttonHeight = (double) getMaxHBoxWidth() / rows;

        for (int row = 0; row < rows; row++)
        {
            for (int col = 0; col < columns; col++)
            {
                Cell cell = new Cell(false, this, row, col);
                cell.getButton().setPrefWidth(buttonWidth);
                cell.getButton().setPrefHeight(buttonHeight);

                cell.getButton().setOnMousePressed(event ->
                {
                    if (event.isPrimaryButtonDown() || event.isSecondaryButtonDown())
                    {
                        updateRestartGameButton("nervous");
                    }
                });

                cell.getButton().setOnMouseReleased(event ->
                {
                    if (!event.isPrimaryButtonDown() || !event.isSecondaryButtonDown())
                    {
                        updateRestartGameButton("neutral");
                    }
                });

                getCells().add(cell);
            }
        }

        Random random = new Random();
        int bombsPlaced = 0;
        while (bombsPlaced < getTotalMines())
        {
            int randomRow = random.nextInt(rows);
            int randomCol = random.nextInt(columns);

            Cell cell = getCellAt(randomRow, randomCol);
            if (!cell.isBomb())
            {
                cell.setBomb(true);
                bombsPlaced++;
            }
        }

        for (int row = 0; row < rows; row++)
        {
            for (int col = 0; col < columns; col++)
            {
                Cell cell = getCellAt(row, col);
                if (!cell.isBomb())
                {
                    cell.setAdjacentBombs(countAdjacentBombs(row, col));
                }
            }
        }

        for (int row = 0; row < rows; row++)
        {
            for (int col = 0; col < columns; col++)
            {
                Cell cell = getCellAt(row, col);
                getGameField().add(cell.getButton(), col, row);
            }
        }

        updateRemainingFlagsCounter();
    }

    private void initializeGameFieldGridPane()
    {
        setGameField(new GridPane());
        getGameField().setAlignment(Pos.CENTER);
        getGameField().getStyleClass().add("game-field");

        double gap = 0;
        getGameField().setHgap(gap);
        getGameField().setVgap(gap);

        for (int i = 0; i < getColumns(); i++)
        {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPercentWidth(100.0 / getColumns());
            getGameField().getColumnConstraints().add(colConstraints);
        }

        for (int i = 0; i < getRows(); i++)
        {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / getRows());
            getGameField().getRowConstraints().add(rowConstraints);
        }
    }

    private int countAdjacentBombs(int row, int col)
    {
        int count = 0;
        for (int r = row - 1; r <= row + 1; r++)
        {
            for (int c = col - 1; c <= col + 1; c++)
            {
                if (isInBounds(r, c) && getCellAt(r, c).isBomb())
                {
                    count++;
                }
            }
        }
        return count;
    }

    protected boolean isInBounds(int row, int col)
    {
        return row >= 0 && row < getGameField().getRowCount() && col >= 0 && col < getGameField().getColumnCount();
    }

    protected Cell getCellAt(int row, int col)
    {
        int columns = getGameField().getColumnCount();
        return getCells().get(row * columns + col);
    }

    private String formatCounter(int count)
    {
        return String.format("%s%02d", count < 0 ? "-" : "", Math.abs(count));
    }

    private String formatTime(int time)
    {
        return String.format("%03d", time);
    }

    private String getRandomWinJingle()
    {
        return "win-jingle-" + (Math.random() < 0.5 ? "1" : "2");
    }

    private String getRandomLoseJingle()
    {
        return "lose-jingle-" + (Math.random() < 0.5 ? "1" : "2");
    }

    private String getRandomBombExplosionSound()
    {
        return "bomb-explosion-" + (Math.random() < 0.5 ? "1" : "2");
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

    public HBox getRemainingFlagCounterBox()
    {
        return remainingFlagCounterBox;
    }

    public void setRemainingFlagCounterBox(HBox remainingFlagCounterBox)
    {
        this.remainingFlagCounterBox = remainingFlagCounterBox;
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

    public Label getRemainingFlagCounterLabel()
    {
        return remainingFlagCounterLabel;
    }

    public void setRemainingFlagCounterLabel(Label remainingFlagCounterLabel)
    {
        this.remainingFlagCounterLabel = remainingFlagCounterLabel;
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

    public int getRows()
    {
        return rows;
    }

    public void setRows(int rows)
    {
        this.rows = rows;
    }

    public int getColumns()
    {
        return columns;
    }

    public void setColumns(int columns)
    {
        this.columns = columns;
    }
}
