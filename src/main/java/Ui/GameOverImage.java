package Ui;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Represents the game over screen displayed when the player loses the game.
 * Includes a background image and an exit button to quit the application.
 */
public class GameOverImage extends Pane {

	/**
	 * The file path to the game over image.
	 * This constant specifies the location of the image resource used to display the game over screen.
	 */
	private static final String IMAGE_NAME = "/com/example/demo/images/gameover.png"; // Path to the game over image

	/**
	 * The height of the game over image, in pixels.
	 * This constant defines the vertical size of the image displayed when the game is over.
	 */
	private static final int HEIGHT = 800; // Height of the game over image

	/**
	 * The width of the game over image, in pixels.
	 * This constant defines the horizontal size of the image displayed when the game is over.
	 */
	private static final int WIDTH = 1400; // Width of the game over image


	/**
	 * An ImageView used to display the game over image.
	 * This image appears when the player loses the game.
	 */
	private final ImageView gameOverImage; // ImageView to display the game over image

	/**
	 * A button that allows the player to exit the game.
	 * Clicking this button will terminate the application.
	 */
	private final Button exitButton; // Button to exit the game


	/**
	 * Constructs a GameOverImage instance with the specified position on the screen.
	 *
	 * @param xPosition the x-coordinate for positioning the pane.
	 * @param yPosition the y-coordinate for positioning the pane.
	 */
	public GameOverImage(double xPosition, double yPosition) {
		// Setup game over image
		gameOverImage = new ImageView(new Image(getClass().getResource(IMAGE_NAME).toExternalForm()));
		gameOverImage.setFitHeight(HEIGHT);
		gameOverImage.setFitWidth(WIDTH);
		gameOverImage.setLayoutX(0);
		gameOverImage.setLayoutY(100);

		// Setup Exit button
		exitButton = new Button("Exit Game");
		styleButton(exitButton);
		exitButton.setLayoutX(WIDTH / 2 - 150); // Center button horizontally
		exitButton.setLayoutY(HEIGHT - 80); // Position button near the bottom
		exitButton.setOnAction(event -> System.exit(0)); // Exit the application on button click

		// Add components to the pane
		this.getChildren().addAll(gameOverImage, exitButton);
		this.setLayoutX(xPosition);
		this.setLayoutY(yPosition);
	}

	/**
	 * Applies styling to a button, including hover effects for visual feedback.
	 *
	 * @param button the button to style.
	 */
	private void styleButton(Button button) {
		button.setStyle(
				"-fx-font-size: 16px; " +
						"-fx-background-color: #4CAF50; " + // Green background
						"-fx-text-fill: white; " + // White text
						"-fx-background-radius: 10; " + // Rounded corners
						"-fx-padding: 10 20 10 20;" // Padding for a larger button
		);

		button.setOnMouseEntered(event ->
				button.setStyle(
						"-fx-font-size: 16px; " +
								"-fx-background-color: #45a049; " + // Darker green on hover
								"-fx-text-fill: white; " +
								"-fx-background-radius: 10; " +
								"-fx-padding: 10 20 10 20;"
				)
		);

		button.setOnMouseExited(event ->
				button.setStyle(
						"-fx-font-size: 16px; " +
								"-fx-background-color: #4CAF50; " +
								"-fx-text-fill: white; " +
								"-fx-background-radius: 10; " +
								"-fx-padding: 10 20 10 20;"
				)
		);
	}
}
