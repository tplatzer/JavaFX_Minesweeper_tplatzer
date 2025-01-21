package htl.steyr.javafx_minesweeper_tplatzer.service;

import java.io.File;

/**
 * The {@code FileManager} class handles file management for the Minesweeper application.
 * <p>
 * This includes determining the appropriate save file location based on the operating system
 * and ensuring the application data folder exists.
 */
public class FileManager
{
    /**
     * The name of the application, used to create a dedicated folder in the system's application data directory.
     */
    private static final String APP_NAME = "Bomb-Disposal-Simulator";

    /**
     * Retrieves the file object for the specified file name located within the application's data folder.
     * <p>
     * If the application's data folder does not exist, it is created automatically.
     *
     * @param fileName The name of the file to retrieve or create in the application's data folder.
     * @return A {@link File} object representing the specified file within the application's data folder.
     */
    public static File getSaveFile(String fileName)
    {
        File appDataFolder = getAppDataFolder(); // Retrieve the application data folder.
        return new File(appDataFolder, fileName); // Return the file object within the data folder.
    }

    /**
     * Retrieves the application data folder for the current operating system.
     * <p>
     * The method determines the appropriate folder based on the operating system:
     * <ul>
     *     <li>Windows: Uses the {@code LOCALAPPDATA} environment variable.</li>
     *     <li>macOS: Uses the {@code ~/Library/Application Support} directory.</li>
     *     <li>Linux/Unix: Uses the {@code ~/.config} directory.</li>
     * </ul>
     * If the application folder does not exist, it is created automatically.
     *
     * @return A {@link File} object representing the application's data folder.
     * @throws IllegalStateException If the {@code LOCALAPPDATA} environment variable is not set on Windows.
     */
    public static File getAppDataFolder()
    {
        String osName = System.getProperty("os.name").toLowerCase(); // Detect the operating system.
        String appDataPath;

        if (osName.contains("win")) // Windows-specific path.
        {
            appDataPath = System.getenv("LOCALAPPDATA");
            if (appDataPath == null) // Ensure LOCALAPPDATA is set.
            {
                throw new IllegalStateException("LOCALAPPDATA environment variable is not set");
            }
        } else if (osName.contains("mac")) // macOS-specific path.
        {
            appDataPath = System.getProperty("user.home") + "/Library/Application Support";
        } else // Default to Linux/Unix path.
        {
            appDataPath = System.getProperty("user.home") + "/.config";
        }

        File appFolder = new File(appDataPath, APP_NAME); // Create a path for the application's folder.
        if (!appFolder.exists()) // Ensure the folder exists.
        {
            appFolder.mkdirs();
        }

        return appFolder; // Return the application's data folder.
    }
}
