package htl.steyr.javafx_minesweeper_tplatzer.model;

import htl.steyr.javafx_minesweeper_tplatzer.service.FileManager;

import java.io.*;

/**
 * The {@code UserDataManager} class handles the saving and loading of user data for the Minesweeper game.
 * <p>
 * This class provides methods to serialize and deserialize {@link UserData} objects to a file.
 * The data is stored in a binary format to maintain user-specific information such as best times and usernames.
 */
public class UserDataManager
{
    /**
     * The name of the file where user data is stored.
     */
    private static final String FILE_NAME = "user_data.dat";

    /**
     * Saves the given {@link UserData} object to a file.
     * <p>
     * This method serializes the user data and writes it to a predefined file location.
     * If the file does not exist, it is created.
     *
     * @param userData The {@link UserData} object to save.
     */
    public static void saveUserData(UserData userData)
    {
        File saveFile = FileManager.getSaveFile(FILE_NAME);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(saveFile)))
        {
            oos.writeObject(userData); // Serializes and writes the user data to the file.
        } catch (Exception e)
        {
            System.err.println(e.getMessage()); // Logs an error message if the operation fails.
        }
    }

    /**
     * Loads user data from a file.
     * <p>
     * This method attempts to deserialize a {@link UserData} object from a predefined file location.
     * If the file does not exist or an error occurs during deserialization, a new {@code UserData} object is returned.
     *
     * @return The {@link UserData} object loaded from the file, or a new {@code UserData} object if the operation fails.
     */
    public static UserData loadUserData()
    {
        File saveFile = FileManager.getSaveFile(FILE_NAME);
        if (!saveFile.exists())
        {
            return new UserData(); // Returns a new UserData object if the file does not exist.
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile)))
        {
            return (UserData) ois.readObject(); // Deserializes and returns the user data.
        } catch (Exception e)
        {
            System.err.println(e.getMessage()); // Logs an error message if the operation fails.
            return new UserData(); // Returns a new UserData object in case of an error.
        }
    }
}
