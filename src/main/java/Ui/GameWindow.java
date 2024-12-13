package Ui;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the main game window for the "Sky Battle" game.
 * This window serves as the primary interface for displaying game content.
 */
public class GameWindow extends JFrame {

    /**
     * Constructs a new GameWindow instance with default settings, including size, title, and initial content.
     */
    public GameWindow() {
        setTitle("Sky Battle - Game Window"); // Set the title of the game window
        setSize(1300, 750); // Set the dimensions of the game window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit the application on window close
        setLocationRelativeTo(null); // Center the window on the screen

        // Add a placeholder label as the initial content of the game window
        JLabel label = new JLabel("Game is running...", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30)); // Set font style and size
        add(label); // Add the label to the window
    }

    /**
     * Launches the game by creating and displaying the GameWindow instance.
     * This method runs on the Event Dispatch Thread (EDT) to ensure thread safety.
     */
    public static void launchGame() {
        SwingUtilities.invokeLater(() -> {
            GameWindow gameWindow = new GameWindow(); // Create a new instance of the game window
            gameWindow.setVisible(true); // Make the game window visible
        });
    }
}
