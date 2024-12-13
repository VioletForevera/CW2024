package Entities;

import Core.ActiveActorDestructible;
import Core.FighterPlane;
import javafx.scene.Group;

/**
 * Represents an enemy plane in the game. The enemy plane moves horizontally
 * and has the capability to fire projectiles at the player.
 */
public class EnemyPlane extends FighterPlane {

	/**
	 * The name of the image file used to represent the enemy plane.
	 */
	private static final String IMAGE_NAME = "enemyplane.png";

	/**
	 * The height of the enemy plane image in pixels.
	 */
	private static final int IMAGE_HEIGHT = 150;

	/**
	 * The horizontal velocity of the enemy plane, representing its speed and direction of movement.
	 * A negative value indicates movement to the left.
	 */
	private static final int HORIZONTAL_VELOCITY = -6;

	/**
	 * The horizontal offset for the position of projectiles fired by the enemy plane.
	 */
	private static final double PROJECTILE_X_POSITION_OFFSET = -100.0;

	/**
	 * The vertical offset for the position of projectiles fired by the enemy plane.
	 */
	private static final double PROJECTILE_Y_POSITION_OFFSET = 50.0;

	/**
	 * The initial health of the enemy plane.
	 * This value determines how many hits the enemy can take before being destroyed.
	 */
	private static final int INITIAL_HEALTH = 1;

	/**
	 * The firing rate of the enemy plane.
	 * This constant defines the probability of the enemy firing a projectile during each game update cycle.
	 */
	private static final double FIRE_RATE = 0.01;

	/**
	 * The horizontal offset for the enemy plane's hitbox.
	 * This value adjusts the hitbox position relative to the plane's displayed image.
	 */
	private double horizontalOffset = 40.0;

	/**
	 * The vertical offset for the enemy plane's hitbox.
	 * This value adjusts the hitbox position relative to the plane's displayed image.
	 */
	private double verticalOffset = 60.0;


	/**
	 * Constructs an EnemyPlane with the specified initial position and adds it to the root group.
	 *
	 * @param initialXPos the initial X position of the enemy plane.
	 * @param initialYPos the initial Y position of the enemy plane.
	 * @param root        the root group where the enemy plane's hitbox can be visualized (for debugging purposes).
	 */
	public EnemyPlane(double initialXPos, double initialYPos, Group root) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos, INITIAL_HEALTH);

		// Initialize the hitbox size
		setHitboxSize(IMAGE_HEIGHT * 0.9, IMAGE_HEIGHT * 0.3);

		// Optionally visualize the hitbox for debugging
		// visualizeHitbox(root);
	}

	/**
	 * Updates the position of the enemy plane by moving it horizontally.
	 * Ensures that the hitbox remains synchronized with the plane's position.
	 */
	@Override
	public void updatePosition() {
		moveHorizontally(HORIZONTAL_VELOCITY);
		updateHitbox();
	}

	/**
	 * Updates the state of the enemy plane, including its position and other relevant attributes.
	 */
	@Override
	public void updateActor() {
		updatePosition();
	}

	/**
	 * Fires a projectile from the enemy plane with a probability determined by the fire rate.
	 *
	 * @return a new instance of {@link EnemyProjectile} if fired; otherwise, {@code null}.
	 */
	@Override
	public ActiveActorDestructible fireProjectile() {
		if (Math.random() < FIRE_RATE) {
			double projectileXPosition = getProjectileXPosition(PROJECTILE_X_POSITION_OFFSET);
			double projectileYPosition = getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET);
			return new EnemyProjectile(projectileXPosition, projectileYPosition);
		}
		return null;
	}

	/**
	 * Updates the position of the hitbox based on the current position of the enemy plane.
	 * Adds an offset to align the hitbox accurately with the plane.
	 */
	@Override
	public void updateHitbox() {
		if (getHitbox() != null) {
			getHitbox().setLayoutX(getLayoutX() + getTranslateX() + horizontalOffset);
			getHitbox().setLayoutY(getLayoutY() + getTranslateY() + verticalOffset);
		}
	}

	// Uncomment the method below to visualize the hitbox for debugging purposes
    /*
    public void visualizeHitbox(Group root) {
        if (getHitbox() != null && !root.getChildren().contains(getHitbox())) {
            root.getChildren().add(getHitbox());
        }
    }
    */
}
