package Ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Manages the display of hearts in the game, allowing for dynamic addition and removal of hearts.
 */
public class HeartDisplay {

	private static final String HEART_IMAGE_NAME = "/com/example/demo/images/heart.png";
	private static final int HEART_HEIGHT = 50;
	private static final int INDEX_OF_FIRST_ITEM = 0;

	private HBox container;
	private double containerXPosition;
	private double containerYPosition;

	/**
	 * Constructs a HeartDisplay object, initializing the heart container and the hearts to be displayed.
	 *
	 * @param xPosition       the x-coordinate of the heart container
	 * @param yPosition       the y-coordinate of the heart container
	 * @param heartsToDisplay the initial number of hearts to display
	 */
	public HeartDisplay(double xPosition, double yPosition, int heartsToDisplay) {
		this.containerXPosition = xPosition;
		this.containerYPosition = yPosition;
		initializeContainer();
		initializeHearts(heartsToDisplay);
	}

	/**
	 * Initializes the container that holds the hearts (HBox) and sets its position.
	 */
	private void initializeContainer() {
		container = new HBox();
		container.setLayoutX(containerXPosition);
		container.setLayoutY(containerYPosition);
	}

	/**
	 * Initializes the specified number of hearts and adds them to the container.
	 *
	 * @param heartsToDisplay the initial number of hearts to display
	 */
	private void initializeHearts(int heartsToDisplay) {
		for (int i = 0; i < heartsToDisplay; i++) {
			addHeart();
		}
	}

	/**
	 * Removes the first heart from the container, representing the loss of a life.
	 */
	public void removeHeart() {
		if (!container.getChildren().isEmpty()) {
			container.getChildren().remove(INDEX_OF_FIRST_ITEM);
		}
	}

	/**
	 * Dynamically adds a new heart to the container, representing the gain of a life.
	 */
	public void addHeart() {
		ImageView heart = new ImageView(new Image(getClass().getResource(HEART_IMAGE_NAME).toExternalForm()));
		heart.setFitHeight(HEART_HEIGHT);
		heart.setPreserveRatio(true);
		container.getChildren().add(heart);
	}

	/**
	 * Gets the container holding the hearts.
	 *
	 * @return the HBox containing the heart images
	 */
	public HBox getContainer() {
		return container;
	}

	/**
	 * Gets the current number of hearts displayed in the container.
	 *
	 * @return the number of hearts currently displayed
	 */
	public int getCurrentNumberOfHearts() {
		return container.getChildren().size();
	}
}
