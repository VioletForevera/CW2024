# Update: Pick-Up Heart Functionality

## Overview

This update introduces a **pick-up heart feature** to the game. Players can now collect hearts to restore health, and the number of hearts collected is dynamically reflected in the **UI**. Additionally, the player's health is accurately synchronized with the heart display. If the player's health drops to zero, they lose the game.

---

## Core Features

### Heart Functionality
- **Dynamic Heart Spawning**: Hearts spawn randomly from the right edge of the screen and move towards the left.
- **Heart Collection**: Players can collect hearts to restore health, dynamically updating the heart display in the UI.
- **Floating Movement**: Hearts float vertically while moving from right to left across the screen.

### UI Integration
- **Heart Display**: A dedicated heart display dynamically updates when hearts are gained or lost.
- **Synchronization**: The heart display accurately reflects the player's health at all times.
- **Game Over and Win UI**: Displays appropriate images when the player either wins or loses.

---

## Key Classes and Code Highlights

### **`Heart` Class**

The `Heart` class represents collectible hearts in the game, which can restore health upon collection.

**Key Features**:
- Spawns at random positions along the right edge of the screen.
- Moves from right to left and can be collected by the player.
- Marks itself as destroyed upon collection.

```java
public class Heart extends ImageView {
    private boolean isDestroyed;

    public Heart(double xPosition, double yPosition) {
        super(new Image(Heart.class.getResource("/path/to/heart.png").toExternalForm()));
        setFitHeight(50);
        setPreserveRatio(true);
        setX(xPosition);
        setY(yPosition);
        isDestroyed = false;
    }

    public void destroy() {
        isDestroyed = true;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }
}
HeartDisplay Class
The HeartDisplay class manages the display of hearts in the UI, representing the player's current health.

Key Features:

Dynamically adds or removes hearts based on health changes.
Displays hearts in a horizontal layout using an HBox container.
java
复制代码
public class HeartDisplay {
    private static final int HEART_HEIGHT = 50;
    private HBox container;

    public HeartDisplay(double xPosition, double yPosition, int heartsToDisplay) {
        container = new HBox();
        container.setLayoutX(xPosition);
        container.setLayoutY(yPosition);
        initializeHearts(heartsToDisplay);
    }

    private void initializeHearts(int heartsToDisplay) {
        for (int i = 0; i < heartsToDisplay; i++) {
            addHeart();
        }
    }

    public void addHeart() {
        ImageView heart = new ImageView(new Image("/path/to/heart.png"));
        heart.setFitHeight(HEART_HEIGHT);
        heart.setPreserveRatio(true);
        container.getChildren().add(heart);
    }

    public void removeHeart() {
        if (!container.getChildren().isEmpty()) {
            container.getChildren().remove(0);
        }
    }

    public HBox getContainer() {
        return container;
    }
}
LevelView Class
The LevelView class is responsible for managing the visual aspects of the level, such as displaying hearts, win images, and game over images.

Key Features:

Displays the player's current health using the HeartDisplay class.
Dynamically updates the heart display when hearts are gained or lost.
Manages the win and game-over UI elements.
java
复制代码
public class LevelView {
    private final HeartDisplay heartDisplay;
    private final WinImage winImage;
    private final GameOverImage gameOverImage;

    public LevelView(Group root, int heartsToDisplay) {
        this.heartDisplay = new HeartDisplay(5, 25, heartsToDisplay);
        this.winImage = new WinImage(355, 175);
        this.gameOverImage = new GameOverImage(-160, -375);
        root.getChildren().add(heartDisplay.getContainer());
    }

    public void showWinImage() {
        if (!root.getChildren().contains(winImage)) {
            root.getChildren().add(winImage);
            winImage.showWinImage();
        }
    }

    public void showGameOverImage() {
        if (!root.getChildren().contains(gameOverImage)) {
            root.getChildren().add(gameOverImage);
        }
    }

    public void addHearts(int heartsRemaining) {
        heartDisplay.addHeart();
    }

    public void removeHearts(int heartsRemaining) {
        heartDisplay.removeHeart();
    }
}
Heart Movement
Hearts now move from right to left across the screen:

Movement: Handled via a TranslateTransition animation.
Floating Effect: Added vertical jitter to simulate a floating effect.
Example Implementation in LevelParent:

java
复制代码
private void spawnHearts() {
    double spawnProbability = 0.01;
    if (Math.random() < spawnProbability) {
        double yPos = Math.random() * screenHeight;
        Heart heart = new Heart(screenWidth, yPos);
        hearts.add(heart);
        root.getChildren().add(heart);

        TranslateTransition moveLeft = new TranslateTransition(Duration.seconds(5), heart);
        moveLeft.setFromX(screenWidth);
        moveLeft.setToX(-50);
        moveLeft.play();

        // Floating effect
        TranslateTransition floatEffect = new TranslateTransition(Duration.seconds(1), heart);
        floatEffect.setFromY(yPos - 10);
        floatEffect.setToY(yPos + 10);
        floatEffect.setCycleCount(Animation.INDEFINITE);
        floatEffect.setAutoReverse(true);
        floatEffect.play();
    }
}
How to Use
Heart Collection: Players collect hearts as they move across the screen.
Heart Display: The heart display dynamically updates as health changes.
Game State: The game accurately reflects the player's health through the heart display.
