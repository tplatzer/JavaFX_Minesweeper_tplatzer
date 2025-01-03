package htl.steyr.javafx_minesweeper_tplatzer;

import java.io.*;

public class UserDataManager
{
    private static final String FILE_NAME = "user_data.dat";

    public static void saveUserData(UserData userData)
    {
        File saveFile = FileManager.getSaveFile(FILE_NAME);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(saveFile)))
        {
            oos.writeObject(userData);
        } catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
    }

    public static UserData loadUserData()
    {
        File saveFile = FileManager.getSaveFile(FILE_NAME);
        if (!saveFile.exists())
        {
            return new UserData();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile)))
        {
            return (UserData) ois.readObject();
        } catch (Exception e)
        {
            System.err.println(e.getMessage());
            return new UserData();
        }
    }
}
