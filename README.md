# 6/11Test-(bugfix) Branch

## Bug Fix: Various Improvements

### Issue
- **Problem:** Multiple bugs were present in the original code, affecting gameplay and user experience.
- **Cause:** Various issues in the code logic and flow led to inconsistent behavior.

### Solution
To address the identified bugs, the following steps were taken:

1. **Fixed Issue with Duplicate Scene Initialization**
   - Implemented checks to ensure the game scene is initialized only once, preventing multiple initializations that could lead to unexpected behavior.
   - **Code snippet:**
     ```java
     public Scene initializeScene() {
         if (!isSceneInitialized) {
             initializeBackground();
             initializeFriendlyUnits();
             levelView.showHeartDisplay();
             isSceneInitialized = true; // Prevents duplicate initialization
         }
         return scene;
     }
     ```

2. **Corrected Enemy Kill Count Updates When Enemies are Destroyed**
   - Updated the kill count logic to accurately reflect the number of enemies destroyed, ensuring the player's kill count is correctly incremented.
   - **Code snippet:**
     ```java
     private void updateKillCount() {
         List<ActiveActorDestructible> destroyedEnemies = enemyUnits.stream()
             .filter(ActiveActorDestructible::isDestroyed)
             .collect(Collectors.toList());
         
         for (ActiveActorDestructible enemy : destroyedEnemies) {
             user.incrementKillCount();
         }
         enemyUnits.removeAll(destroyedEnemies);
     }
     ```

3. **Ensured Proper Entry into the Boss Level**
   - Adjusted the logic to guarantee that players transition correctly into the boss level, addressing issues where players could skip levels or encounter glitches.
   - **Code snippet:**
     ```java
     public void goToNextLevel(String levelName) {
         if (levelName.equals("BossLevel")) {
             // Ensure player transitions properly to the Boss level
             setChanged();
             notifyObservers(levelName);
         }
     }
     ```

4. **Fixed Enemy Generation and Destruction Logic**
   - Improved the algorithms for spawning and removing enemy units, which ensures they appear and disappear correctly in the game world.
   - **Code snippet:**
     ```java
     private void spawnEnemyUnits() {
         // Logic to spawn enemies at the correct intervals and positions
     }

     private void removeAllDestroyedActors() {
         enemyUnits.removeIf(actor -> actor.isDestroyed());
     }
     ```

### Summary
This update resolves several critical bugs in the gameplay experience, enhancing both performance and player satisfaction.

