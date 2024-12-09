package sounds;

import javax.sound.sampled.*;
import java.io.InputStream;

public class MusicPlayer {
    private static MusicPlayer instance; // 单例实例
    private Clip clip;
    private FloatControl volumeControl;

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

    // 静态方法获取单例实例
    public static synchronized MusicPlayer getInstance(String resourcePath) {
        if (instance == null) {
            instance = new MusicPlayer(resourcePath);
        }
        return instance;
    }

    public void play() {
        if (clip != null) {
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.setFramePosition(0);
        }
    }

    public void setVolume(float volume) {
        if (volumeControl != null) {
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            volumeControl.setValue(min + (max - min) * volume);
        } else {
            System.err.println("Volume control not available.");
        }
    }

    public static void playEffect(String resourcePath, float volume) {
        try {
            System.out.println("Attempting to load resource: " + resourcePath); // 调试信息
            InputStream soundStream = MusicPlayer.class.getResourceAsStream(resourcePath);
            if (soundStream == null) {
                throw new IllegalArgumentException("File not found: " + resourcePath);
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundStream);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            // 设置音量
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float min = volumeControl.getMinimum(); // 最低音量
                float max = volumeControl.getMaximum(); // 最高音量
                float gain = min + (max - min) * volume; // 根据传入的 volume 值计算音量
                volumeControl.setValue(gain); // 设置音量
            } else {
                System.err.println("Volume control not supported for this clip.");
            }

            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
