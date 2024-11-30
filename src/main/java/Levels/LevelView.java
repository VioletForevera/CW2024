package Levels;

import Ui.GameOverImage;
import Ui.HeartDisplay;
import Ui.WinImage;
import javafx.scene.Group;

/**
 * Represents the view layer of the level, responsible for displaying hearts, win and game over images,
 * and updating the UI as the game progresses.
 */
public class LevelView {

	private static final double HEART_DISPLAY_X_POSITION = 5;
	private static final double HEART_DISPLAY_Y_POSITION = 25;
	private static final int WIN_IMAGE_X_POSITION = 355;
	private static final int WIN_IMAGE_Y_POSITION = 175;
	private static final int LOSS_SCREEN_X_POSITION = -160;
	private static final int LOSS_SCREEN_Y_POSITION = -375;

	private final Group root;
	private final WinImage winImage;
	private final GameOverImage gameOverImage;
	private final HeartDisplay heartDisplay;

	/**
	 * Constructs a LevelView instance with the specified root group and number of hearts to display.
	 *
	 * @param root            the root container of the scene where the UI elements will be added.
	 * @param heartsToDisplay the initial number of hearts to be displayed.
	 */
	public LevelView(Group root, int heartsToDisplay) {
		this.root = root;
		this.heartDisplay = new HeartDisplay(HEART_DISPLAY_X_POSITION, HEART_DISPLAY_Y_POSITION, heartsToDisplay);
		this.winImage = new WinImage(WIN_IMAGE_X_POSITION, WIN_IMAGE_Y_POSITION);
		this.gameOverImage = new GameOverImage(LOSS_SCREEN_X_POSITION, LOSS_SCREEN_Y_POSITION);
	}

	/**
	 * Displays the heart display on the screen, showing the current number of hearts.
	 */
	public void showHeartDisplay() {
		if (!root.getChildren().contains(heartDisplay.getContainer())) {
			root.getChildren().add(heartDisplay.getContainer());
		}
	}

	/**
	 * Displays the win image on the screen when the player wins the level.
	 */
	public void showWinImage() {
		if (!root.getChildren().contains(winImage)) {
			root.getChildren().add(winImage);
			winImage.showWinImage();
		}
	}

	/**
	 * Displays the game over image on the screen when the player loses the game.
	 */
	public void showGameOverImage() {
		if (!root.getChildren().contains(gameOverImage)) {
			root.getChildren().add(gameOverImage);
		}
	}

	/**
	 * Removes hearts from the display when the player's health decreases.
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
	 * Adds hearts to the display when the player's health increases.
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
