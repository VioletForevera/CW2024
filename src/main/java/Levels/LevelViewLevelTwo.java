package Levels;

import Ui.ShieldImage;
import javafx.scene.Group;

/**
 * Represents the view layer for Level Two, extending the base LevelView.
 * Adds specific UI elements for Level Two, such as the shield image.
 */
public class LevelViewLevelTwo extends LevelView {

	/**
	 * X-coordinate for the shield image in the scene.
	 */
	private static final int SHIELD_X_POSITION = 1150;

	/**
	 * Y-coordinate for the shield image in the scene.
	 */
	private static final int SHIELD_Y_POSITION = 500;

	/**
	 * The root container of the scene where all UI elements are added.
	 */
	private final Group root;

	/**
	 * The shield image displayed in Level Two.
	 */
	private final ShieldImage shieldImage;

	/**
	 * Constructs a LevelViewLevelTwo instance with the specified root and number of hearts to display.
	 * Initializes the shield image and adds it to the root.
	 *
	 * @param root            the root container of the scene where the UI elements will be added.
	 * @param heartsToDisplay the initial number of hearts to display.
	 */
	public LevelViewLevelTwo(Group root, int heartsToDisplay) {
		super(root, heartsToDisplay);
		this.root = root;
		this.shieldImage = new ShieldImage(SHIELD_X_POSITION, SHIELD_Y_POSITION);
		addImagesToRoot();
	}

	/**
	 * Adds the shield image to the root container, ensuring it is displayed in the scene.
	 */
	private void addImagesToRoot() {
		root.getChildren().addAll(shieldImage);
	}

	/**
	 * Displays the shield in the scene, making it visible to the player.
	 */
	public void showShield() {
		shieldImage.showShield();
	}

	/**
	 * Hides the shield in the scene, making it invisible to the player.
	 */
	public void hideShield() {
		shieldImage.hideShield();
	}
}
