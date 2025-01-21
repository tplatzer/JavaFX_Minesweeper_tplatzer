package htl.steyr.javafx_minesweeper_tplatzer.model;

import htl.steyr.javafx_minesweeper_tplatzer.controller.GameController;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;

import java.util.Objects;

/**
 * Represents a single cell in the Minesweeper game grid.
 * <p>
 * A cell can either be a bomb or a regular cell and supports various states such as flagged,
 * revealed, or questioned. Each cell interacts with a {@link GameController} to manage game logic
 * such as revealing cells, toggling flags, and checking win conditions.
 */
public class Cell
{
    /**
     * The visual style of the cell (e.g., "retro", "modern").
     * This determines the appearance of the cell in the game UI.
     */
    private String style;

    /**
     * Indicates whether the cell contains a bomb.
     * {@code true} if the cell contains a bomb, {@code false} otherwise.
     */
    private boolean isBomb;

    /**
     * Indicates whether the cell is flagged by the player.
     * {@code true} if the cell is flagged, {@code false} otherwise.
     */
    private boolean isFlagged;

    /**
     * Indicates whether the cell has been revealed.
     * {@code true} if the cell is revealed, {@code false} otherwise.
     */
    private boolean isRevealed;

    /**
     * Indicates whether the cell is marked with a question mark.
     * {@code true} if the cell is questioned, {@code false} otherwise.
     */
    private boolean isQuestioned;

    /**
     * The number of bombs adjacent to this cell.
     * Represents how many neighboring cells contain bombs.
     */
    private int adjacentBombs;

    /**
     * The graphical button representation of the cell.
     * This button is used in the game UI to display and interact with the cell.
     */
    private Button button;

    /**
     * The {@link GameController} managing the game logic for this cell.
     * Links the cell to its controller to handle interactions and updates.
     */
    private GameController gameController;

    /**
     * The row index of the cell in the game grid.
     * Used to identify the cell's position vertically.
     */
    private int row;

    /**
     * The column index of the cell in the game grid.
     * Used to identify the cell's position horizontally.
     */
    private int column;


    /**
     * Constructs a new {@code Cell} with the specified parameters.
     * <p>
     * Initializes the cell's state, visual style, position, and graphical representation.
     * Attaches mouse click handlers to manage primary (left-click) and secondary (right-click) actions.
     *
     * @param style          The visual style of the cell (e.g., "retro", "modern").
     * @param isBomb         {@code true} if the cell contains a bomb, {@code false} otherwise.
     * @param gameController The {@link GameController} managing this cell.
     * @param row            The row index of the cell in the game grid.
     * @param column         The column index of the cell in the game grid.
     */
    public Cell(String style, boolean isBomb, GameController gameController, int row, int column)
    {
        setStyle(style);                     // Sets the visual style of the cell.
        setBomb(isBomb);                     // Indicates whether the cell contains a bomb.
        setGameController(gameController);   // Associates the cell with a GameController instance.
        setRow(row);                         // Sets the cell's row index in the grid.
        setColumn(column);                   // Sets the cell's column index in the grid.

        setFlagged(false);                   // Initializes the cell as not flagged.
        setRevealed(false);                  // Initializes the cell as not revealed.
        setQuestioned(false);                // Initializes the cell as not questioned.
        setAdjacentBombs(0);                 // Sets the initial number of adjacent bombs to zero.

        setButton(new Button());             // Creates the graphical button for the cell.
        getButton().setPrefSize(30, 30);     // Sets the button's preferred size.
        getButton().getStyleClass().add("cell-button"); // Applies the default style class to the button.

        // Adds a mouse click listener to the button to handle user interactions.
        getButton().setOnMouseClicked(event ->
        {
            if (getGameController().isFirstClick()) // Starts the game timer on the first click.
            {
                getGameController().startTimer();
                getGameController().setFirstClick(false);
            }

            switch (event.getButton()) // Handles left and right mouse clicks.
            {
                case MouseButton.PRIMARY -> reveal();      // Left-click reveals the cell.
                case MouseButton.SECONDARY -> toggleFlag(); // Right-click toggles the flag state.
            }
        });
    }

    /**
     * Toggles the state of the cell between flagged, questioned, and unmarked.
     * <p>
     * If the cell is not revealed, this method cycles through the following states when right-clicked:
     * <ul>
     *     <li>Flagged: Marks the cell with a flag icon.</li>
     *     <li>Questioned: Marks the cell with a question mark icon.</li>
     *     <li>Unmarked: Removes any icon from the cell.</li>
     * </ul>
     * Updates the remaining flags counter and checks the game's win condition after each change.
     */
    private void toggleFlag()
    {
        if (!isRevealed()) // Ensures the cell is not already revealed.
        {
            if (isFlagged()) // If flagged, change to questioned.
            {
                setFlagged(false);         // Unmark the cell as flagged.
                setQuestioned(true);       // Mark the cell as questioned.
                setIconForButton("question"); // Update the cell's icon to a question mark.
            } else if (isQuestioned()) // If questioned, remove the icon.
            {
                setQuestioned(false);      // Unmark the cell as questioned.
                removeIconFromButton();    // Remove any icon from the cell.
            } else // Otherwise, flag the cell.
            {
                setFlagged(true);          // Mark the cell as flagged.
                setIconForButton("flag");  // Update the cell's icon to a flag.
            }

            getGameController().updateRemainingFlagsCounter(); // Updates the displayed count of remaining flags.
            getGameController().checkWinCondition(); // Checks if the player has won the game.
        }
    }

    /**
     * Reveals the cell without suppressing the end-game logic.
     * <p>
     * This method is a convenience wrapper for the {@link #reveal(boolean)} method,
     * calling it with {@code suppressEndGame} set to {@code false}.
     */
    protected void reveal()
    {
        reveal(false); // Calls the overloaded reveal method without suppressing the end-game logic.
    }

    /**
     * Reveals the cell and updates its state and appearance based on its content.
     * <p>
     * If the cell is flagged or already revealed, the method does nothing. Otherwise:
     * <ul>
     *     <li>If the cell is a bomb, it triggers the game's end unless suppression is enabled.</li>
     *     <li>If the cell has adjacent bombs, it displays the count of adjacent bombs.</li>
     *     <li>If the cell has no adjacent bombs, it recursively reveals all neighboring cells.</li>
     * </ul>
     *
     * @param suppressEndGame {@code true} to prevent the game from ending if the cell contains a bomb,
     *                        {@code false} to allow the game to end normally.
     */
    public void reveal(boolean suppressEndGame)
    {
        if (isRevealed() || isFlagged()) // Skip if the cell is already revealed or flagged.
        {
            return;
        }

        if (isQuestioned()) // Remove the question mark if present.
        {
            setQuestioned(false); // Unmark the cell as questioned.
            removeIconFromButton(); // Remove the graphical question mark icon.
        }

        setRevealed(true); // Mark the cell as revealed.
        getButton().setDisable(true); // Disable interactions with the button.
        getButton().getStyleClass().add("cell-button-revealed"); // Apply the revealed cell style.

        if (isBomb()) // If the cell contains a bomb:
        {
            getButton().getStyleClass().add("cell-bomb-revealed"); // Apply the bomb-revealed style.

            silentBombReveal(suppressEndGame); // Reveal the bomb icon silently.
            if (!suppressEndGame) getGameController().endGame(false); // End the game if suppression is disabled.
        } else if (getAdjacentBombs() > 0) // If the cell has adjacent bombs:
        {
            getButton().setText(String.valueOf(getAdjacentBombs())); // Display the count of adjacent bombs.
            getButton().getStyleClass().add("cell-number-" + getAdjacentBombs()); // Apply the number style.
        } else // If the cell has no adjacent bombs:
        {
            getButton().getStyleClass().add("cell-button-revealed"); // Apply the revealed style.
            revealAdjacentCells(); // Recursively reveal all neighboring cells.
        }

        if (!suppressEndGame)
            getGameController().checkWinCondition(); // Check the win condition if suppression is disabled.
    }

    /**
     * Reveals the cell as a bomb without triggering end-game logic by default.
     * <p>
     * This method updates the cell's appearance to indicate that it contains a bomb.
     * If {@code suppressEndGame} is {@code true}, it applies an additional style
     * to visually distinguish the revealed bomb from others.
     *
     * @param suppressEndGame {@code true} to apply a distinct style for bomb-revealed cells
     *                        without ending the game, {@code false} otherwise.
     */
    public void silentBombReveal(boolean suppressEndGame)
    {
        getButton().setText(""); // Clears any text from the button.
        setIconForButton("bomb"); // Sets the bomb icon for the button.
        getButton().getStyleClass().add("cell-button-revealed"); // Applies the revealed cell style.

        // If suppression is enabled, add a specific style to highlight the bomb.
        if (suppressEndGame)
        {
            getButton().getStyleClass().add("cell-bomb-revealed");
        }
    }

    /**
     * Sets an icon for the cell's button using the specified image file.
     * <p>
     * This method loads an image based on the provided icon name and applies it to the button.
     * The image is dynamically resized to maintain its aspect ratio and fit within the button's dimensions.
     *
     * @param icon The name of the icon file (without the file extension) to be displayed on the button.
     *             The file should be located in the path: {@code /<style>/img/<icon>.png}.
     * @throws NullPointerException if the icon file cannot be found in the specified path.
     */
    public void setIconForButton(String icon)
    {
        // Load the image resource based on the icon name and the current style.
        ImageView imageView = new ImageView(Objects.requireNonNull(getClass().getResource("/" + getStyle() + "/img/" + icon + ".png")).toExternalForm());
        imageView.setPreserveRatio(true); // Maintain the aspect ratio of the image.

        // Add listeners to dynamically adjust the icon size based on button dimensions.
        getButton().widthProperty().addListener((observable, oldValue, newValue) -> adjustIconSize(imageView));
        getButton().heightProperty().addListener((observable, oldValue, newValue) -> adjustIconSize(imageView));

        adjustIconSize(imageView); // Initially adjust the icon size.

        getButton().setGraphic(imageView); // Set the image as the graphic for the button.
    }

    /**
     * Adjusts the size of the icon to fit within the button while maintaining its aspect ratio.
     * <p>
     * This method calculates the optimal size for the icon based on the button's current width
     * and height, ensuring the icon occupies 75% of the button's smaller dimension.
     *
     * @param imageView The {@link ImageView} representing the icon to be resized.
     */
    private void adjustIconSize(ImageView imageView)
    {
        // Calculate the size as 75% of the smaller dimension of the button.
        double iconSize = Math.min(getButton().getWidth(), getButton().getHeight()) * 0.75;

        // Set the calculated size for the icon's width and height.
        imageView.setFitWidth(iconSize);
        imageView.setFitHeight(iconSize);
    }

    /**
     * Removes the icon from the cell's button.
     * <p>
     * This method clears the graphic element of the button, effectively removing any
     * displayed icon (e.g., bomb, flag, or question mark).
     */
    private void removeIconFromButton()
    {
        getButton().setGraphic(null); // Clears the graphic associated with the button.
    }

    /**
     * Reveals all adjacent cells recursively if they do not contain bombs.
     * <p>
     * This method checks the neighboring cells of the current cell within the bounds of the game field.
     * If a neighboring cell is not revealed or flagged and does not contain adjacent bombs, it is revealed.
     * The process continues recursively for neighboring cells with no adjacent bombs.
     * <p>
     * Cells with adjacent bombs are revealed without further recursion.
     */
    private void revealAdjacentCells()
    {
        for (int row = getRow() - 1; row <= getRow() + 1; row++) // Loop through rows around the current cell.
        {
            for (int col = getColumn() - 1; col <= getColumn() + 1; col++) // Loop through columns around the current cell.
            {
                // Ensure the cell is within bounds and is not the current cell itself.
                if (getGameController().isInBounds(row, col) && !(row == getRow() && col == getColumn()))
                {
                    Cell neighbor = getGameController().getCellAt(row, col);

                    // Only proceed if the neighboring cell is not already revealed or flagged.
                    if (!neighbor.isRevealed() && !neighbor.isFlagged())
                    {
                        neighbor.setRevealed(true); // Mark the neighboring cell as revealed.
                        neighbor.getButton().setDisable(true); // Disable interaction with the cell's button.

                        if (neighbor.getAdjacentBombs() == 0) // If the neighboring cell has no adjacent bombs:
                        {
                            neighbor.getButton().getStyleClass().add("cell-button-revealed"); // Apply the revealed style.
                            neighbor.revealAdjacentCells(); // Recursively reveal its neighbors.
                        } else // If the neighboring cell has adjacent bombs:
                        {
                            neighbor.getButton().setText(String.valueOf(neighbor.getAdjacentBombs())); // Display the number of adjacent bombs.
                            neighbor.getButton().getStyleClass().add("cell-button-revealed"); // Apply the revealed style.
                            neighbor.getButton().getStyleClass().add("cell-number-" + neighbor.getAdjacentBombs()); // Apply a specific style for the number.
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks if this cell contains a bomb.
     *
     * @return {@code true} if the cell contains a bomb, {@code false} otherwise.
     */
    public boolean isBomb()
    {
        return isBomb;
    }

    /**
     * Sets whether this cell contains a bomb.
     *
     * @param bomb {@code true} to indicate the cell contains a bomb, {@code false} otherwise.
     */
    public void setBomb(boolean bomb)
    {
        isBomb = bomb;
    }

    /**
     * Checks if this cell is flagged by the player.
     *
     * @return {@code true} if the cell is flagged, {@code false} otherwise.
     */
    public boolean isFlagged()
    {
        return isFlagged;
    }

    /**
     * Sets whether this cell is flagged.
     *
     * @param flagged {@code true} to flag the cell, {@code false} to unflag it.
     */
    public void setFlagged(boolean flagged)
    {
        isFlagged = flagged;
    }

    /**
     * Checks if this cell has been revealed.
     *
     * @return {@code true} if the cell is revealed, {@code false} otherwise.
     */
    public boolean isRevealed()
    {
        return isRevealed;
    }

    /**
     * Sets whether this cell is revealed.
     *
     * @param revealed {@code true} to mark the cell as revealed, {@code false} otherwise.
     */
    public void setRevealed(boolean revealed)
    {
        isRevealed = revealed;
    }

    /**
     * Gets the number of bombs adjacent to this cell.
     *
     * @return The number of adjacent bombs.
     */
    public int getAdjacentBombs()
    {
        return adjacentBombs;
    }

    /**
     * Sets the number of bombs adjacent to this cell.
     *
     * @param adjacentBombs The number of adjacent bombs to set.
     */
    public void setAdjacentBombs(int adjacentBombs)
    {
        this.adjacentBombs = adjacentBombs;
    }

    /**
     * Gets the button associated with this cell.
     *
     * @return The {@link Button} associated with this cell.
     */
    public Button getButton()
    {
        return button;
    }

    /**
     * Sets the button associated with this cell.
     *
     * @param button The {@link Button} to associate with this cell.
     */
    public void setButton(Button button)
    {
        this.button = button;
    }

    /**
     * Gets the game controller associated with this cell.
     *
     * @return The {@link GameController} managing this cell.
     */
    public GameController getGameController()
    {
        return gameController;
    }

    /**
     * Sets the game controller managing this cell.
     *
     * @param gameController The {@link GameController} to associate with this cell.
     */
    public void setGameController(GameController gameController)
    {
        this.gameController = gameController;
    }

    /**
     * Gets the row index of this cell in the game grid.
     *
     * @return The row index of the cell.
     */
    public int getRow()
    {
        return row;
    }

    /**
     * Sets the row index of this cell in the game grid.
     *
     * @param row The row index to set.
     */
    public void setRow(int row)
    {
        this.row = row;
    }

    /**
     * Gets the column index of this cell in the game grid.
     *
     * @return The column index of the cell.
     */
    public int getColumn()
    {
        return column;
    }

    /**
     * Sets the column index of this cell in the game grid.
     *
     * @param column The column index to set.
     */
    public void setColumn(int column)
    {
        this.column = column;
    }

    /**
     * Checks if this cell is marked as "questioned" by the player.
     *
     * @return {@code true} if the cell is questioned, {@code false} otherwise.
     */
    public boolean isQuestioned()
    {
        return isQuestioned;
    }

    /**
     * Sets whether this cell is marked as "questioned".
     *
     * @param questioned {@code true} to mark the cell as questioned, {@code false} otherwise.
     */
    public void setQuestioned(boolean questioned)
    {
        isQuestioned = questioned;
    }

    /**
     * Gets the visual style associated with this cell (e.g., "retro", "modern").
     *
     * @return The visual style of the cell.
     */
    public String getStyle()
    {
        return style;
    }

    /**
     * Sets the visual style associated with this cell (e.g., "retro", "modern").
     *
     * @param style The visual style to set.
     */
    public void setStyle(String style)
    {
        this.style = style;
    }
}
