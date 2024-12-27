package htl.steyr.javafx_minesweeper_tplatzer;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;

import java.util.Objects;

public class Cell
{
    private boolean isBomb;
    private boolean isFlagged;
    private boolean isRevealed;
    private boolean isQuestioned;
    private int adjacentBombs;
    private Button button;
    private GameController controller;
    private int row;
    private int column;

    public Cell(boolean isBomb, GameController controller, int row, int column)
    {
        setBomb(isBomb);
        setController(controller);
        setRow(row);
        setColumn(column);

        setFlagged(false);
        setRevealed(false);
        setQuestioned(false);
        setAdjacentBombs(0);

        setButton(new Button());
        getButton().setPrefSize(30, 30);
        getButton().getStyleClass().add("cell-button");

        getButton().setOnMouseClicked(event ->
        {
            switch (event.getButton())
            {
                case MouseButton.PRIMARY -> reveal();
                case MouseButton.SECONDARY -> toggleFlag();
            }
        });
    }

    private void toggleFlag()
    {
        if (!isRevealed())
        {
            if (isFlagged())
            {
                setFlagged(false);
                setQuestioned(true);
                setIconForButton("question");
            } else if (isQuestioned())
            {
                setQuestioned(false);
                removeIconFromButton();
            } else
            {
                setFlagged(true);
                setIconForButton("flag");
            }

            getController().updateRemainingFlagsCounter();
            getController().checkWinCondition();
        }
    }

    protected void reveal()
    {
        reveal(false);
    }

    protected void reveal(boolean suppressEndGame)
    {
        if (isRevealed() || isFlagged())
        {
            return;
        }

        if (isQuestioned())
        {
            setQuestioned(false);
            removeIconFromButton();
        }

        setRevealed(true);
        getButton().setDisable(true);
        getButton().getStyleClass().add("cell-button-revealed");

        if (isBomb())
        {
            silentBombReveal();
            if (!suppressEndGame) getController().endGame(false);
        } else if (getAdjacentBombs() > 0)
        {
            getButton().setText(String.valueOf(getAdjacentBombs()));
            getButton().getStyleClass().add("cell-number-" + getAdjacentBombs());
        } else
        {
            getButton().getStyleClass().add("cell-button-revealed");
            revealAdjacentCells();
        }

        getController().checkWinCondition();
    }

    protected void silentBombReveal()
    {
        getButton().setText("");
        setIconForButton("bomb");
    }

    protected void setIconForButton(String icon)
    {
        ImageView imageView = new ImageView(Objects.requireNonNull(getClass().getResource("/img/" + icon + ".png")).toExternalForm());
        imageView.setPreserveRatio(true);

        getButton().widthProperty().addListener((observable, oldValue, newValue) -> adjustIconSize(imageView));
        getButton().heightProperty().addListener((observable, oldValue, newValue) -> adjustIconSize(imageView));

        adjustIconSize(imageView);

        getButton().setGraphic(imageView);
    }

    private void adjustIconSize(ImageView imageView)
    {
        double iconSize = Math.min(getButton().getWidth(), getButton().getHeight()) * 0.75;
        imageView.setFitWidth(iconSize);
        imageView.setFitHeight(iconSize);
    }

    private void removeIconFromButton()
    {
        getButton().setGraphic(null);
    }

    private void revealAdjacentCells()
    {
        for (int row = getRow() - 1; row <= getRow() + 1; row++)
        {
            for (int col = getColumn() - 1; col <= getColumn() + 1; col++)
            {
                if (getController().isInBounds(row, col) && !(row == getRow() && col == getColumn()))
                {
                    Cell neighbor = getController().getCellAt(row, col);

                    if (!neighbor.isRevealed() && !neighbor.isFlagged())
                    {
                        neighbor.setRevealed(true);
                        neighbor.getButton().setDisable(true);

                        if (neighbor.getAdjacentBombs() == 0)
                        {
                            neighbor.getButton().getStyleClass().add("cell-button-revealed");
                            neighbor.revealAdjacentCells();
                        } else
                        {
                            neighbor.getButton().setText(String.valueOf(neighbor.getAdjacentBombs()));
                            neighbor.getButton().getStyleClass().add("cell-button-revealed");
                            neighbor.getButton().getStyleClass().add("cell-number-" + neighbor.getAdjacentBombs());
                        }
                    }
                }
            }
        }
    }

    public boolean isBomb()
    {
        return isBomb;
    }

    public void setBomb(boolean bomb)
    {
        isBomb = bomb;
    }

    public boolean isFlagged()
    {
        return isFlagged;
    }

    public void setFlagged(boolean flagged)
    {
        isFlagged = flagged;
    }

    public boolean isRevealed()
    {
        return isRevealed;
    }

    public void setRevealed(boolean revealed)
    {
        isRevealed = revealed;
    }

    public int getAdjacentBombs()
    {
        return adjacentBombs;
    }

    public void setAdjacentBombs(int adjacentBombs)
    {
        this.adjacentBombs = adjacentBombs;
    }

    public Button getButton()
    {
        return button;
    }

    public void setButton(Button button)
    {
        this.button = button;
    }

    public GameController getController()
    {
        return controller;
    }

    public void setController(GameController controller)
    {
        this.controller = controller;
    }

    public int getRow()
    {
        return row;
    }

    public void setRow(int row)
    {
        this.row = row;
    }

    public int getColumn()
    {
        return column;
    }

    public void setColumn(int column)
    {
        this.column = column;
    }

    public boolean isQuestioned()
    {
        return isQuestioned;
    }

    public void setQuestioned(boolean questioned)
    {
        isQuestioned = questioned;
    }
}
