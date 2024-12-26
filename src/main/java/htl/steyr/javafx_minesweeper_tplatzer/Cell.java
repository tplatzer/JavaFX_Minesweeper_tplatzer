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
    private int adjacentBombs;
    private Button button;
    private GameController controller;
    private int row;
    private int col;

    public Cell (boolean isBomb, GameController controller, int row, int col)
    {
        setBomb(isBomb);
        setController(controller);
        setRow(row);
        setCol(col);

        setFlagged(false);
        setRevealed(false);
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
            setFlagged(!isFlagged());

            if (isFlagged())
            {
                setIconForButton("flag");
            } else
            {
                removeIconFromButton();
            }

            getController().updateRemainingFlagsCounter();
            getController().checkWinCondition();
        }
    }

    private void reveal()
    {
        if (isRevealed() || isFlagged())
        {
            return;
        }

        setRevealed(true);

        if (isBomb())
        {
            silentReveal();

            getController().endGame(false);
        } else if (getAdjacentBombs() > 0)
        {
            getButton().setText(String.valueOf(getAdjacentBombs()));
        } else
        {
            getButton().setVisible(false);
            revealAdjacentCells();
        }

        getController().checkWinCondition();
    }

    private void silentReveal()
    {
        getButton().setText("");
        setIconForButton("bomb");
    }

    private void setIconForButton(String icon)
    {
        ImageView imageView = new ImageView(Objects.requireNonNull(getClass().getResource("/img/" + icon + ".png")).toExternalForm());
        double iconSize = getButton().getPrefWidth() * 0.9;
        imageView.setFitWidth(iconSize);
        imageView.setFitHeight(iconSize);
        imageView.setPreserveRatio(true);
        getButton().setGraphic(imageView);
    }

    private void removeIconFromButton()
    {
        getButton().setGraphic(null);
    }

    private void revealAdjacentCells()
    {
        for (int row = getRow() - 1; row <= getRow() + 1; row++)
        {
            for (int col = getCol() - 1; col <= getCol() + 1; col++)
            {
                if (getController().isInBounds(row, col) && !(row == getRow() && col == getCol()))
                {
                    Cell neighbor = getController().getCellAt(row, col);

                    if (!neighbor.isRevealed() && !neighbor.isFlagged())
                    {
                        neighbor.setRevealed(true);

                        if (neighbor.getAdjacentBombs() == 0)
                        {
                            neighbor.getButton().setVisible(false);
                            neighbor.revealAdjacentCells();
                        } else
                        {
                            neighbor.getButton().setText(String.valueOf(neighbor.getAdjacentBombs()));
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

    public int getCol()
    {
        return col;
    }

    public void setCol(int col)
    {
        this.col = col;
    }
}
