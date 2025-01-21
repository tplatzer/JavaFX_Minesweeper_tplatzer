package htl.steyr.javafx_minesweeper_tplatzer.service;

import htl.steyr.javafx_minesweeper_tplatzer.controller.Controller;

import javax.sound.sampled.*;
import java.io.*;
import java.util.Objects;

/**
 * The {@code MusicPlayer} class is responsible for handling the playback of background music and sound effects.
 * <p>
 * It supports continuous playback, single playback, and dynamic volume adjustment for audio files.
 */
public class MusicPlayer
{
    /**
     * The audio clip used for playback.
     */
    private Clip clip;


    /**
     * Plays a music file in a continuous loop.
     * <p>
     * The music file is loaded from the style-specific folder under {@code /sfx/}.
     * It validates the input file, sets the volume, and starts playback in a loop.
     *
     * @param fileName The name of the audio file to play (e.g., "background-music.wav").
     * @param volume   The desired volume level in decibels (e.g., -30.0f for reduced volume).
     * @param style    The visual style that determines the folder structure (e.g., "retro", "modern").
     */
    public void playMusic(String fileName, float volume, String style)
    {
        try (InputStream audioStream = getClass().getResourceAsStream("/" + style + "/sfx/" + fileName))
        {
            // Validates that the input stream is not null, throws an exception if the file is not found.
            Controller.checkIfInputStreamIsNotNull(audioStream, fileName);

            // Reads the audio input stream and initializes the audio clip.
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(audioStream));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            // Adjusts the volume to the specified level.
            setVolume(volume);

            // Starts playback and loops the audio continuously.
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
        {
            // Logs the error message if an exception occurs during file reading or playback.
            System.err.println(e.getMessage());
        }
    }

    /**
     * Plays a music file once without looping.
     * <p>
     * The music file is loaded from the style-specific folder under {@code /sfx/}.
     * It validates the input file, sets the volume, and starts playback for a single iteration.
     *
     * @param fileName The name of the audio file to play (e.g., "click-sound.wav").
     * @param volume   The desired volume level in decibels (e.g., -20.0f for reduced volume).
     * @param style    The visual style that determines the folder structure (e.g., "retro", "modern").
     */
    public void playMusicOnce(String fileName, float volume, String style)
    {
        try (InputStream audioStream = getClass().getResourceAsStream("/" + style + "/sfx/" + fileName))
        {
            // Validates that the input stream is not null, throws an exception if the file is not found.
            Controller.checkIfInputStreamIsNotNull(audioStream, fileName);

            // Reads the audio input stream and initializes the audio clip.
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(audioStream));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            // Adjusts the volume to the specified level.
            setVolume(volume);

            // Starts playback for a single iteration.
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
        {
            // Logs the error message if an exception occurs during file reading or playback.
            System.err.println(e.getMessage());
        }
    }

    /**
     * Stops the currently playing music and releases resources.
     * <p>
     * This method checks if the music clip is currently running. If so, it stops playback and
     * closes the clip to release system resources.
     */
    public void stopMusic()
    {
        if (clip != null && clip.isRunning())
        {
            clip.stop();  // Stops the music playback.
            clip.close(); // Closes the clip to release resources.
        }
    }

    /**
     * Adjusts the playback volume of the current audio clip.
     * <p>
     * This method ensures that the provided volume is within the valid range supported by the audio clip.
     * If the audio clip supports volume control, the method sets the volume to the specified value
     * after clamping it within the allowed range.
     *
     * @param volume The desired volume level, where the range is determined by the audio clip's
     *               {@link FloatControl.Type#MASTER_GAIN}.
     */
    private void setVolume(float volume)
    {
        // Check if the audio clip is valid and supports volume control.
        if (clip != null && clip.isControlSupported(FloatControl.Type.MASTER_GAIN))
        {
            // Retrieve the volume control for the clip.
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            // Get the minimum and maximum volume levels supported by the clip.
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();

            // Clamp the provided volume within the allowed range.
            volume = Math.max(min, Math.min(max, volume));

            // Set the volume to the clamped value.
            volumeControl.setValue(volume);
        }
    }

    /**
     * Retrieves the duration of a sound effect in seconds.
     * <p>
     * This method calculates the duration of an audio file by dividing its total frame count
     * by the audio format's frame rate. If any error occurs while accessing the audio file,
     * the method logs the error and returns a duration of {@code 0.0}.
     *
     * @param fileName The name of the sound effect file (without extension).
     * @param style    The visual/audio style folder where the sound effect is located.
     * @return The duration of the sound effect in seconds, or {@code 0.0} if an error occurs.
     */
    public static double getSoundEffectDuration(String fileName, String style)
    {
        try
        {
            // Retrieve the audio input stream for the specified sound effect.
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                    Objects.requireNonNull(MusicPlayer.class.getResource("/" + style + "/sfx/" + fileName + ".wav"))
            );

            // Get the audio format and frame count of the sound effect.
            AudioFormat format = audioInputStream.getFormat();
            long frames = audioInputStream.getFrameLength();

            // Calculate and return the duration in seconds.
            return frames / format.getFrameRate();
        } catch (Exception e)
        {
            // Log the error message and return 0.0 for the duration.
            System.err.println(e.getMessage());
            return 0.0;
        }
    }
}
