# 27/11 Updates Summary

- **Added**: **Win Menu** and **Game Over Menu**.
- **Implemented**: **Free Movement** for the player's plane (both vertically and horizontally).
- **Documented**: Known issues.


## Updates

### 1. **Win and Game Over Menus**

#### Description
- Introduced **Win Menu** and **Game Over Menu**, displayed after the player achieves victory or suffers defeat.
- Menus include the following functionalities:
  - **Exit Game**: Exits the application.
  - **Return to Menu**: Hides the current window and returns to the main menu to restart the game.

#### Implementation Details
- **EDIT Classes**:
  - `Ui.WinImage`: Displays the win menu with interactive buttons.
  - `Ui.GameOverImage`: Displays the game over menu with interactive buttons.
- **Logic**:
  - Each menu includes a background image and two buttons.
  - Implemented using `JavaFX` components such as `Button` and `ImageView`.
  - Button interactions are handled via the `setOnAction` method.
- **Styling**:
  - Buttons are styled using CSS to enhance aesthetics and interactivity.

#### Core Code
- **Return to Menu Logic**:
  ```java
  returnButton.setOnAction(event -> {
      System.out.println("Returning to main menu...");
      this.getScene().getWindow().hide();
      javax.swing.SwingUtilities.invokeLater(() -> {
          MainMenu mainMenu = new MainMenu();
          mainMenu.setVisible(true);
      });
  });
### 2. **Free Movement for the Plane**

#### Description
- Enabled free movement for the player's plane within the screen (both vertically and horizontally).
- Improved projectile logic to ensure bullets' positions align with the plane's current location.

#### Implementation Details
- **Modified Classes**:
  - `Entities.UserPlane`: Added logic for horizontal movement with `moveLeft()` and `moveRight()` methods.
  - `Entities.UserProjectile`: Synced bullet initialization with the plane's position.
- **Keyboard Input Handling**:
  Added key event bindings in the `LevelParent` class's `initializeBackground()` method:

  ```java
  background.setOnKeyPressed(e -> {
      KeyCode kc = e.getCode();
      if (kc == KeyCode.UP) user.moveUp();
      if (kc == KeyCode.DOWN) user.moveDown();
      if (kc == KeyCode.LEFT) user.moveLeft();
      if (kc == KeyCode.RIGHT) user.moveRight();
      if (kc == KeyCode.SPACE) fireProjectile();
  });
  background.setOnKeyReleased(e -> {
      KeyCode kc = e.getCode();
      if (kc == KeyCode.UP || kc == KeyCode.DOWN) user.stopVertical();
      if (kc == KeyCode.LEFT || kc == KeyCode.RIGHT) user.stopHorizontal();
  });
Known Issues
1. Error on Clicking Start Game After Returning to Menu
Issue: After navigating back to the main menu from the Win or Game Over screen, clicking "Start Game" results in the following error:
text

java.lang.IllegalStateException: Application launch must not be called more than once
Cause: The JavaFX Application.launch() method can only be called once. Restarting the game by invoking Main.main() triggers this error.
