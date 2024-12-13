/**
 * Represents a Boss entity in the game. The Boss is a type of FighterPlane with additional
 * functionality, including health bar, shield, and complex movement patterns.
 */
package Entities;

import Core.ActiveActorDestructible;
import Core.FighterPlane;
import Ui.ShieldImage;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a Boss character in the game with unique abilities and behaviors such as a shield,
 * a health bar, and complex movement patterns.
 */
public class Boss extends FighterPlane {

	// Static fields for defining boss attributes
	/**
	 * The file name of the image representing the boss.
	 * Used to load the visual representation of the boss in the game.
	 */
	private static final String IMAGE_NAME = "bossplane.png";

	/**
	 * The initial X-coordinate for the boss's position.
	 * This determines where the boss will appear horizontally on the screen when the game starts.
	 */
	private static final double INITIAL_X_POSITION = 1000.0;

	/**
	 * The initial Y-coordinate for the boss's position.
	 * This determines where the boss will appear vertically on the screen when the game starts.
	 */
	private static final double INITIAL_Y_POSITION = 400;

	/**
	 * The vertical offset for the projectile's starting position relative to the Boss's position.
	 * This constant is used to align the projectile's initial Y-coordinate with the Boss's firing position.
	 */
	public static final double PROJECTILE_Y_POSITION_OFFSET = 75.0;

	/**
	 * The firing rate of the Boss, represented as a probability.
	 */
	private static final double BOSS_FIRE_RATE = 0.04;
	/**
	 * The probability of the Boss activating its shield.
	 */
	private static final double BOSS_SHIELD_PROBABILITY = 0.002;
	/**
	 * The height of the boss image in pixels.
	 * Used to determine the size and scale of the boss's visual representation.
	 */
	private static final int IMAGE_HEIGHT = 300;

	/**
	 * The vertical velocity for the Boss's movement.
	 * This constant determines the speed of upward or downward motion.
	 */
	private static final int VERTICAL_VELOCITY = 8;

	/**
	 * The initial health value of the boss.
	 * This represents the total number of health points the boss starts with.
	 */
	private static final int HEALTH = 19;
	/**
	 * The number of moves in each movement cycle.
	 * Defines how frequently the boss changes its movement direction.
	 */
	private static final int MOVE_FREQUENCY_PER_CYCLE = 5;

	/**
	 * Represents the integer value zero.
	 * Used as a constant for various calculations or comparisons.
	 */
	private static final int ZERO = 0;

	/**
	 * The maximum number of consecutive frames the boss can move in the same direction.
	 * Helps create variability in the boss's movement pattern.
	 */
	private static final int MAX_FRAMES_WITH_SAME_MOVE = 10;

	/**
	 * The upper boundary for the boss's vertical position on the screen.
	 * Prevents the boss from moving too far up off the screen.
	 */
	private static final int Y_POSITION_UPPER_BOUND = -100;

	/**
	 * The lower boundary for the boss's vertical position on the screen.
	 * Prevents the boss from moving too far down off the screen.
	 */
	private static final int Y_POSITION_LOWER_BOUND = 475;

	/**
	 * The maximum number of frames during which the shield can remain active.
	 * Ensures that the shield effect has a finite duration.
	 */
	private static final int MAX_FRAMES_WITH_SHIELD = 500;


	/**
	 * The movement pattern for the Boss.
	 * This list stores a sequence of vertical velocity values (up, down, or stationary)
	 * that dictate the Boss's movement behavior.
	 */
	private final List<Integer> movePattern;

	/**
	 * Indicates whether the Boss currently has its shield activated.
	 * When true, the Boss is protected from taking damage.
	 */
	private boolean isShielded;

	/**
	 * Tracks the number of consecutive moves made in the same direction.
	 * Used to determine when to shuffle the movement pattern to create dynamic behavior.
	 */
	private int consecutiveMovesInSameDirection;

	/**
	 * Tracks the index of the current move in the boss's movement pattern.
	 * This value determines which movement direction the boss will follow next.
	 */
	private int indexOfCurrentMove;

	/**
	 * Tracks the number of frames during which the shield has been activated.
	 * This is used to determine when the shield should be deactivated.
	 */
	private int framesWithShieldActivated;

	/**
	 * The shield image associated with the Boss.
	 * This visual element is used to indicate when the Boss has an active shield.
	 */
	private final ShieldImage shieldImage;

	/**
	 * The health bar representing the boss's remaining health visually.
	 * This is a red rectangle that decreases in width as the boss takes damage.
	 */
	private final Rectangle healthBar;

	/**
	 * The maximum health of the Boss.
	 * This value is used to calculate the health percentage for the health bar.
	 */
	private final double maxHealth;

	/**
	 * The horizontal offset of the hitbox relative to the boss's position.
	 * This determines how far the hitbox is shifted along the X-axis.
	 */
	private double hitboxOffsetX;
	/**
	 * The vertical offset of the hitbox relative to the boss's position.
	 * This determines how far the hitbox is shifted along the Y-axis.
	 */
	private double hitboxOffsetY;

	/**
	 * Default constructor. Initializes the Boss with default settings.
	 */
	public Boss() {
		this(new Group());
	}

	/**
	 * Constructs a Boss with a specified root group for adding UI elements.
	 *
	 * @param root the root group where the shield image and health bar will be added.
	 */
	public Boss(Group root) {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, HEALTH);
		this.movePattern = new ArrayList<>();
		this.consecutiveMovesInSameDirection = 0;
		this.indexOfCurrentMove = 0;
		this.framesWithShieldActivated = 0;
		this.isShielded = false;
		this.maxHealth = HEALTH;

		this.shieldImage = new ShieldImage(INITIAL_X_POSITION, INITIAL_Y_POSITION);
		if (root != null) {
			root.getChildren().add(shieldImage);
		}

		this.healthBar = new Rectangle(100, 10);
		this.healthBar.setFill(Color.RED);
		this.healthBar.setLayoutX(INITIAL_X_POSITION);
		this.healthBar.setLayoutY(INITIAL_Y_POSITION + IMAGE_HEIGHT);
		if (root != null) {
			root.getChildren().add(healthBar);
		}

		setHitboxSize(IMAGE_HEIGHT * 0.8, IMAGE_HEIGHT * 0.3);

		this.hitboxOffsetX = 50;
		this.hitboxOffsetY = 100;

		initializeMovePattern();
	}

	/**
	 * Sets the offsets for the hitbox relative to the Boss's position.
	 *
	 * @param offsetX the horizontal offset.
	 * @param offsetY the vertical offset.
	 */
	public void setHitboxOffset(double offsetX, double offsetY) {
		this.hitboxOffsetX = offsetX;
		this.hitboxOffsetY = offsetY;
	}

	@Override
	public void updatePosition() {
		double initialTranslateY = getTranslateY();
		moveVertically(getNextMove());
		double currentPosition = getLayoutY() + getTranslateY();

		if (currentPosition < Y_POSITION_UPPER_BOUND || currentPosition > Y_POSITION_LOWER_BOUND) {
			setTranslateY(initialTranslateY);
		}

		shieldImage.setLayoutX(getLayoutX() + getTranslateX());
		shieldImage.setLayoutY(getLayoutY() + getTranslateY());
		healthBar.setLayoutX(getLayoutX() + getTranslateX());
		healthBar.setLayoutY(getLayoutY() + getTranslateY() + IMAGE_HEIGHT);

		updateHitbox();
	}

	@Override
	public void updateHitbox() {
		if (getHitbox() != null) {
			getHitbox().setLayoutX(getLayoutX() + getTranslateX() + hitboxOffsetX);
			getHitbox().setLayoutY(getLayoutY() + getTranslateY() + hitboxOffsetY);
		}
	}

	@Override
	public void updateActor() {
		updatePosition();
		updateShield();
		updateHealthBar();
	}

	@Override
	public ActiveActorDestructible fireProjectile() {
		if (Math.random() < BOSS_FIRE_RATE) {
			double xPos = getLayoutX() + getTranslateX();
			double yPos = getLayoutY() + getTranslateY() + PROJECTILE_Y_POSITION_OFFSET;
			double velocityX = -15;
			double velocityY = 0;

			BossProjectile projectile = new BossProjectile(xPos, yPos, velocityX, velocityY);
			System.out.println("BossProjectile created at: (" + xPos + ", " + yPos + ")");
			return projectile;
		}
		return null;
	}

	@Override
	public void takeDamage() {
		if (!isShielded) {
			super.takeDamage();
			updateHealthBar();
			if (getHealth() <= 0) {
				System.out.println("Boss defeated. Preparing second phase...");
				this.destroy();
			}
		}
	}

	/**
	 * Updates the health bar width based on the current health percentage.
	 */
	public void updateHealthBar() {
		double healthPercentage = Math.max(0, getHealth() / maxHealth);
		healthBar.setWidth(healthPercentage * 100);

		healthBar.setLayoutX(getLayoutX() + getTranslateX());
		healthBar.setLayoutY(getLayoutY() + getTranslateY() + IMAGE_HEIGHT);
	}

	/**
	 * Initializes the movement pattern for the Boss.
	 * The pattern alternates between moving up, moving down, and staying still.
	 * The pattern is randomized using {@link Collections#shuffle(List)}.
	 */
	private void initializeMovePattern() {
		for (int i = 0; i < MOVE_FREQUENCY_PER_CYCLE; i++) {
			movePattern.add(VERTICAL_VELOCITY); // Move down.
			movePattern.add(-VERTICAL_VELOCITY); // Move up.
			movePattern.add(ZERO); // Stay in place.
		}
		Collections.shuffle(movePattern); // Randomize the movement order.
	}


	/**
	 * Updates the shield status of the boss.
	 * <p>
	 * - If the shield is currently active, it increments the frame count and ensures the shield is visible.
	 * - If the shield should be activated (based on probability), it activates the shield and makes it visible.
	 * - If the shield has been active for its maximum duration, it deactivates the shield and hides it.
	 * </p>
	 */
	private void updateShield() {
		if (isShielded) {
			framesWithShieldActivated++;
			shieldImage.showShield();
		} else if (shieldShouldBeActivated()) {
			activateShield();
			shieldImage.showShield();
		}
		if (shieldExhausted()) {
			deactivateShield();
			shieldImage.hideShield();
		}
	}


	/**
	 * Determines the next move in the movement pattern for the Boss.
	 * Shuffles the movement pattern after a certain number of consecutive moves in the same direction.
	 *
	 * @return the next move in the movement pattern.
	 */
	private int getNextMove() {
		int currentMove = movePattern.get(indexOfCurrentMove);
		consecutiveMovesInSameDirection++;
		if (consecutiveMovesInSameDirection == MAX_FRAMES_WITH_SAME_MOVE) {
			Collections.shuffle(movePattern); // Randomize the movement pattern.
			consecutiveMovesInSameDirection = 0;
			indexOfCurrentMove++;
		}
		if (indexOfCurrentMove == movePattern.size()) {
			indexOfCurrentMove = 0; // Reset to the start of the pattern.
		}
		return currentMove;
	}


	/**
	 * Determines whether the shield should be activated based on a random probability.
	 *
	 * @return {@code true} if the shield activation condition is met (random value is less than the shield probability),
	 *         {@code false} otherwise.
	 */
	private boolean shieldShouldBeActivated() {
		return Math.random() < BOSS_SHIELD_PROBABILITY;
	}


	/**
	 * Checks if the shield has been active for the maximum allowed duration.
	 *
	 * @return {@code true} if the shield has been active for the maximum number of frames,
	 *         {@code false} otherwise.
	 */
	private boolean shieldExhausted() {
		return framesWithShieldActivated == MAX_FRAMES_WITH_SHIELD;
	}


	/**
	 * Activates the shield for the Boss.
	 * This method sets the shield status to active, making the Boss temporarily immune to damage.
	 */
	private void activateShield() {
		isShielded = true;
	}


	/**
	 * Deactivates the shield for the Boss.
	 * This method resets the shield status and the frame counter for the shield's activation duration.
	 * It ensures the shield is no longer visible and the Boss is vulnerable to attacks.
	 */
	private void deactivateShield() {
		isShielded = false;
		framesWithShieldActivated = 0;
	}


	/**
	 * Gets the shield image associated with the Boss.
	 *
	 * @return the shield image.
	 */
	public ShieldImage getShieldImage() {
		return shieldImage;
	}

	/**
	 * Gets the health bar associated with the Boss.
	 *
	 * @return the health bar.
	 */
	public Rectangle getHealthBar() {
		return healthBar;
	}
}
