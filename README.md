# 13/11 Menu Music, Settings, and Background Update

## Feature Addition: Game Menu with Background, Background Music, and Volume Control

### Issue
- **Problem:** The game lacked a main menu with background music, an adjustable volume control option, and a visually engaging background.
- **Solution:** Added a game menu with a custom background image, background music, and a settings option for volume control.

### Solution
To implement the game menu, background music, and volume control, the following steps were taken:

1. **Add Game Menu with Background Image**
   - Created a main menu interface with options for "Start Game," "Settings," and "Exit," and set a custom background image to enhance visual appeal.
   - Code snippet:
     ```java
     private class BackgroundPanel extends JPanel {
         private Image backgroundImage;

         public BackgroundPanel() {
             backgroundImage = new ImageIcon(getClass().getResource("/com/example/demo/images/mainMenuBackground.png")).getImage();
         }

         @Override
         protected void paintComponent(Graphics g) {
             super.paintComponent(g);
             g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
         }
     }
     ```

2. **Initialize Background Music in Menu**
   - A `MusicPlayer` instance was created in the `MainMenu` class to play background music when the menu is opened, enhancing the ambiance.
   - Code snippet:
     ```java
     public MainMenu() {
         musicPlayer = new MusicPlayer("src/main/resources/com/example/demo/sounds/background_music.wav");
         musicPlayer.play();
     }
     ```

3. **Add Volume Control in Settings**
   - Added a slider to the settings dialog to adjust the volume of the background music. The slider is linked to the `MusicPlayer`'s `setVolume()` method, allowing users to adjust the music volume in real-time.
   - Code snippet:
     ```java
     JSlider volumeSlider = new JSlider(0, 100, 50);
     volumeSlider.addChangeListener(e -> {
         int volumeLevel = volumeSlider.getValue();
         musicPlayer.setVolume(volumeLevel / 100.0f);
     });
     ```

### Summary
This update introduces a main menu with a visually appealing background, background music for an immersive atmosphere, and a settings option to adjust the music volume. Together, these features provide a more engaging and customizable user experience.
