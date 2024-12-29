package htl.steyr.javafx_minesweeper_tplatzer;

import java.io.*;

public class BestTimeManager
{
    private static final String FILE_NAME = "best_times.dat";

    public static void saveBestTimes(BestTimes bestTimes)
    {
        File saveFile = FileManager.getSaveFile(FILE_NAME);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(saveFile)))
        {
            oos.writeObject(bestTimes);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static BestTimes loadBestTimes()
    {
        File saveFile = FileManager.getSaveFile(FILE_NAME);
        if (!saveFile.exists())
        {
            return new BestTimes();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile)))
        {
            return (BestTimes) ois.readObject();
        } catch (Exception e)
        {
            e.printStackTrace();
            return new BestTimes();
        }
    }
}
