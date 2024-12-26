package htl.steyr.javafx_minesweeper_tplatzer;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class MusicPlayer
{
    private Clip clip;

    public void playMusic(String fileName, float volume)
    {
        File audioFile = new File(Objects.requireNonNull(getClass().getResource("/sfx/" + fileName)).toExternalForm().replace("file:", ""));
        try
        {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            setVolume(volume);

            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void playMusicShort(String fileName, float volume)
    {
        try
        {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(Objects.requireNonNull(getClass().getResource("/sfx/" + fileName)).toExternalForm().replace("file:", "")));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            setVolume(volume);

            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
        {
            System.out.println(e.getMessage());
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
            e.printStackTrace();
            return 0.0;
        }
    }
}
