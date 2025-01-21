package htl.steyr.javafx_minesweeper_tplatzer.controller;

import htl.steyr.javafx_minesweeper_tplatzer.service.LeaderboardClient;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Controller class for managing the global leaderboard UI in the Minesweeper game.
 * <p>
 * This class is responsible for displaying the leaderboard for different difficulty levels
 * (beginner, advanced, and pro), retrieving data from the leaderboard service, and formatting
 * the displayed entries.
 */
public class LeaderboardController extends Controller
{
    /**
     * Maximum width for the leaderboard VBox.
     */
    private static final int maxVBoxWidth = 1125;

    /**
     * Maximum height for the leaderboard VBox.
     */
    private static final int maxVBoxHeight = 525;

    /**
     * Maximum height for the leaderboard VBox.
     */
    private final Stage stage = new Stage();

    /**
     * The root container for the leaderboard UI.
     */
    private final VBox root = new VBox();

    /**
     * Container for all leaderboard columns.
     */
    private HBox columnsBox;

    /**
     * Column displaying leaderboard entries for beginner level.
     */
    private VBox beginnerColumn;

    /**
     * Column displaying leaderboard entries for advanced level.
     */
    private VBox advancedColumn;

    /**
     * Column displaying leaderboard entries for pro level.
     */
    private VBox proColumn;

    /**
     * Visual style of the leaderboard (e.g., "retro", "modern").
     */
    private String style;


    /**
     * Constructs a new {@code LeaderboardController} with the specified style.
     * <p>
     * This constructor initializes the controller and sets the visual style for the leaderboard.
     *
     * @param style The visual style used for the leaderboard (e.g., "retro", "modern").
     */
    public LeaderboardController(String style)
    {
        setStyle(style); // Sets the style for the leaderboard.
    }

    /**
     * Starts the leaderboard UI.
     * <p>
     * This method initializes the necessary UI elements, configures the stage with the specified style,
     * sets up the leaderboard window, and displays it to the user.
     */
    public void start()
    {
        initializeUserElements(); // Initializes the leaderboard UI components.

        initializeStage(getStage(), getStyle()); // Configures the stage with the specified visual style.
        initializeWindow(); // Sets up the leaderboard window, including loading leaderboard data.
        getStage().show(); // Displays the leaderboard window.
    }

    /**
     * Initializes the UI elements for the leaderboard.
     * <p>
     * This method sets up the leaderboard columns box, which contains the columns for different difficulty levels.
     */
    private void initializeUserElements()
    {
        initializeColumnsBox(); // Sets up the box containing leaderboard columns.
    }

    /**
     * Initializes the main leaderboard window.
     * <p>
     * This method configures the layout, styling, and size of the leaderboard window. It also fetches
     * the leaderboard data from the server and populates the columns with player entries for different
     * difficulty levels. If the data cannot be loaded, an error message is displayed instead.
     */
    private void initializeWindow()
    {
        getRoot().setSpacing(10); // Sets the spacing between UI elements.
        getRoot().setAlignment(Pos.CENTER); // Aligns all elements in the center.
        getRoot().setMinSize(LeaderboardController.getMaxVBoxWidth(), LeaderboardController.getMaxVBoxHeight()); // Sets minimum size.
        getRoot().setMaxSize(LeaderboardController.getMaxVBoxWidth(), LeaderboardController.getMaxVBoxHeight()); // Sets maximum size.
        getRoot().prefWidthProperty().bind(getStage().widthProperty()); // Binds root width to stage width.
        getRoot().prefHeightProperty().bind(getStage().heightProperty()); // Binds root height to stage height.
        getRoot().getChildren().addAll(getColumnsBox()); // Adds the columns box to the root container.
        getRoot().getStyleClass().add("leaderboard-root"); // Adds a style class for styling the root.
        getRoot().getStylesheets().addAll( // Adds stylesheets for visual appearance.
                Objects.requireNonNull(getClass().getResource("/" + getStyle() + "/style/style.css")).toExternalForm(),
                Objects.requireNonNull(getClass().getResource("/" + getStyle() + "/style/leaderboardStyle.css")).toExternalForm()
        );

        try
        {
            LeaderboardClient client = new LeaderboardClient(); // Creates a client to fetch leaderboard data.
            Map<String, List<Map<String, Object>>> leaderboard = client.fetchLeaderboard(); // Fetches leaderboard data.

            // Populates the beginner column with leaderboard entries.
            for (Map<String, Object> entry : leaderboard.get("beginner"))
            {
                Label entryLabel = new Label(formatLeaderboardEntry((String) entry.get("username"), ((int) entry.get("time"))));
                entryLabel.getStyleClass().add("leaderboard-entry");
                getBeginnerColumn().getChildren().add(entryLabel);
            }

            // Populates the advanced column with leaderboard entries.
            for (Map<String, Object> entry : leaderboard.get("advanced"))
            {
                Label entryLabel = new Label(formatLeaderboardEntry((String) entry.get("username"), ((int) entry.get("time"))));
                entryLabel.getStyleClass().add("leaderboard-entry");
                getAdvancedColumn().getChildren().add(entryLabel);
            }

            // Populates the pro column with leaderboard entries.
            for (Map<String, Object> entry : leaderboard.get("pro"))
            {
                Label entryLabel = new Label(formatLeaderboardEntry((String) entry.get("username"), ((int) entry.get("time"))));
                entryLabel.getStyleClass().add("leaderboard-entry");
                getProColumn().getChildren().add(entryLabel);
            }
        } catch (Exception e)
        {
            // Displays an error message if the leaderboard data cannot be loaded.
            getRoot().getChildren().add(new Label("Failed to load leaderboard."));
            System.out.println(e.getMessage());
        }

        // Creates and configures the scene for the leaderboard.
        Scene scene = new Scene(getRoot());
        getStage().setTitle("Global Leaderboard"); // Sets the window title.
        getStage().setResizable(false); // Disables window resizing.
        getStage().setScene(scene); // Sets the scene for the stage.
    }

    /**
     * Initializes the box containing the leaderboard columns.
     * <p>
     * This method creates an {@link HBox} to hold the leaderboard columns for different difficulty levels
     * (Beginner, Advanced, Pro). Each column is initialized with a header label and added to the box.
     */
    private void initializeColumnsBox()
    {
        setColumnsBox(new HBox()); // Creates a new HBox to hold the leaderboard columns.
        getColumnsBox().setSpacing(20); // Sets the spacing between the columns.

        // Initializes the individual columns for each difficulty level.
        setBeginnerColumn(initializeColumns("Beginner"));
        setAdvancedColumn(initializeColumns("Advanced"));
        setProColumn(initializeColumns("Pro"));

        // Adds all initialized columns to the HBox.
        getColumnsBox().getChildren().addAll(getBeginnerColumn(), getAdvancedColumn(), getProColumn());
    }

    /**
     * Initializes a leaderboard column with a header label.
     * <p>
     * This method creates a {@link VBox} representing a column in the leaderboard, sets its layout properties,
     * and applies styling. The column is initialized with a header label indicating the difficulty level.
     *
     * @param label The header label for the column (e.g., "Beginner", "Advanced", "Pro").
     * @return A {@link VBox} representing the initialized leaderboard column.
     */
    private VBox initializeColumns(String label)
    {
        VBox column = new VBox(new Label(label)); // Creates a VBox and adds a header label.
        column.setSpacing(5); // Sets spacing between elements in the column.
        column.getStyleClass().add("leaderboard-column"); // Applies the "leaderboard-column" style class.
        column.getChildren().getFirst().getStyleClass().add("leaderboard-header"); // Applies a style class to the header label.

        return column; // Returns the initialized column.
    }

    /**
     * Formats a leaderboard entry with the player's username and their best time.
     * <p>
     * This method creates a formatted string that displays the player's username
     * (left-aligned with a fixed width) and their best time in seconds.
     *
     * @param username The username of the player.
     * @param time     The player's best time in seconds.
     * @return A formatted string representing the leaderboard entry.
     * Example: {@code "player123       :  45 seconds"}.
     */
    private String formatLeaderboardEntry(String username, int time)
    {
        // Formats the username with a width of 16 characters and the time as a 3-digit integer.
        return String.format("%-16s : %4d seconds", username, time);
    }

    /**
     * Gets the maximum width for the leaderboard VBox.
     *
     * @return The maximum width in pixels.
     */
    public static int getMaxVBoxWidth()
    {
        return maxVBoxWidth;
    }

    /**
     * Gets the maximum height for the leaderboard VBox.
     *
     * @return The maximum height in pixels.
     */
    public static int getMaxVBoxHeight()
    {
        return maxVBoxHeight;
    }

    /**
     * Gets the root {@link VBox} of the leaderboard UI.
     *
     * @return The root {@link VBox} containing all UI elements for the leaderboard.
     */
    public VBox getRoot()
    {
        return root;
    }

    /**
     * Gets the {@link Stage} used for the leaderboard window.
     *
     * @return The {@link Stage} object representing the leaderboard window.
     */
    public Stage getStage()
    {
        return stage;
    }

    /**
     * Gets the visual style of the leaderboard.
     *
     * @return A {@link String} representing the style (e.g., "retro", "modern").
     */
    public String getStyle()
    {
        return style;
    }

    /**
     * Sets the visual style of the leaderboard.
     *
     * @param style A {@link String} representing the style (e.g., "retro", "modern").
     */
    public void setStyle(String style)
    {
        this.style = style;
    }

    /**
     * Gets the {@link VBox} representing the column for the beginner difficulty level.
     *
     * @return The {@link VBox} containing leaderboard entries for the beginner level.
     */
    public VBox getBeginnerColumn()
    {
        return beginnerColumn;
    }

    /**
     * Sets the {@link VBox} representing the column for the beginner difficulty level.
     *
     * @param beginnerColumn The {@link VBox} to set for the beginner leaderboard column.
     */
    public void setBeginnerColumn(VBox beginnerColumn)
    {
        this.beginnerColumn = beginnerColumn;
    }

    /**
     * Gets the {@link VBox} representing the column for the advanced difficulty level.
     *
     * @return The {@link VBox} containing leaderboard entries for the advanced level.
     */
    public VBox getAdvancedColumn()
    {
        return advancedColumn;
    }

    /**
     * Sets the {@link VBox} representing the column for the advanced difficulty level.
     *
     * @param advancedColumn The {@link VBox} to set for the advanced leaderboard column.
     */
    public void setAdvancedColumn(VBox advancedColumn)
    {
        this.advancedColumn = advancedColumn;
    }

    /**
     * Gets the {@link VBox} representing the column for the pro difficulty level.
     *
     * @return The {@link VBox} containing leaderboard entries for the pro level.
     */
    public VBox getProColumn()
    {
        return proColumn;
    }

    /**
     * Sets the {@link VBox} representing the column for the pro difficulty level.
     *
     * @param proColumn The {@link VBox} to set for the pro leaderboard column.
     */
    public void setProColumn(VBox proColumn)
    {
        this.proColumn = proColumn;
    }

    /**
     * Gets the {@link HBox} containing all leaderboard columns.
     *
     * @return The {@link HBox} containing the beginner, advanced, and pro columns.
     */
    public HBox getColumnsBox()
    {
        return columnsBox;
    }

    /**
     * Sets the {@link HBox} containing all leaderboard columns.
     *
     * @param columnsBox The {@link HBox} to set for the leaderboard columns container.
     */
    public void setColumnsBox(HBox columnsBox)
    {
        this.columnsBox = columnsBox;
    }
}
