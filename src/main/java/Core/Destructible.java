package Core;

/**
 * Represents an object that can take damage and be destroyed.
 * This interface should be implemented by classes that have destructible behavior.
 */
public interface Destructible {

	/**
	 * Applies damage to the object. Implementing classes should define the behavior
	 * when the object takes damage (e.g., reducing health or triggering effects).
	 */
	void takeDamage();

	/**
	 * Destroys the object. Implementing classes should define what happens
	 * when the object is destroyed (e.g., removing it from the game or triggering animations).
	 */
	void destroy();
}
