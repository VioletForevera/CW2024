package Entities;

import Core.Projectile;

/**
 * Represents a projectile fired by the Boss character in the game.
 * The projectile has an initial position, a specified velocity, and moves
 * accordingly during each update cycle.
 */
public class BossProjectile extends Projectile {

	/**
	 * The name of the image file used to represent the fireball entity.
	 * This constant specifies the file name of the image asset.
	 */
	private static final String IMAGE_NAME = "fireball.png";

	/**
	 * The height of the image used for rendering, in pixels.
	 * This constant specifies the vertical size of the image representing the entity.
	 */
	private static final int IMAGE_HEIGHT = 75;


	/**
	 * The horizontal velocity of the Boss.
	 * This field controls the left or right movement of the Boss during the game.
	 */
	private double velocityX; // Horizontal velocity

	/**
	 * The vertical velocity of the Boss.
	 * This field controls the upward or downward movement of the Boss during the game.
	 */
	private double velocityY; // Vertical velocity


	/**
	 * Constructs a BossProjectile with the specified initial position and velocity.
	 *
	 * @param initialXPos the initial X position of the projectile.
	 * @param initialYPos the initial Y position of the projectile.
	 * @param velocityX   the horizontal velocity of the projectile.
	 * @param velocityY   the vertical velocity of the projectile.
	 */
	public BossProjectile(double initialXPos, double initialYPos, double velocityX, double velocityY) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);
		this.velocityX = velocityX;
		this.velocityY = velocityY;
	}

	/**
	 * Updates the position of the projectile based on its velocity.
	 * Called during each game update cycle.
	 */
	@Override
	public void updatePosition() {
		moveHorizontally(velocityX);
		moveVertically(velocityY);
	}

	/**
	 * Updates the actor, specifically its position.
	 * This method is invoked in each game update cycle.
	 */
	@Override
	public void updateActor() {
		updatePosition();
	}

	/**
	 * Sets or updates the velocity of the projectile.
	 *
	 * @param velocityX the new horizontal velocity.
	 * @param velocityY the new vertical velocity.
	 */
	public void setVelocity(double velocityX, double velocityY) {
		this.velocityX = velocityX;
		this.velocityY = velocityY;
	}
}
