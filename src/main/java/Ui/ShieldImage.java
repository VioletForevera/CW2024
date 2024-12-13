package Ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents the shield image used in the game, allowing it to be shown or hidden as needed.
 */
public class ShieldImage extends ImageView {

	/**
	 * The path to the shield image resource.
	 * This constant specifies the location of the shield image file used for visual representation.
	 */
	private static final String IMAGE_NAME = "/com/example/demo/images/shield.png";

	/**
	 * The size of the shield image in pixels.
	 * This constant defines the height and width of the shield, assuming a square aspect ratio.
	 */
	private static final int SHIELD_SIZE = 200;


	/**
	 * Constructs a ShieldImage instance with the specified position.
	 *
	 * @param xPosition the x-coordinate of the shield image.
	 * @param yPosition the y-coordinate of the shield image.
	 */
	public ShieldImage(double xPosition, double yPosition) {
		this.setLayoutX(xPosition);
		this.setLayoutY(yPosition);

		// Debugging code: Check if the image path is successfully loaded
		java.net.URL shieldImageUrl = getClass().getResource(IMAGE_NAME);
		System.out.println("Shield Image URL: " + shieldImageUrl); // Print the URL

		if (shieldImageUrl != null) {
			this.setImage(new Image(shieldImageUrl.toExternalForm()));
		} else {
			System.out.println("Shield image not found at the specified path.");
		}

		this.setVisible(false);
		this.setFitHeight(SHIELD_SIZE);
		this.setFitWidth(SHIELD_SIZE);
	}

	/**
	 * Makes the shield image visible.
	 */
	public void showShield() {
		this.setVisible(true);
	}

	/**
	 * Hides the shield image.
	 */
	public void hideShield() {
		this.setVisible(false);
	}
}
