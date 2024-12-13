package Ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Manages the display of hearts in the game, allowing for dynamic addition and removal of hearts.
 */
public class HeartDisplay {

	/**
	 * Path to the image used for rendering a heart.
	 * This constant specifies the file location of the heart image resource.
	 */
	private static final String HEART_IMAGE_NAME = "/com/example/demo/images/heart.png";

	/**
	 * The height of the heart image in pixels.
	 * Used to set the display size of each heart.
	 */
	private static final int HEART_HEIGHT = 50;

	/**
	 * Index of the first item in the container.
	 * Used for removing hearts from the display starting from the first element.
	 */
	private static final int INDEX_OF_FIRST_ITEM = 0;

	/**
	 * The container that holds all the heart images.
	 * Used to visually represent the player's health in the UI.
	 */
	private HBox container;

	/**
	 * The x-coordinate of the heart container on the screen.
	 * Determines the horizontal position of the heart display.
	 */
	private double containerXPosition;

	/**
	 * The y-coordinate of the heart container on the screen.
	 * Determines the vertical position of the heart display.
	 */
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


}
