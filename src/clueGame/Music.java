package clueGame;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class Music {

    private final String filePath;
    private boolean initialPlay = false;
    private Clip clip;
    private long clipPosition;

    public Music(String filePath) {
        this.filePath = filePath;
    }

    public Music() {
        this.filePath = "data/music.wav";
    }

//    public static void main(String[] args) {
//        Music musicPlayer = new Music();
//        musicPlayer.playAndLoopMusic("data/music.wav");
//
//        // Example usage of pause and resume
////        try {
////            Thread.sleep(5000); // Pause after 5 seconds
////            musicPlayer.pause();
////            Thread.sleep(5000); // Resume after another 5 seconds
////            musicPlayer.resume();
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
//    }

    public void play() {
        try {
            File musicFile = new File(filePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicFile);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            // Set loop count to LOOP_CONTINUOUSLY for indefinite looping
            clip.loop(Clip.LOOP_CONTINUOUSLY);

            // Start playing the music
            clip.start();
            initialPlay = true;

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();
            clip = null;
        }
    }

    public void pause() {
        if (!initialPlay) return;
        if (clip != null && clip.isRunning()) {
            clipPosition = clip.getMicrosecondPosition();
            clip.stop();
        }
    }

    public void resume() {
        if (!initialPlay) play();
        else if (clip != null && !clip.isRunning()) {
            clip.setMicrosecondPosition(clipPosition);
            clip.start();
        }
    }

    public boolean isPlaying() {
        return clip != null && clip.isRunning();
    }
}
