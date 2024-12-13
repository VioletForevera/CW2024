package Core;

/**
 * Abstract class representing a Projectile. A Projectile is a destructible active actor
 * that can take damage and has specific position update behavior.
 */
public abstract class Projectile extends ActiveActorDestructible {

	/**
	 * Constructs a Projectile with the specified image, size, and initial position.
	 *
	 * @param imageName   the name of the image file representing the projectile.
	 * @param imageHeight the height of the image.
	 * @param initialXPos the initial x-coordinate position.
	 * @param initialYPos the initial y-coordinate position.
	 */
	public Projectile(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super(imageName, imageHeight, initialXPos, initialYPos);
	}

	/**
	 * Destroys the projectile. This method is invoked when the projectile takes damage.
	 */
	@Override
	public void takeDamage() {
		this.destroy();
	}

	/**
	 * Abstract method to update the position of the projectile. Subclasses must implement this method
	 * to define the specific movement behavior of the projectile.
	 */
	@Override
	public abstract void updatePosition();

}
