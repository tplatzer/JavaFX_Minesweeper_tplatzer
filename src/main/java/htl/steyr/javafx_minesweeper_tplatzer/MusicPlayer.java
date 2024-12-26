package htl.steyr.javafx_minesweeper_tplatzer;

import javax.sound.sampled.*;
import java.io.*;
import java.util.Objects;

public class MusicPlayer
{
    private Clip clip;

    public void playMusic(String fileName, float volume)
    {
        try (InputStream audioStream = getClass().getResourceAsStream("/sfx/" + fileName))
        {
            Controller.checkIfInputStreamIsNotNull(audioStream, fileName);

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(audioStream));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            setVolume(volume);

            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
        {
            System.err.println(e.getMessage());
        }
    }

    public void playMusicOnce(String fileName, float volume)
    {
        try (InputStream audioStream = getClass().getResourceAsStream("/sfx/" + fileName))
        {
            Controller.checkIfInputStreamIsNotNull(audioStream, fileName);

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(audioStream));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            setVolume(volume);

            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
        {
            System.err.println(e.getMessage());
        }
    }

    public void stopMusic()
    {
        if (clip != null && clip.isRunning())
        {
            clip.stop();
            clip.close();
        }
    }

    private void setVolume(float volume)
    {
        if (clip != null && clip.isControlSupported(FloatControl.Type.MASTER_GAIN))
        {
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            volume = Math.max(min, Math.min(max, volume));
            volumeControl.setValue(volume);
        }
    }

    public static double getSoundEffectDuration(String fileName)
    {
        try
        {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Objects.requireNonNull(MusicPlayer.class.getResource("/sfx/" + fileName + ".wav")));
            AudioFormat format = audioInputStream.getFormat();
            long frames = audioInputStream.getFrameLength();
            return frames / format.getFrameRate();
        } catch (Exception e)
        {
            System.err.println(e.getMessage());
            return 0.0;
        }
    }
}
