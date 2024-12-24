package htl.steyr.javafx_minesweeper_tplatzer;

import javafx.scene.control.Button;

public class Cell {
    private boolean isBomb;
    private boolean isFlagged;
    private boolean isRevealed;
    private Button button;

    public Cell(boolean isBomb) {
        this.isBomb = isBomb;
        this.isFlagged = false;
        this.isRevealed = false;
        this.button = new Button();
        this.button.setPrefSize(30, 30);
        this.button.getStyleClass().add("cell-button");
        this.button.setFocusTraversable(false);
    }

    public boolean isBomb() {
        return isBomb;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }

    public Button getButton() {
        return button;
    }
}
