package htl.steyr.javafx_minesweeper_tplatzer;

import java.io.File;

public class FileManager
{
    private static final String APP_NAME = "Bomb-Disposal-Simulator";

    public static File getSaveFile(String fileName)
    {
        File appDataFolder = getAppDataFolder();
        return new File(appDataFolder, fileName);
    }

    public static File getAppDataFolder()
    {
        String osName = System.getProperty("os.name").toLowerCase();
        String appDataPath;

        if (osName.contains("win"))
        {
            appDataPath = System.getenv("LOCALAPPDATA");
            if (appDataPath == null)
            {
                throw new IllegalStateException("LOCALAPPDATA environment variable is not set");
            }
        } else if (osName.contains("mac"))
        {
            appDataPath = System.getProperty("user.home") + "/Library/Application Support";
        } else
        {
            appDataPath = System.getProperty("user.home") + "/.config";
        }

        File appFolder = new File(appDataPath, APP_NAME);
        if (!appFolder.exists())
        {
            appFolder.mkdirs();
        }

        return appFolder;
    }
}
