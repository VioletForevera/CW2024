package Ui;

import com.example.demo.controller.Main;
import sounds.MusicPlayer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Represents the main menu of the game, providing options to start the game,
 * access settings, and exit the game.
 */
public class MainMenu extends JFrame {

    /**
     * The initial volume level of the game.
     * This variable stores the volume level as a percentage (0 to 100),
     * where 50 represents the default starting volume.
     */
    private int volumeLevel = 50; // Initial volume level


    /**
     * Constructs the main menu and initializes the UI components and background music.
     */
    public MainMenu() {
        // Initialize the music player (singleton pattern)
        MusicPlayer musicPlayer = MusicPlayer.getInstance("/com/example/demo/images/sound.wav");
        musicPlayer.play();
        musicPlayer.setVolume(volumeLevel / 100.0f);

        // Set window title and basic configuration
        setTitle("Game Menu");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set the background image
        setContentPane(new BackgroundPanel());

        // Create buttons
        JButton startButton = createStyledButton("Start Game");
        JButton settingsButton = createStyledButton("Settings");
        JButton exitButton = createStyledButton("Exit");

        // Add action listeners to buttons
        startButton.addActionListener(e -> startGame(musicPlayer));
        settingsButton.addActionListener(e -> openSettings(musicPlayer));
        exitButton.addActionListener(e -> exitGame());

        // Configure button layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(startButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(settingsButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(exitButton);
        buttonPanel.add(Box.createVerticalGlue());

        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.CENTER);
    }

    /**
     * Creates a styled button with the specified text.
     *
     * @param text the text to display on the button.
     * @return the styled button.
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setForeground(Color.WHITE);
        return button;
    }

    /**
     * Starts the game, stops the menu music, and closes the main menu window.
     *
     * @param musicPlayer the music player instance to stop.
     */
    private void startGame(MusicPlayer musicPlayer) {
        System.out.println("Game Started!");
        this.dispose(); // Close the current menu window

        // Stop menu music
        musicPlayer.stop();

        // Launch the game
        Main.startGame();
    }

    /**
     * Opens the settings dialog for adjusting the volume level.
     *
     * @param musicPlayer the music player instance to update the volume.
     */
    private void openSettings(MusicPlayer musicPlayer) {
        JDialog settingsDialog = new JDialog(this, "Settings", true);
        settingsDialog.setSize(400, 300);
        settingsDialog.setLocationRelativeTo(this);

        JPanel dialogPanel = new JPanel();
        dialogPanel.setBackground(new Color(50, 50, 50));
        dialogPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        dialogPanel.setLayout(new GridLayout(2, 1, 10, 10));

        JLabel volumeLabel = new JLabel("Volume");
        volumeLabel.setForeground(Color.WHITE);
        volumeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        volumeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JSlider volumeSlider = new JSlider(0, 100, volumeLevel);
        volumeSlider.setMajorTickSpacing(20);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);
        volumeSlider.setBackground(new Color(50, 50, 50));
        volumeSlider.setForeground(Color.WHITE);

        volumeSlider.addChangeListener(e -> {
            volumeLevel = volumeSlider.getValue();
            musicPlayer.setVolume(volumeLevel / 100.0f);
        });

        dialogPanel.add(volumeLabel);
        dialogPanel.add(volumeSlider);

        settingsDialog.setContentPane(dialogPanel);
        settingsDialog.setVisible(true);
    }

    /**
     * Exits the game by terminating the application.
     */
    private void exitGame() {
        System.exit(0);
    }

    /**
     * The main method to launch the main menu.
     *
     * @param args the command-line arguments.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainMenu menu = new MainMenu();
            menu.setVisible(true);
        });
    }

    /**
     * Represents the background panel for the main menu, displaying an image.
     */
    private class BackgroundPanel extends JPanel {
        /**
         * Represents the background image used in the game or UI component.
         * This variable holds the {@link Image} object to be displayed as the background.
         */
        private Image backgroundImage;


        /**
         * Constructs a BackgroundPanel and loads the background image.
         */
        public BackgroundPanel() {
            backgroundImage = new ImageIcon(getClass().getResource("/com/example/demo/images/mainMenuBackground.png")).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
