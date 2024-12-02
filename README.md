# Game Project Update Log

## Overview
This update introduces the following features and fixes:
1. **Global Background Music**: Background music now plays seamlessly across different levels, maintaining continuity during gameplay.
2. **Bug Fix for Re-entering Level Two**: Fixed an issue where transitioning to the next level could inadvertently return the player to Level Two due to multiple level transition triggers.

---

## Update Details

### 1. Global Background Music
Added the `GlobalMusic` class to manage background music playback globally. Using the Singleton pattern, the music player is instantiated only once, ensuring smooth playback across level transitions.

#### New Code:
**`GlobalMusic.java`**
```java
package Core;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class GlobalMusic {
    private static GlobalMusic instance;
    private MediaPlayer mediaPlayer;

    private GlobalMusic() {
        Media music = new Media(getClass().getResource("/com/example/demo/audio/background_music.mp3").toExternalForm());
        mediaPlayer = new MediaPlayer(music);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop indefinitely
    }

    public static GlobalMusic getInstance() {
        if (instance == null) {
            instance = new GlobalMusic();
        }
        return instance;
    }

    public void play() {
        if (mediaPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
            mediaPlayer.play();
        }
    }

    public void stop() {
        mediaPlayer.stop();
    }
}
```
