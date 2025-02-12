/**
 * Defines the {@code htl.steyr.javafx_minesweeper_tplatzer} module.
 * <p>
 * This module contains the Minesweeper application, implemented using JavaFX.
 * It specifies dependencies required for UI rendering, external controls,
 * HTTP communication, and desktop functionalities.
 * </p>
 *
 * <h2>Required Modules:</h2>
 * <ul>
 *     <li>{@code javafx.controls} - Provides JavaFX UI components.</li>
 *     <li>{@code org.controlsfx.controls} - Includes additional JavaFX UI controls.</li>
 *     <li>{@code java.desktop} - Enables AWT and Swing functionalities for audio and file handling.</li>
 *     <li>{@code java.net.http} - Allows communication with external web services.</li>
 * </ul>
 *
 * <h2>Exported Packages:</h2>
 * <ul>
 *     <li>{@link htl.steyr.javafx_minesweeper_tplatzer.app} - Contains the main application entry points.</li>
 *     <li>{@link htl.steyr.javafx_minesweeper_tplatzer.controller} - Handles user interactions and UI control.</li>
 *     <li>{@link htl.steyr.javafx_minesweeper_tplatzer.model} - Manages data structures and persistence.</li>
 *     <li>{@link htl.steyr.javafx_minesweeper_tplatzer.service} - Provides auxiliary services like audio and networking.</li>
 * </ul>
 */
module htl.steyr.javafx_minesweeper_tplatzer
{
    requires javafx.controls; // Provides JavaFX UI components.

    requires org.controlsfx.controls; // Includes extended JavaFX UI controls.
    requires java.desktop; // Provides AWT and Swing functionalities for file and audio handling.
    requires java.net.http; // Enables HTTP communication for leaderboard interactions.

    exports htl.steyr.javafx_minesweeper_tplatzer.app; // Exports application entry points.
    exports htl.steyr.javafx_minesweeper_tplatzer.controller; // Exports UI controllers for handling interactions.
    exports htl.steyr.javafx_minesweeper_tplatzer.model; // Exports data structures and persistence logic.
    exports htl.steyr.javafx_minesweeper_tplatzer.service; // Exports auxiliary services like audio and networking.
}
