package Entities;

import Core.ActiveActorDestructible;
import Core.FighterPlane;
import javafx.scene.Group;

/**
 * Represents the user's controllable plane in the game.
 * The UserPlane can move in all directions, fire projectiles, and take damage.
 */
public class UserPlane extends FighterPlane {

	/**
	 * The name of the image used to represent the user's plane.
	 */
	private static final String IMAGE_NAME = "userplane.png";

	/**
	 * The upper bound for the user's vertical position.
	 * This prevents the plane from moving too high on the screen.
	 */
	private static final double Y_UPPER_BOUND = -40;

	/**
	 * The lower bound for the user's vertical position.
	 * This prevents the plane from moving too low on the screen.
	 */
	private static final double Y_LOWER_BOUND = 600.0;

	/**
	 * The left bound for the user's horizontal position.
	 * This prevents the plane from moving too far to the left.
	 */
	private static final double X_LEFT_BOUND = 0.0;

	/**
	 * The right bound for the user's horizontal position.
	 * This prevents the plane from moving too far to the right.
	 */
	private static final double X_RIGHT_BOUND = 1300.0;

	/**
	 * The initial horizontal position of the user's plane.
	 */
	private static final double INITIAL_X_POSITION = 5.0;

	/**
	 * The initial vertical position of the user's plane.
	 */
	private static final double INITIAL_Y_POSITION = 300.0;

	/**
	 * The height of the image used to represent the user's plane, in pixels.
	 */
	private static final int IMAGE_HEIGHT = 150;

	/**
	 * The default vertical velocity of the user's plane.
	 */
	private static final int VERTICAL_VELOCITY = 8;

	/**
	 * The default horizontal velocity of the user's plane.
	 */
	private static final int HORIZONTAL_VELOCITY = 8;

	/**
	 * The horizontal offset for positioning the projectile fired by the user's plane.
	 */
	private static final int PROJECTILE_X_POSITION_OFFSET = 60;

	/**
	 * The vertical offset for positioning the projectile fired by the user's plane.
	 */
	private static final int PROJECTILE_Y_POSITION_OFFSET = 10;

	/**
	 * A multiplier for adjusting the vertical velocity of the user's plane.
	 * This can be used for accelerating or decelerating movement vertically.
	 */
	private int verticalVelocityMultiplier;

	/**
	 * A multiplier for adjusting the horizontal velocity of the user's plane.
	 * This can be used for accelerating or decelerating movement horizontally.
	 */
	private int horizontalVelocityMultiplier;

	/**
	 * Tracks the number of enemies destroyed by the user's plane.
	 */
	private int numberOfKills;

	/**
	 * The current health of the user's plane.
	 * This decreases as the plane takes damage.
	 */
	private int health;

	/**
	 * The root group where visual elements associated with the user's plane are added.
	 */
	private final Group root;


	/**
	 * Constructs a UserPlane with specified initial health and root group.
	 *
	 * @param initialHealth the initial health of the UserPlane.
	 * @param root          the root group for adding visual elements.
	 */
	public UserPlane(int initialHealth, Group root) {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, initialHealth);
		this.health = initialHealth;
		verticalVelocityMultiplier = 0;
		horizontalVelocityMultiplier = 0;
		this.root = root;

		setHitboxSize(IMAGE_HEIGHT * 0.8, IMAGE_HEIGHT * 0.5);
	}

	/**
	 * Constructs a UserPlane with specified initial health and no root group.
	 *
	 * @param initialHealth the initial health of the UserPlane.
	 */
	public UserPlane(int initialHealth) {
		this(initialHealth, null);
	}

	/**
	 * Updates the position of the UserPlane based on its velocity multipliers.
	 * Ensures that the plane stays within defined boundaries.
	 */
	@Override
	public void updatePosition() {
		if (isMoving()) {
			double initialTranslateY = getTranslateY();
			double initialTranslateX = getTranslateX();

			this.moveVertically(VERTICAL_VELOCITY * verticalVelocityMultiplier);
			double newPositionY = getLayoutY() + getTranslateY();
			if (newPositionY < Y_UPPER_BOUND || newPositionY > Y_LOWER_BOUND) {
				this.setTranslateY(initialTranslateY);
			}

			this.moveHorizontally(HORIZONTAL_VELOCITY * horizontalVelocityMultiplier);
			double newPositionX = getLayoutX() + getTranslateX();
			if (newPositionX < X_LEFT_BOUND || newPositionX > X_RIGHT_BOUND) {
				this.setTranslateX(initialTranslateX);
			}
		}
		updateHitbox();
	}

	/**
	 * Updates the state of the UserPlane, including its position.
	 */
	@Override
	public void updateActor() {
		updatePosition();
	}

	/**
	 * Fires a projectile from the UserPlane's current position.
	 *
	 * @return the newly created projectile.
	 */
	@Override
	public ActiveActorDestructible fireProjectile() {
		double projectileX = getLayoutX() + getTranslateX() + PROJECTILE_X_POSITION_OFFSET;
		double projectileY = getLayoutY() + getTranslateY() + PROJECTILE_Y_POSITION_OFFSET;

		UserProjectile projectile = new UserProjectile(projectileX, projectileY, root);
		return projectile;
	}

	/**
	 * Decreases the UserPlane's health by one.
	 * If health reaches zero, the plane is destroyed.
	 */
	@Override
	public void takeDamage() {
		if (health > 0) {
			health--;
			if (health <= 0) {
				destroy();
			}
		}
	}

	/**
	 * Increases the UserPlane's health by one.
	 */
	public void incrementHealth() {
		health++;
	}

	/**
	 * Gets the current health of the UserPlane.
	 *
	 * @return the current health.
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * Checks if the UserPlane is currently moving.
	 *
	 * @return true if moving, false otherwise.
	 */
	public boolean isMoving() {
		return verticalVelocityMultiplier != 0 || horizontalVelocityMultiplier != 0;
	}

	/**
	 * Starts upward movement by setting the vertical velocity multiplier to -1.
	 */
	public void moveUp() {
		verticalVelocityMultiplier = -1;
	}

	/**
	 * Starts downward movement by setting the vertical velocity multiplier to 1.
	 */
	public void moveDown() {
		verticalVelocityMultiplier = 1;
	}

	/**
	 * Starts leftward movement by setting the horizontal velocity multiplier to -1.
	 */
	public void moveLeft() {
		horizontalVelocityMultiplier = -1;
	}

	/**
	 * Starts rightward movement by setting the horizontal velocity multiplier to 1.
	 */
	public void moveRight() {
		horizontalVelocityMultiplier = 1;
	}

	/**
	 * Stops vertical movement by setting the vertical velocity multiplier to 0.
	 */
	public void stopVertical() {
		verticalVelocityMultiplier = 0;
	}

	/**
	 * Stops horizontal movement by setting the horizontal velocity multiplier to 0.
	 */
	public void stopHorizontal() {
		horizontalVelocityMultiplier = 0;
	}

	/**
	 * Stops all movement by resetting both vertical and horizontal velocity multipliers to 0.
	 */
	public void stop() {
		stopVertical();
		stopHorizontal();
	}

	/**
	 * Gets the number of kills achieved by the UserPlane.
	 *
	 * @return the number of kills.
	 */
	public int getNumberOfKills() {
		return numberOfKills;
	}

	/**
	 * Increments the kill count for the UserPlane.
	 */
	public void incrementKillCount() {
		numberOfKills++;
	}
}
