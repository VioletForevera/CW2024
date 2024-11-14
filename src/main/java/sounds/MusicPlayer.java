package sounds;

import java.io.File;
import javax.sound.sampled.*;

public class MusicPlayer {
    private Clip clip;
    private FloatControl volumeControl;

    public MusicPlayer(String filePath) {
        try {
            File soundFile = new File(filePath);
            if (!soundFile.exists()) {
                System.err.println("File not found at path: " + filePath);
                throw new IllegalArgumentException("File not found: " + filePath);
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);

            // Check if volume control is supported
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            } else {
                System.err.println("Volume control not supported.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Play music and loop
    public void play() {
        if (clip != null) {
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    // Stop music and reset playback position
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.setFramePosition(0); // Reset playback position
        }
    }

    // Set volume (0.0f - 1.0f)
    public void setVolume(float volume) {
        if (volumeControl != null) {
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            volumeControl.setValue(min + (max - min) * volume);
        } else {
            System.err.println("Volume control not available.");
        }
    }
}
