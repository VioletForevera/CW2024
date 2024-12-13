package Core;

import sounds.MusicPlayer;

/**
 * Abstract class representing a FighterPlane. FighterPlane is a destructible active actor
 * that can take damage, fire projectiles, and manage its health.
 */
public abstract class FighterPlane extends ActiveActorDestructible {

	/**
	 * Represents the current health of the fighter plane.
	 * This value decreases as the plane takes damage.
	 */
	private int health; // The health of the fighter plane.


	/**
	 * Constructs a FighterPlane with the specified image, size, position, and initial health.
	 *
	 * @param imageName   the name of the image file representing the fighter plane.
	 * @param imageHeight the height of the image.
	 * @param initialXPos the initial x-coordinate position.
	 * @param initialYPos the initial y-coordinate position.
	 * @param health      the initial health of the fighter plane.
	 */
	public FighterPlane(String imageName, int imageHeight, double initialXPos, double initialYPos, int health) {
		super(imageName, imageHeight, initialXPos, initialYPos);
		this.health = health;
	}

	/**
	 * Abstract method to be implemented by subclasses to fire projectiles.
	 *
	 * @return an ActiveActorDestructible representing the fired projectile.
	 */
	public abstract ActiveActorDestructible fireProjectile();

	/**
	 * Reduces the health of the FighterPlane by 1. If health reaches zero, the plane is destroyed
	 * and an explosion sound effect is played.
	 */
	@Override
	public void takeDamage() {
		health--;
		System.out.println("FighterPlane took damage. Remaining health: " + health);
		if (healthAtZero()) {
			// Play explosion sound effect at 60% volume.
			MusicPlayer.playEffect("/com/example/demo/images/explosion.wav", 0.6f);
			this.destroy();
			System.out.println("FighterPlane destroyed.");
		}
	}

	/**
	 * Calculates the x-coordinate for the projectile to be fired.
	 *
	 * @param xPositionOffset the offset value to adjust the x-coordinate.
	 * @return the calculated x-coordinate for the projectile.
	 */
	protected double getProjectileXPosition(double xPositionOffset) {
		return getLayoutX() + getTranslateX() + xPositionOffset;
	}

	/**
	 * Calculates the y-coordinate for the projectile to be fired.
	 *
	 * @param yPositionOffset the offset value to adjust the y-coordinate.
	 * @return the calculated y-coordinate for the projectile.
	 */
	protected double getProjectileYPosition(double yPositionOffset) {
		return getLayoutY() + getTranslateY() + yPositionOffset;
	}

	/**
	 * Checks if the health of the FighterPlane is zero or less.
	 *
	 * @return true if health is zero or less, false otherwise.
	 */
	private boolean healthAtZero() {
		return health <= 0;
	}

	/**
	 * Gets the current health of the FighterPlane.
	 *
	 * @return the current health.
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * Updates the actor, including its position. This method is called periodically in the game loop.
	 */
	@Override
	public void updateActor() {
		updatePosition(); // Update the position of the fighter plane.
		System.out.println("Updated FighterPlane actor.");
	}

	/**
	 * Sets the health of the FighterPlane to a specific value.
	 *
	 * @param health the new health value.
	 */
	public void setHealth(int health) {
		this.health = health;
		System.out.println("Health set to: " + health);
	}
}
