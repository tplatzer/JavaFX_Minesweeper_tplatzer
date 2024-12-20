module htl.steyr.javafx_minesweeper_tplatzer {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.desktop;

    opens htl.steyr.javafx_minesweeper_tplatzer to javafx.fxml;
    exports htl.steyr.javafx_minesweeper_tplatzer;
}