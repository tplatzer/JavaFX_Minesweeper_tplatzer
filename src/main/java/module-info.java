module htl.steyr.javafx_minesweeper_tplatzer {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.desktop;
    requires java.net.http;

    exports htl.steyr.javafx_minesweeper_tplatzer.app;
    exports htl.steyr.javafx_minesweeper_tplatzer.controller;
    exports htl.steyr.javafx_minesweeper_tplatzer.model;
    exports htl.steyr.javafx_minesweeper_tplatzer.service;
}
