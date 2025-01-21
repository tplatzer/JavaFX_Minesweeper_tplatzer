package htl.steyr.javafx_minesweeper_tplatzer.model;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents user data for the Minesweeper game, including username and best times for different difficulties.
 * <p>
 * This class implements {@link Serializable} to allow user data to be saved and loaded.
 */
public class UserData implements Serializable
{
    /**
     * Serial version UID for ensuring compatibility during the serialization and deserialization process.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Stores the best time (in seconds) achieved by the user for the "Beginner" difficulty level.
     * <p>
     * The default value is {@code Integer.MAX_VALUE}, indicating no time has been recorded yet.
     */
    private int beginnerBestTime;

    /**
     * Stores the best time (in seconds) achieved by the user for the "Advanced" difficulty level.
     * <p>
     * The default value is {@code Integer.MAX_VALUE}, indicating no time has been recorded yet.
     */
    private int advancedBestTime;

    /**
     * Stores the best time (in seconds) achieved by the user for the "Pro" difficulty level.
     * <p>
     * The default value is {@code Integer.MAX_VALUE}, indicating no time has been recorded yet.
     */
    private int proBestTime;

    /**
     * The username associated with this user's data.
     * <p>
     * The default value is "Default_Username".
     */
    private String username;


    /**
     * Constructs a new {@code UserData} instance with default values.
     * <p>
     * Initializes the user's game data with default best times and a default username.
     * This constructor is typically used to create a new user profile with no prior game data.
     * <p>
     * Default values:
     * <ul>
     *     <li>{@code beginnerBestTime}: {@code Integer.MAX_VALUE} (indicating no recorded time)</li>
     *     <li>{@code advancedBestTime}: {@code Integer.MAX_VALUE} (indicating no recorded time)</li>
     *     <li>{@code proBestTime}: {@code Integer.MAX_VALUE} (indicating no recorded time)</li>
     *     <li>{@code username}: "Default_Username"</li>
     * </ul>
     */
    public UserData()
    {
        setBeginnerBestTime(Integer.MAX_VALUE); // Initializes beginner best time to no recorded time.
        setAdvancedBestTime(Integer.MAX_VALUE); // Initializes advanced best time to no recorded time.
        setProBestTime(Integer.MAX_VALUE);      // Initializes pro best time to no recorded time.
        setUsername("Default_Username");        // Sets the default username.
    }

    /**
     * Constructs a new {@code UserData} instance with the specified username.
     * <p>
     * Initializes the user's game data with default best times and the provided username.
     * This constructor is typically used to create a user profile with a custom username.
     * <p>
     * Default values:
     * <ul>
     *     <li>{@code beginnerBestTime}: {@code Integer.MAX_VALUE} (indicating no recorded time)</li>
     *     <li>{@code advancedBestTime}: {@code Integer.MAX_VALUE} (indicating no recorded time)</li>
     *     <li>{@code proBestTime}: {@code Integer.MAX_VALUE} (indicating no recorded time)</li>
     * </ul>
     *
     * @param username The username to associate with this user data.
     */
    public UserData(String username)
    {
        super();                // Calls the parent constructor (if necessary).
        setUsername(username);  // Sets the username for this user data.
    }

    /**
     * Returns the best time for the beginner difficulty level.
     *
     * @return The best time for the beginner difficulty, in seconds.
     */
    public int getBeginnerBestTime()
    {
        return beginnerBestTime;
    }

    /**
     * Sets the best time for the beginner difficulty level.
     *
     * @param beginnerBestTime The best time to set for the beginner difficulty, in seconds.
     */
    public void setBeginnerBestTime(int beginnerBestTime)
    {
        this.beginnerBestTime = beginnerBestTime;
    }

    /**
     * Returns the best time for the advanced difficulty level.
     *
     * @return The best time for the advanced difficulty, in seconds.
     */
    public int getAdvancedBestTime()
    {
        return advancedBestTime;
    }

    /**
     * Sets the best time for the advanced difficulty level.
     *
     * @param advancedBestTime The best time to set for the advanced difficulty, in seconds.
     */
    public void setAdvancedBestTime(int advancedBestTime)
    {
        this.advancedBestTime = advancedBestTime;
    }

    /**
     * Returns the best time for the pro difficulty level.
     *
     * @return The best time for the pro difficulty, in seconds.
     */
    public int getProBestTime()
    {
        return proBestTime;
    }

    /**
     * Sets the best time for the pro difficulty level.
     *
     * @param proBestTime The best time to set for the pro difficulty, in seconds.
     */
    public void setProBestTime(int proBestTime)
    {
        this.proBestTime = proBestTime;
    }

    /**
     * Returns the username associated with this user data.
     *
     * @return The username of the player.
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Sets the username for this user data.
     *
     * @param username The new username to set.
     * @return The updated {@code UserData} instance for method chaining.
     */
    public UserData setUsername(String username)
    {
        this.username = username;
        return this;
    }
}
