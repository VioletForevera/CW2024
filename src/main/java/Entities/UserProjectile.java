package Entities;

import Core.Projectile;
import javafx.scene.Group;

/**
 * Represents a projectile fired by the user-controlled plane.
 * The UserProjectile moves forward horizontally and has a customizable hitbox for collision detection.
 */
public class UserProjectile extends Projectile {

	/**
	 * The name of the image used to represent the user's projectile.
	 */
	private static final String IMAGE_NAME = "userfire.png";

	/**
	 * The height of the image used to represent the user's projectile, in pixels.
	 */
	private static final int IMAGE_HEIGHT = 125;

	/**
	 * The horizontal velocity of the user's projectile.
	 * This defines the speed at which the projectile moves horizontally.
	 */
	private static final int HORIZONTAL_VELOCITY = 15;

	/**
	 * The horizontal offset for positioning the hitbox of the projectile.
	 * This helps align the hitbox with the visible part of the projectile.
	 */
	private double horizontalOffset = 50.0;

	/**
	 * The vertical offset for positioning the hitbox of the projectile.
	 * This helps align the hitbox with the visible part of the projectile.
	 */
	private double verticalOffset = 50.0;


	/**
	 * Constructs a UserProjectile with the specified initial position and optional visualization of its hitbox.
	 *
	 * @param initialXPos the initial horizontal position of the projectile.
	 * @param initialYPos the initial vertical position of the projectile.
	 * @param root        the root group where the hitbox visualization can be added for debugging (optional).
	 */
	public UserProjectile(double initialXPos, double initialYPos, Group root) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);

		// Initialize the hitbox size
		setHitboxSize(IMAGE_HEIGHT * 0.3, IMAGE_HEIGHT * 0.2);

		// Visualize the hitbox (optional, for debugging purposes)
		// visualizeHitbox(root);
	}

	/**
	 * Updates the position of the projectile.
	 * The projectile moves forward horizontally.
	 */
	@Override
	public void updatePosition() {
		// Move the projectile horizontally
		moveHorizontally(HORIZONTAL_VELOCITY);

		// Synchronize the hitbox position
		updateHitbox();
	}

	/**
	 * Updates the state of the projectile, including its position.
	 */
	@Override
	public void updateActor() {
		updatePosition(); // Update the position
	}

	/**
	 * Updates the position of the hitbox based on the projectile's position and offsets.
	 */
	@Override
	protected void updateHitbox() {
		if (getHitbox() != null) {
			// Adjust the hitbox position using the offsets
			getHitbox().setLayoutX(getLayoutX() + getTranslateX() + horizontalOffset);
			getHitbox().setLayoutY(getLayoutY() + getTranslateY() + verticalOffset);
		}
	}

	/**
	 * Sets the horizontal and vertical offsets for the hitbox.
	 *
	 * @param horizontalOffset the horizontal offset to apply.
	 * @param verticalOffset   the vertical offset to apply.
	 */
	/*public void setHitboxOffsets(double horizontalOffset, double verticalOffset) {
		this.horizontalOffset = horizontalOffset;
		this.verticalOffset = verticalOffset;
		System.out.println("Set hitbox offsets: horizontalOffset=" + horizontalOffset
				+ ", verticalOffset=" + verticalOffset);
	}*/

	/**
	 * Optional: Visualizes the hitbox by adding it to the root group for debugging purposes.
	 *
	 * @param root the root group where the hitbox can be added.
	 */
    /*public void visualizeHitbox(Group root) {
        if (getHitbox() != null && !root.getChildren().contains(getHitbox())) {
            root.getChildren().add(getHitbox());
        }
    }*/
}
