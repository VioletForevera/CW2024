package Levels;

import Entities.Boss;
import Entities.MutationBoss1;

/**
 * Represents the third level of the game.
 * This level includes both a {@link Boss} and a {@link MutationBoss1} as enemies.
 * The level ends when the player defeats the MutationBoss1 or loses all their health.
 */
public class LevelThree extends LevelParent {

	/**
	 * Path to the background image for this level.
	 */
	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background3.png";

	/**
	 * Initial health of the player's plane in this level.
	 */
	private static final int PLAYER_INITIAL_HEALTH = 5;

	/**
	 * The primary boss enemy for this level.
	 */
	private final Boss boss;

	/**
	 * The secondary MutationBoss1 enemy for this level.
	 */
	private final MutationBoss1 mutationBoss1;

	/**
	 * The view object responsible for managing UI elements for this level.
	 */
	private LevelViewLevelTwo levelView;

	/**
	 * Constructs a new LevelThree instance with specified screen dimensions.
	 *
	 * @param screenHeight the height of the game screen.
	 * @param screenWidth  the width of the game screen.
	 */
	public LevelThree(double screenHeight, double screenWidth) {
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH);
		boss = new Boss();
		mutationBoss1 = new MutationBoss1();
	}

	/**
	 * Initializes the friendly units for this level.
	 * Adds the player's plane to the game scene.
	 */
	@Override
	protected void initializeFriendlyUnits() {
		getRoot().getChildren().add(getUser());
	}

	/**
	 * Checks if the game is over in this level.
	 * Ends the game with a loss if the player's plane is destroyed,
	 * or a win if the MutationBoss1 is defeated.
	 */
	@Override
	protected void checkIfGameOver() {
		if (userIsDestroyed()) {
			loseGame();
		} else if (mutationBoss1.isDestroyed()) {
			winGame();
		}
	}

	/**
	 * Spawns enemy units for this level.
	 * Ensures that either the Boss or the MutationBoss1 is added to the game scene,
	 * depending on their destruction state.
	 */
	@Override
	protected void spawnEnemyUnits() {
		if (getCurrentNumberOfEnemies() == 0) {
			if (!boss.isDestroyed()) {
				addEnemyUnit(boss);
			} else if (!mutationBoss1.isDestroyed()) {
				addEnemyUnit(mutationBoss1);
			}
		}
	}

	/**
	 * Instantiates the level view for this level.
	 * Responsible for managing UI elements such as health display and level-specific details.
	 *
	 * @return the {@link LevelView} instance for this level.
	 */
	@Override
	protected LevelView instantiateLevelView() {
		levelView = new LevelViewLevelTwo(getRoot(), PLAYER_INITIAL_HEALTH);
		return levelView;
	}

}
