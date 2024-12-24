package htl.steyr.javafx_minesweeper_tplatzer;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class MusicPlayer
{
    private Clip clip;

    public void playMusic(String fileName) {
        File audioFile = new File(Objects.requireNonNull(getClass().getResource("/sfx/" + fileName)).toExternalForm());
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println(e.getMessage());
        }
    }

    public void playMusicShort(String fileName)
    {
        try
        {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(Objects.requireNonNull(getClass().getResource("/sfx/" + fileName)).toExternalForm()));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void stopMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
}
