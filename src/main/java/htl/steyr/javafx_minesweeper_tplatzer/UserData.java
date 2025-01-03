package htl.steyr.javafx_minesweeper_tplatzer;

import java.io.Serial;
import java.io.Serializable;

public class UserData implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;
    private int beginnerBestTime;
    private int advancedBestTime;
    private int proBestTime;
    private String username;

    public UserData()
    {
        setBeginnerBestTime(Integer.MAX_VALUE);
        setAdvancedBestTime(Integer.MAX_VALUE);
        setProBestTime(Integer.MAX_VALUE);
        setUsername("Default_Username");
    }

    public UserData(String username)
    {
        super();
        setUsername(username);
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

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }
}
