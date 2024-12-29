package htl.steyr.javafx_minesweeper_tplatzer;

import java.io.Serial;
import java.io.Serializable;

public class BestTimes implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;
    private int beginnerBestTime;
    private int advancedBestTime;
    private int proBestTime;

    public BestTimes()
    {
        setBeginnerBestTime(Integer.MAX_VALUE);
        setAdvancedBestTime(Integer.MAX_VALUE);
        setProBestTime(Integer.MAX_VALUE);
    }

    public int getBeginnerBestTime()
    {
        return beginnerBestTime;
    }

    public void setBeginnerBestTime(int beginnerBestTime)
    {
        this.beginnerBestTime = beginnerBestTime;
    }

    public int getAdvancedBestTime()
    {
        return advancedBestTime;
    }

    public void setAdvancedBestTime(int advancedBestTime)
    {
        this.advancedBestTime = advancedBestTime;
    }

    public int getProBestTime()
    {
        return proBestTime;
    }

    public void setProBestTime(int proBestTime)
    {
        this.proBestTime = proBestTime;
    }
}
