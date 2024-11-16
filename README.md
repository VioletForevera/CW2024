# 16/11 Test: Visible & Adjustable Hitbox Feature

## Feature Addition: Visualizing and Adjusting Hitboxes

### Issue
- **Problem:** The game lacked a visual representation of hitboxes, making collision debugging difficult.
- **Solution:** Added visual hitboxes and adjustable offsets for better debugging and gameplay experience.

### Solution
To implement visual and adjustable hitboxes, the following steps were taken:

1. **Add Hitbox Visualization**
   - Ensured that each game object's hitbox can be visualized on the screen for debugging purposes.
   - **Code snippet:**
     ```java
     public void visualizeHitbox(Group root) {
         if (hitbox != null && !root.getChildren().contains(hitbox)) {
             root.getChildren().add(hitbox); // Add hitbox to the scene
             System.out.println("Hitbox visualized in the scene.");
         } else {
             System.out.println("Hitbox is null or already visualized.");
         }
     }
     ```

2. **Synchronize Hitbox with Object Movement**
   - Ensured that the hitbox dynamically updates its position as the game object moves.
   - **Code snippet:**
     ```java
     public void updateHitbox() {
         if (hitbox != null) {
             hitbox.setLayoutX(getLayoutX() + getTranslateX() + hitboxOffsetX);
             hitbox.setLayoutY(getLayoutY() + getTranslateY() + hitboxOffsetY);
             System.out.println("Updated hitbox position: x=" + hitbox.getLayoutX() + ", y=" + hitbox.getLayoutY());
         } else {
             System.out.println("Hitbox is null when updating position!");
         }
     }
     ```

3. **Allow Adjustable Hitbox Offset**
   - Introduced variables to control the horizontal and vertical offsets of the hitbox relative to the object.
   - **Code snippet:**
     ```java
     public void setHitboxOffset(double offsetX, double offsetY) {
         this.hitboxOffsetX = offsetX;
         this.hitboxOffsetY = offsetY;
         System.out.println("Set hitbox offset: offsetX=" + offsetX + ", offsetY=" + offsetY);
     }
     ```

### Summary
This update introduces visible and customizable hitboxes, making it easier to debug and refine collision detection in the game. These changes provide developers and players with a clearer understanding of object boundaries.
