package Entities;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents a heart in the game, which can act as a health indicator or a collectible item.
 */
public class Heart extends ImageView {

    private static final String HEART_IMAGE_PATH = "/com/example/demo/images/heart.png";
    private boolean isDestroyed;

    /**
     * Constructs a Heart object at a specified position.
     *
     * @param xPosition the x-coordinate of the heart's position.
     * @param yPosition the y-coordinate of the heart's position.
     */
    public Heart(double xPosition, double yPosition) {
        super(new Image(Heart.class.getResource(HEART_IMAGE_PATH).toExternalForm()));
        setFitHeight(50); // Set the height of the heart
        setPreserveRatio(true); // Maintain the aspect ratio
        setX(xPosition);
        setY(yPosition);
        this.isDestroyed = false;
    }

    /**
     * Marks the heart as destroyed.
     */
    public void destroy() {
        isDestroyed = true;
    }

    /**
     * Checks if the heart is destroyed.
     *
     * @return true if the heart is destroyed, false otherwise.
     */
    public boolean isDestroyed() {
        return isDestroyed;
    }

    /**
     * Updates the heart's state. This can include animation or movement logic if needed.
     */
    public void updateActor() {
        // Implement any behavior updates for the heart if necessary
        // For example, animations or interactions
    }
}
