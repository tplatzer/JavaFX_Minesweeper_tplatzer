package htl.steyr.javafx_minesweeper_tplatzer;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LeaderboardController extends Controller
{
    private static final int maxVBoxWidth = 1050;
    private static final int maxVBoxHeight = 525;
    private final Stage stage = new Stage();
    private final VBox root = new VBox();
    private HBox columnsBox;
    private VBox beginnerColumn;
    private VBox advancedColumn;
    private VBox proColumn;
    private String style;

    public LeaderboardController(String style)
    {
        setStyle(style);
    }

    public void start()
    {
        initializeUserElements();

        initializeStage(getStage(), getStyle());
        initializeWindow();
        getStage().show();
    }

    private void initializeUserElements()
    {
        initializeColumnsBox();
    }

    private void initializeWindow()
    {
        getRoot().setSpacing(10);
        getRoot().setAlignment(Pos.CENTER);
        getRoot().setMinSize(LeaderboardController.getMaxVBoxWidth(), LeaderboardController.getMaxVBoxHeight());
        getRoot().setMaxSize(LeaderboardController.getMaxVBoxWidth(), LeaderboardController.getMaxVBoxHeight());
        getRoot().prefWidthProperty().bind(getStage().widthProperty());
        getRoot().prefHeightProperty().bind(getStage().heightProperty());
        getRoot().getChildren().addAll(getColumnsBox());
        getRoot().getStyleClass().add("leaderboard-root");
        getRoot().getStylesheets().addAll(
                Objects.requireNonNull(getClass().getResource("/" + getStyle() + "/style/style.css")).toExternalForm(),
                Objects.requireNonNull(getClass().getResource("/" + getStyle() + "/style/leaderboardStyle.css")).toExternalForm());

        try
        {
            LeaderboardClient client = new LeaderboardClient();
            Map<String, List<Map<String, Object>>> leaderboard = client.fetchLeaderboard();

            for (Map<String, Object> entry : leaderboard.get("beginner"))
            {
                Label entryLabel = new Label(formatLeaderboardEntry((String) entry.get("username"), ((int) entry.get("time"))));
                entryLabel.getStyleClass().add("leaderboard-entry");
                getBeginnerColumn().getChildren().add(entryLabel);
            }
            for (Map<String, Object> entry : leaderboard.get("advanced"))
            {
                Label entryLabel = new Label(formatLeaderboardEntry((String) entry.get("username"), ((int) entry.get("time"))));
                entryLabel.getStyleClass().add("leaderboard-entry");
                getAdvancedColumn().getChildren().add(entryLabel);
            }
            for (Map<String, Object> entry : leaderboard.get("pro"))
            {
                Label entryLabel = new Label(formatLeaderboardEntry((String) entry.get("username"), ((int) entry.get("time"))));
                entryLabel.getStyleClass().add("leaderboard-entry");
                getProColumn().getChildren().add(entryLabel);
            }
        } catch (Exception e)
        {
            getRoot().getChildren().add(new Label("Failed to load leaderboard."));
            System.err.println(e.getMessage());
        }

        Scene scene = new Scene(getRoot());

        getStage().setTitle("Global Leaderboard");
        getStage().setScene(scene);
    }

    private void initializeColumnsBox()
    {
        setColumnsBox(new HBox());
        getColumnsBox().setSpacing(20);

        setBeginnerColumn(initializeColumns("Beginner"));
        setAdvancedColumn(initializeColumns("Advanced"));
        setProColumn(initializeColumns("Pro"));
        getColumnsBox().getChildren().addAll(getBeginnerColumn(), getAdvancedColumn(), getProColumn());
    }

    private VBox initializeColumns(String label)
    {
        VBox column = new VBox(new Label(label));
        column.setSpacing(5);
        column.getStyleClass().add("leaderboard-column");
        column.getChildren().getFirst().getStyleClass().add("leaderboard-header");

        return column;
    }

    private String formatLeaderboardEntry(String username, int time) {
        return String.format("%-16s : %3d seconds", username, time);
    }

    public static int getMaxVBoxWidth()
    {
        return maxVBoxWidth;
    }

    public static int getMaxVBoxHeight()
    {
        return maxVBoxHeight;
    }

    public VBox getRoot()
    {
        return root;
    }

    public Stage getStage()
    {
        return stage;
    }

    public String getStyle()
    {
        return style;
    }

    public void setStyle(String style)
    {
        this.style = style;
    }

    public VBox getBeginnerColumn()
    {
        return beginnerColumn;
    }

    public void setBeginnerColumn(VBox beginnerColumn)
    {
        this.beginnerColumn = beginnerColumn;
    }

    public VBox getAdvancedColumn()
    {
        return advancedColumn;
    }

    public void setAdvancedColumn(VBox advancedColumn)
    {
        this.advancedColumn = advancedColumn;
    }

    public VBox getProColumn()
    {
        return proColumn;
    }

    public void setProColumn(VBox proColumn)
    {
        this.proColumn = proColumn;
    }

    public HBox getColumnsBox()
    {
        return columnsBox;
    }

    public void setColumnsBox(HBox columnsBox)
    {
        this.columnsBox = columnsBox;
    }
}
