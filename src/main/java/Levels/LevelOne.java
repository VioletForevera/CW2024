package Levels;

import Core.ActiveActorDestructible;
import Entities.EnemyPlane;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import sounds.MusicPlayer;

/**
 * Represents the first level of the game.
 * Handles spawning enemies, checking game-over conditions, and advancing to the next level.
 */
public class LevelOne extends LevelParent {

	/**
	 * Path to the background image for the level.
	 * Specifies the file location of the background image resource.
	 */
	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background1.png";

	/**
	 * The name of the next level to transition to after completing the current level.
	 * Used to dynamically load the next level.
	 */
	private static final String NEXT_LEVEL = "Levels.LevelTwo";

	/**
	 * The maximum number of enemies allowed to exist in the level at any given time.
	 * This value caps the number of enemies spawned concurrently.
	 */
	private static final int TOTAL_ENEMIES = 5;

	/**
	 * The number of kills required to advance to the next level.
	 * Players must achieve this target to progress in the game.
	 */
	private static final int KILLS_TO_ADVANCE = 10;

	/**
	 * The probability of spawning a new enemy during each game update cycle.
	 * Determines the likelihood of enemy planes appearing on the screen.
	 */
	private static final double ENEMY_SPAWN_PROBABILITY = 0.20;

	/**
	 * The initial health of the player's plane at the start of the level.
	 * Represents the total health points available to the player at the beginning of the game.
	 */
	private static final int PLAYER_INITIAL_HEALTH = 5;

	/**
	 * A progress bar used to visually represent the player's kill progress in the level.
	 * The progress bar updates dynamically as the player achieves kills.
	 */
	private final ProgressBar killProgressBar;

	/**
	 * A text element used to display detailed progress information for the player's kills.
	 * Shows the current number of kills compared to the target number of kills required to advance.
	 */
	private final Text killProgressText;


	/**
	 * Constructs a LevelOne instance.
	 *
	 * @param screenHeight the height of the game screen.
	 * @param screenWidth  the width of the game screen.
	 */
	public LevelOne(double screenHeight, double screenWidth) {
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH);

		// Ensure background music continues playing
		MusicPlayer musicPlayer = MusicPlayer.getInstance("/com/example/demo/images/sound.wav");
		musicPlayer.play();

		// Initialize the progress bar and text
		killProgressBar = new ProgressBar(0);
		killProgressBar.setPrefWidth(200);
		killProgressBar.setLayoutX(20); // Adjusted position
		killProgressBar.setLayoutY(screenHeight - 70);

		killProgressText = new Text("Kills: 0 / " + KILLS_TO_ADVANCE);
		killProgressText.setFont(new Font("Arial", 16));
		killProgressText.setFill(Color.WHITE);
		killProgressText.setLayoutX(250);
		killProgressText.setLayoutY(screenHeight - 60); // Align with progress bar

		// Add progress bar and text to the scene root
		getRoot().getChildren().addAll(killProgressBar, killProgressText);
	}

	/**
	 * Checks if the game is over or if the user has reached the kill target to advance to the next level.
	 * If the player's plane is destroyed, the game ends.
	 * If the player has achieved the required kills, the level advances.
	 */
	@Override
	protected void checkIfGameOver() {
		if (userIsDestroyed()) {
			loseGame();
		} else if (userHasReachedKillTarget()) {
			goToNextLevel(NEXT_LEVEL);
		}
		updateKillProgress();
	}

	/**
	 * Initializes friendly units, specifically the user's plane, and adds it to the game root.
	 */
	@Override
	protected void initializeFriendlyUnits() {
		getRoot().getChildren().add(getUser());
	}

	/**
	 * Spawns enemy planes in the game. The total number of enemies is capped at a predefined limit.
	 * Each enemy's spawn probability is determined by a random chance.
	 */
	@Override
	protected void spawnEnemyUnits() {
		int currentNumberOfEnemies = getCurrentNumberOfEnemies();
		for (int i = 0; i < TOTAL_ENEMIES - currentNumberOfEnemies; i++) {
			if (Math.random() < ENEMY_SPAWN_PROBABILITY) {
				double newEnemyInitialYPosition = Math.random() * getEnemyMaximumYPosition();
				ActiveActorDestructible newEnemy = new EnemyPlane(getScreenWidth(), newEnemyInitialYPosition, getRoot());
				addEnemyUnit(newEnemy);
			}
		}
	}

	/**
	 * Instantiates the LevelView for this level, setting the initial player's health display.
	 *
	 * @return the LevelView instance for this level.
	 */
	@Override
	protected LevelView instantiateLevelView() {
		return new LevelView(getRoot(), PLAYER_INITIAL_HEALTH);
	}

	/**
	 * Determines whether the user has achieved the required number of kills to advance to the next level.
	 *
	 * @return true if the player's kill count has reached or exceeded the target; false otherwise.
	 */
	public boolean userHasReachedKillTarget() {
		return getUser().getNumberOfKills() >= KILLS_TO_ADVANCE;
	}

	/**
	 * Updates the kill progress bar and the corresponding text.
	 */
	private void updateKillProgress() {
		int kills = getUser().getNumberOfKills();
		double progress = (double) kills / KILLS_TO_ADVANCE;
		killProgressBar.setProgress(progress);
		killProgressText.setText("Kills: " + kills + " / " + KILLS_TO_ADVANCE);
		killProgressBar.toFront();
		killProgressText.toFront();
		System.out.println("Updated progress: " + progress + ", Kills: " + kills);
	}
}
