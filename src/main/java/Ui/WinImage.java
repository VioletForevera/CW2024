package Ui;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Represents the "You Win" screen in the game, which displays a congratulatory image and an exit button.
 */
public class WinImage extends Pane {

	/**
	 * The file path to the "You Win" image.
	 */
	private static final String IMAGE_NAME = "/com/example/demo/images/youwin.png";

	/**
	 * The height of the "You Win" image in pixels.
	 */
	private static final int HEIGHT = 500;

	/**
	 * The width of the "You Win" image in pixels.
	 */
	private static final int WIDTH = 600;

	/**
	 * The ImageView used to display the "You Win" image.
	 */
	private final ImageView winImage;

	/**
	 * The button used to exit the game.
	 */
	private final Button exitButton;

	/**
	 * Constructs a WinImage instance at the specified position.
	 *
	 * @param xPosition the x-coordinate of the "You Win" image pane.
	 * @param yPosition the y-coordinate of the "You Win" image pane.
	 */
	public WinImage(double xPosition, double yPosition) {
		// Setup win image
		winImage = new ImageView(new Image(getClass().getResource(IMAGE_NAME).toExternalForm()));
		winImage.setFitHeight(HEIGHT);
		winImage.setFitWidth(WIDTH);
		winImage.setLayoutX(0);
		winImage.setLayoutY(0);

		// Setup Exit button
		exitButton = new Button("Exit Game");
		styleButton(exitButton);
		exitButton.setLayoutX(WIDTH / 2 - 150); // Position the button in the center horizontally
		exitButton.setLayoutY(HEIGHT - 80);    // Position the button at the bottom of the image
		exitButton.setOnAction(event -> System.exit(0)); // Exit the application on click

		// Add components to the pane
		this.getChildren().addAll(winImage, exitButton);
		this.setLayoutX(xPosition);
		this.setLayoutY(yPosition);
		this.setVisible(false); // Initially hidden
	}

	/**
	 * Displays the "You Win" screen by making the pane visible.
	 */
	public void showWinImage() {
		this.setVisible(true);
	}

	/**
	 * Applies styling to the provided button, including hover effects.
	 *
	 * @param button the button to be styled.
	 */
	private void styleButton(Button button) {
		button.setStyle(
				"-fx-font-size: 16px; " +
						"-fx-background-color: #4CAF50; " + // Green background
						"-fx-text-fill: white; " +          // White text
						"-fx-background-radius: 10; " +    // Rounded corners
						"-fx-padding: 10 20 10 20;"        // Padding for a larger button
		);

		// Change button style on mouse hover
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
