package htl.steyr.javafx_minesweeper_tplatzer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class MenuController
{
    private static final int maxHBoxWidth = 1000;
    private static final int maxHBoxHeight = 700;
    private static final int maxButtonWidth = 200;
    private static final int maxButtonHeight = 100;
    private Stage stage;
    private Scene menuScene;
    private VBox root = new VBox();
    private Button[] difficultyButtons = new Button[3];

    public void start(Stage stage)
    {
        setStage(stage);
        setDefaultValues();
        initializeUserElements();
        playBackgroundMusic();

        showWindow();
    }

    private void setDefaultValues()
    {

    }

    private void playBackgroundMusic()
    {

    }

    private void showWindow()
    {
        getRoot().setSpacing(10);
        getRoot().setAlignment(Pos.CENTER);
        getRoot().setMinSize(MenuController.getMaxHBoxWidth(), MenuController.getMaxHBoxHeight());
        getRoot().setMaxSize(MenuController.getMaxHBoxWidth(), MenuController.getMaxHBoxHeight());
        getRoot().prefWidthProperty().bind(getStage().widthProperty());
        getRoot().prefHeightProperty().bind(getStage().heightProperty());
        getRoot().getChildren().addAll(/*Children*/);
        getRoot().getStylesheets().addAll(
                Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm(),
                Objects.requireNonNull(getClass().getResource("/menuStyle.css")).toExternalForm());

        setMenuScene(new Scene(getRoot()));
        getMenuScene().getStylesheets().addAll(
                Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm(),
                Objects.requireNonNull(getClass().getResource("/menuStyle.css")).toExternalForm());
        getStage().getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/img/icon.png")).toExternalForm()));
        getStage().setScene(getMenuScene());
        getStage().setTitle("Menu");
        getStage().setHeight(900);
        getStage().show();
    }

    private void handleButtonClick(ActionEvent event)
    {

    }

    private void initializeUserElements()
    {
        setDifficultyButtons(new Button[]{
                initializeButton("beginner"),
                initializeButton("advanced"),
                initializeButton("pro"),
        });
    }

    private Button initializeButton(String id)
    {
        Button button = new Button();
        button.setMaxSize(getMaxButtonWidth(), getMaxButtonHeight());
        button.setId(id);
        button.setFocusTraversable(false);
        button.setOnAction(this::handleButtonClick);

        return button;
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

    public Button[] getDifficultyButtons()
    {
        return difficultyButtons;
    }

    public void setDifficultyButtons(Button[] difficultyButtons)
    {
        this.difficultyButtons = difficultyButtons;
    }
}