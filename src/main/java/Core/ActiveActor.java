package Core;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * The ActiveActor class serves as a base class for all active game entities.
 * It extends the {@link ImageView} class and provides functionality for movement,
 * hitbox management, and collision detection.
 */
public abstract class ActiveActor extends ImageView {
	/**
	 * The base location of image resources used in the application.
	 * This string is used as a prefix for locating image files.
	 */
	private static final String IMAGE_LOCATION = "/com/example/demo/images/";
	/**
	 * A rectangular area representing the hitbox of the object.
	 * The hitbox is used for collision detection during gameplay.
	 */
	private Rectangle hitbox; // The hitbox used for collision detection

	/**
	 * Constructs an ActiveActor instance with the specified image, size, and initial position.
	 *
	 * @param imageName   the name of the image file to use for the actor
	 * @param imageHeight the height of the image
	 * @param initialXPos the initial X position of the actor
	 * @param initialYPos the initial Y position of the actor
	 */
	public ActiveActor(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		this.setImage(new javafx.scene.image.Image(getClass().getResource(IMAGE_LOCATION + imageName).toExternalForm()));
		this.setLayoutX(initialXPos);
		this.setLayoutY(initialYPos);
		this.setFitHeight(imageHeight);
		this.setPreserveRatio(true);

		// Initialize the hitbox
		hitbox = new Rectangle();
		hitbox.setFill(Color.TRANSPARENT); // Ensure the hitbox is transparent
		hitbox.setStroke(Color.RED); // Set the border color to red for debugging
		hitbox.setStrokeWidth(1);
		updateHitbox(); // Synchronize the hitbox position during initialization
	}

	/**
	 * Updates the position of the actor. This method must be implemented by subclasses.
	 */
	public abstract void updatePosition();

	/**
	 * Updates the position of the hitbox to match the actor's current position.
	 */
	protected void updateHitbox() {
		hitbox.setX(getLayoutX() + getTranslateX());
		hitbox.setY(getLayoutY() + getTranslateY());
	}

	/**
	 * Updates the hitbox. This method is provided for external classes to call.
	 */
	public void updateActorHitbox() {
		updateHitbox(); // Calls the protected method
	}

	/**
	 * Sets the size of the hitbox.
	 *
	 * @param width  the width of the hitbox
	 * @param height the height of the hitbox
	 */
	public void setHitboxSize(double width, double height) {
		if (hitbox != null) {
			this.hitbox.setWidth(width);
			this.hitbox.setHeight(height);
			updateHitbox(); // Synchronize position after resizing
		}
	}

	/**
	 * Retrieves the current hitbox.
	 *
	 * @return the hitbox as a {@link Rectangle}
	 */
	public Rectangle getHitbox() {
		return hitbox;
	}

	/**
	 * Moves the actor horizontally by a specified amount and updates the hitbox.
	 *
	 * @param horizontalMove the amount to move horizontally
	 */
	protected void moveHorizontally(double horizontalMove) {
		this.setTranslateX(getTranslateX() + horizontalMove);
		updateHitbox();
	}

	/**
	 * Moves the actor vertically by a specified amount and updates the hitbox.
	 *
	 * @param verticalMove the amount to move vertically
	 */
	protected void moveVertically(double verticalMove) {
		this.setTranslateY(getTranslateY() + verticalMove);
		updateHitbox();
	}
}
