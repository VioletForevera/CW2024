package sounds;

import javax.sound.sampled.*;
import java.io.InputStream;

public class MusicPlayer {
    private Clip clip;
    private FloatControl volumeControl;

    public MusicPlayer(String resourcePath) {
        try {
            // 使用类路径加载资源文件
            InputStream soundStream = getClass().getResourceAsStream(resourcePath);
            if (soundStream == null) {
                throw new IllegalArgumentException("File not found: " + resourcePath);
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundStream);
            clip = AudioSystem.getClip();
            clip.open(audioStream);

            // 检查是否支持音量控制
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            } else {
                System.err.println("Volume control not supported.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null) {
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY); // 循环播放
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.setFramePosition(0); // 重置播放位置
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
}
