package Levels;

import Ui.GameOverImage;
import Ui.HeartDisplay;
import Ui.WinImage;
import javafx.scene.Group;

/**
 * Represents the view layer of a level, managing the UI elements such as hearts, win image,
 * and game over image. Responsible for updating the visual components as the game progresses.
 */
public class LevelView {

	/**
	 * X-coordinate for the heart display on the screen.
	 */
	private static final double HEART_DISPLAY_X_POSITION = 5;

	/**
	 * Y-coordinate for the heart display on the screen.
	 */
	private static final double HEART_DISPLAY_Y_POSITION = 25;

	/**
	 * X-coordinate for the win image on the screen.
	 */
	private static final int WIN_IMAGE_X_POSITION = 355;

	/**
	 * Y-coordinate for the win image on the screen.
	 */
	private static final int WIN_IMAGE_Y_POSITION = 175;

	/**
	 * X-coordinate for the game over image on the screen.
	 */
	private static final int LOSS_SCREEN_X_POSITION = -160;

	/**
	 * Y-coordinate for the game over image on the screen.
	 */
	private static final int LOSS_SCREEN_Y_POSITION = -375;

	/**
	 * The root container of the scene where all UI elements will be added.
	 */
	private final Group root;

	/**
	 * The win image displayed when the player wins the level.
	 */
	private final WinImage winImage;

	/**
	 * The game over image displayed when the player loses the level.
	 */
	private final GameOverImage gameOverImage;

	/**
	 * The heart display showing the player's remaining health.
	 */
	private final HeartDisplay heartDisplay;

	/**
	 * Constructs a LevelView instance with the specified root group and initial number of hearts.
	 *
	 * @param root            the root container of the scene where the UI elements will be added.
	 * @param heartsToDisplay the initial number of hearts to display.
	 */
	public LevelView(Group root, int heartsToDisplay) {
		this.root = root;
		this.heartDisplay = new HeartDisplay(HEART_DISPLAY_X_POSITION, HEART_DISPLAY_Y_POSITION, heartsToDisplay);
		this.winImage = new WinImage(WIN_IMAGE_X_POSITION, WIN_IMAGE_Y_POSITION);
		this.gameOverImage = new GameOverImage(LOSS_SCREEN_X_POSITION, LOSS_SCREEN_Y_POSITION);
	}

	/**
	 * Displays the heart display in the scene, showing the player's remaining health.
	 * Ensures the heart display is only added once to the root container.
	 */
	public void showHeartDisplay() {
		if (!root.getChildren().contains(heartDisplay.getContainer())) {
			root.getChildren().add(heartDisplay.getContainer());
		}
	}

	/**
	 * Displays the win image on the screen when the player wins the level.
	 * Ensures the win image is only added once to the root container.
	 */
	public void showWinImage() {
		if (!root.getChildren().contains(winImage)) {
			root.getChildren().add(winImage);
			winImage.showWinImage();
		}
	}

	/**
	 * Displays the game over image on the screen when the player loses the game.
	 * Ensures the game over image is only added once to the root container.
	 */
	public void showGameOverImage() {
		if (!root.getChildren().contains(gameOverImage)) {
			root.getChildren().add(gameOverImage);
		}
	}

	/**
	 * Removes hearts from the heart display when the player's health decreases.
	 *
	 * @param heartsRemaining the current number of hearts to display after removal.
	 */
	public void removeHearts(int heartsRemaining) {
		int currentNumberOfHearts = heartDisplay.getContainer().getChildren().size();
		int heartsToRemove = currentNumberOfHearts - heartsRemaining;
		for (int i = 0; i < heartsToRemove; i++) {
			heartDisplay.removeHeart();
		}
	}

	/**
	 * Adds hearts to the heart display when the player's health increases.
	 *
	 * @param heartsRemaining the current number of hearts to display after addition.
	 */
	public void addHearts(int heartsRemaining) {
		int currentNumberOfHearts = heartDisplay.getContainer().getChildren().size();
		int heartsToAdd = heartsRemaining - currentNumberOfHearts;
		for (int i = 0; i < heartsToAdd; i++) {
			heartDisplay.addHeart();
		}
	}
}
