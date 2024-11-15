###To implement the health bar and adjustable hitbox for the Boss character, the following steps were taken:

1. **Add Health Bar Below Boss**
   - Created a health bar below the Boss character, which decreases in size as the health decreases.
   - **Code snippet:**
     ```java
     // Initialize the health bar
     healthBar = new Rectangle(100, 10); // Initial width is 100, height is 10
     healthBar.setFill(Color.RED);       // Set color to red
     healthBar.setLayoutX(INITIAL_X_POSITION); 
     healthBar.setLayoutY(INITIAL_Y_POSITION + IMAGE_HEIGHT);
     ```

2. **Update Health Bar Width on Damage**
   - The width of the health bar adjusts based on the percentage of remaining health.
   - **Code snippet:**
     ```java
     public void updateHealthBar() {
         double healthPercentage = Math.max(0, getHealth() / maxHealth);
         healthBar.setWidth(healthPercentage * 100); // Set width based on health percentage
         healthBar.setLayoutX(getLayoutX() + getTranslateX());
         healthBar.setLayoutY(getLayoutY() + getTranslateY() + IMAGE_HEIGHT);
     }
     ```

3. **Adjustable Hitbox Size**
   - The hitbox size can be dynamically set using the `setHitboxSize()` method for flexible collision detection.
   - **Code snippet:**
     ```java
     public void setHitboxSize(double width, double height) {
         if (hitbox != null) {
             this.hitbox.setWidth(width);
             this.hitbox.setHeight(height);
             System.out.println("Set hitbox size: width=" + width + ", height=" + height);
         }
     }
     ```

4. **Synchronize Hitbox Position with Boss Movement**
   - Each time the Boss’s position is updated, the hitbox position is synchronized to ensure accurate collision detection.
   - **Code snippet:**
     ```java
     public void updateHitbox() {
         if (hitbox != null) {
             hitbox.setLayoutX(getLayoutX() + getTranslateX());
             hitbox.setLayoutY(getLayoutY() + getTranslateY());
             System.out.println("Updated hitbox position: x=" + hitbox.getLayoutX() + ", y=" + hitbox.getLayoutY());
         } else {
             System.out.println("Hitbox is null when updating position!");
         }
     }
     ```
