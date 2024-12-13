package Levels;

import Core.ActiveActorDestructible;
import Entities.EnemyPlane;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Represents the second level of the game.
 * In this level, the player must defeat a specific number of enemies to advance to the next level.
 */
public class LevelTwo extends LevelParent {

    /**
     * Path to the background image for the second level.
     * This image will be displayed as the backdrop of the game scene.
     */
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background2.png";

    /**
     * The name of the next level class to transition to after completing this level.
     */
    private static final String NEXT_LEVEL = "Levels.LevelThree";

    /**
     * Total number of enemies that can appear in this level.
     * Limits the maximum number of enemies present at any given time.
     */
    private static final int TOTAL_ENEMIES = 8;

    /**
     * Number of kills required for the player to advance to the next level.
     * Players must reach this target to progress.
     */
    private static final int KILLS_TO_ADVANCE = 15;

    /**
     * Probability of spawning a new enemy during each game tick.
     * A higher value increases the likelihood of spawning enemies.
     */
    private static final double ENEMY_SPAWN_PROBABILITY = 0.30;

    /**
     * Initial health of the player's plane at the start of this level.
     */
    private static final int PLAYER_INITIAL_HEALTH = 5;

    /**
     * Progress bar for tracking the player's kill progress.
     * The bar visually represents how close the player is to reaching the target kills.
     */
    private final ProgressBar killProgressBar;

    /**
     * Text to display the current kill count and target kills.
     * Provides feedback to the player on their progress in the level.
     */
    private final Text killProgressText;


    /**
     * Constructs a new LevelTwo instance with the specified screen dimensions.
     *
     * @param screenHeight the height of the game screen.
     * @param screenWidth  the width of the game screen.
     */
    public LevelTwo(double screenHeight, double screenWidth) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH);

        // Initialize the progress bar and text
        killProgressBar = new ProgressBar(0);
        killProgressBar.setPrefWidth(200);
        killProgressBar.setLayoutX(20);
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
     * Checks if the game is over in this level.
     * Ends the game with a loss if the player's plane is destroyed,
     * or advances to the next level if the kill target is reached.
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
     * Initializes friendly units for this level by adding the player's plane to the game scene.
     */
    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
    }

    /**
     * Spawns enemy units for this level.
     * Ensures that the total number of enemies does not exceed the maximum allowed
     * and spawns new enemies based on a random probability.
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
     * Instantiates the level view for this level.
     * Manages UI elements such as health display and other level-specific details.
     *
     * @return a {@link LevelView} instance for this level.
     */
    @Override
    protected LevelView instantiateLevelView() {
        return new LevelView(getRoot(), PLAYER_INITIAL_HEALTH);
    }

    /**
     * Checks if the player has reached the required number of kills to advance to the next level.
     *
     * @return {@code true} if the player has reached the kill target, {@code false} otherwise.
     */
    private boolean userHasReachedKillTarget() {
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
