package sounds;

import javax.sound.sampled.*;
import java.io.InputStream;

/**
 * The MusicPlayer class is a singleton utility for managing background music
 * and sound effects in a game or application. It supports playback, stopping,
 * looping, and volume adjustment of audio clips.
 */
public class MusicPlayer {
    /**
     * Singleton instance of the MusicPlayer.
     * Ensures that only one instance of the MusicPlayer exists at any given time.
     * Used to manage and control background music across the application.
     */
    private static MusicPlayer instance; // Singleton instance

    /**
     * Represents the audio clip for playing sound or music.
     * This variable holds the audio data and provides methods to control playback such as start, stop, and loop.
     */
    private Clip clip;

    /**
     * Represents the volume control for the audio system.
     * This variable provides the ability to adjust the volume of audio playback using {@link FloatControl}.
     */
    private FloatControl volumeControl;


    /**
     * Private constructor to initialize the MusicPlayer with the specified audio file.
     *
     * @param resourcePath the path to the audio resource to be played.
     */
    private MusicPlayer(String resourcePath) {
        try {
            InputStream soundStream = getClass().getResourceAsStream(resourcePath);
            if (soundStream == null) {
                throw new IllegalArgumentException("File not found: " + resourcePath);
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundStream);
            clip = AudioSystem.getClip();
            clip.open(audioStream);

            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            } else {
                System.err.println("Volume control not supported.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the singleton instance of the MusicPlayer, initializing it if necessary.
     *
     * @param resourcePath the path to the audio resource to be played.
     * @return the singleton MusicPlayer instance.
     */
    public static synchronized MusicPlayer getInstance(String resourcePath) {
        if (instance == null) {
            instance = new MusicPlayer(resourcePath);
        }
        return instance;
    }

    /**
     * Starts playing the audio clip in a continuous loop.
     */
    public void play() {
        if (clip != null) {
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    /**
     * Stops the audio playback and resets the clip to the beginning.
     */
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.setFramePosition(0);
        }
    }

    /**
     * Sets the playback volume for the audio clip.
     *
     * @param volume a value between 0.0 (minimum volume) and 1.0 (maximum volume).
     */
    public void setVolume(float volume) {
        if (volumeControl != null) {
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            volumeControl.setValue(min + (max - min) * volume);
        } else {
            System.err.println("Volume control not available.");
        }
    }

    /**
     * Plays a one-time sound effect with the specified volume.
     *
     * @param resourcePath the path to the audio resource to be played.
     * @param volume       a value between 0.0 (minimum volume) and 1.0 (maximum volume).
     */
    public static void playEffect(String resourcePath, float volume) {
        try {
            System.out.println("Attempting to load resource: " + resourcePath); // Debug information
            InputStream soundStream = MusicPlayer.class.getResourceAsStream(resourcePath);
            if (soundStream == null) {
                throw new IllegalArgumentException("File not found: " + resourcePath);
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundStream);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            // Set volume
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float min = volumeControl.getMinimum();
                float max = volumeControl.getMaximum();
                float gain = min + (max - min) * volume;
                volumeControl.setValue(gain);
            } else {
                System.err.println("Volume control not supported for this clip.");
            }

            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
