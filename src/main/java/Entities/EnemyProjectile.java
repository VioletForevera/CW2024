package Entities;

import Core.Projectile;

/**
 * Represents a projectile fired by an enemy in the game. The projectile moves
 * horizontally and interacts with other game objects upon collision.
 */
public class EnemyProjectile extends Projectile {

	/**
	 * The file name of the image used to represent the actor visually.
	 * This constant specifies the resource path or file name for the actor's appearance.
	 */
	private static final String IMAGE_NAME = "enemyFire.png";

	/**
	 * The height of the image used for rendering the actor, in pixels.
	 * This constant defines the vertical size of the image displayed for the actor.
	 */
	private static final int IMAGE_HEIGHT = 50;

	/**
	 * The horizontal velocity of the actor.
	 * This constant defines the speed at which the actor moves horizontally across the screen.
	 */
	private static final int HORIZONTAL_VELOCITY = -10;


	/**
	 * Constructs an EnemyProjectile at the specified initial position.
	 *
	 * @param initialXPos the initial X position of the projectile.
	 * @param initialYPos the initial Y position of the projectile.
	 */
	public EnemyProjectile(double initialXPos, double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);
	}

	/**
	 * Updates the position of the projectile by moving it horizontally.
	 */
	@Override
	public void updatePosition() {
		moveHorizontally(HORIZONTAL_VELOCITY);
	}

	/**
	 * Updates the state of the projectile, including its position and other relevant attributes.
	 */
	@Override
	public void updateActor() {
		updatePosition();
	}
}
