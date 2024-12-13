package Core;

/**
 * Represents an active actor in the game that can be destroyed.
 * This abstract class extends {@link ActiveActor} and implements {@link Destructible},
 * providing a foundation for actors that can take damage and be destroyed.
 */
public abstract class ActiveActorDestructible extends ActiveActor implements Destructible {

	/**
	 * Indicates whether this actor is destroyed.
	 */
	private boolean isDestroyed;

	/**
	 * Constructs an {@code ActiveActorDestructible} instance with the specified parameters.
	 *
	 * @param imageName    the name of the image resource representing this actor.
	 * @param imageHeight  the height of the image for this actor.
	 * @param initialXPos  the initial x-coordinate of the actor.
	 * @param initialYPos  the initial y-coordinate of the actor.
	 */
	public ActiveActorDestructible(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super(imageName, imageHeight, initialXPos, initialYPos);
		isDestroyed = false;
	}

	/**
	 * Updates the position of this actor.
	 * Subclasses must provide an implementation for updating the actor's position.
	 */
	@Override
	public abstract void updatePosition();

	/**
	 * Updates the state of this actor, including position and other properties.
	 * Subclasses must provide an implementation for this method.
	 */
	public abstract void updateActor();

	/**
	 * Applies damage to this actor.
	 * Subclasses must provide an implementation to define how damage affects the actor.
	 */
	@Override
	public abstract void takeDamage();

	/**
	 * Marks this actor as destroyed and sets its destroyed state.
	 * This method is inherited from the {@link Destructible} interface.
	 */
	@Override
	public void destroy() {
		setDestroyed(true);
	}

	/**
	 * Sets the destroyed state of this actor.
	 *
	 * @param isDestroyed {@code true} to mark the actor as destroyed; {@code false} otherwise.
	 */
	protected void setDestroyed(boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
	}

	/**
	 * Returns whether this actor is destroyed.
	 *
	 * @return {@code true} if the actor is destroyed; {@code false} otherwise.
	 */
	public boolean isDestroyed() {
		return isDestroyed;
	}
}
