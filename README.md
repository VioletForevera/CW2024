6/11Test-(bugfix) Branch
Bug Fixes: Scene Initialization, Enemy Kill Count, Boss Level Entry, and Enemy Management
Issues and Solutions
Fix for Duplicate Scene Initialization

Problem: The scene was initialized multiple times, leading to resource waste and potential display errors.
Solution: Added a flag isSceneInitialized in the initializeScene() method. This flag is checked before initializing to ensure the scene is only set up once.
Code snippet:
java
复制
public Scene initializeScene() {
    if (!isSceneInitialized) {
        initializeBackground();
        initializeFriendlyUnits();
        levelView.showHeartDisplay();
        isSceneInitialized = true;
    }
    return scene;
}
Corrected Enemy Kill Count Updates

Problem: Enemy kill count was not updating correctly upon enemy destruction, resulting in inaccurate scoring.
Solution: Updated the updateKillCount() method to increment the kill count whenever an enemy is destroyed. Removed destroyed enemies from the list and scene.
Code snippet:
java
复制代码
private void updateKillCount() {
    List<ActiveActorDestructible> destroyedEnemies = enemyUnits.stream()
        .filter(ActiveActorDestructible::isDestroyed)
        .collect(Collectors.toList());

    for (ActiveActorDestructible enemy : destroyedEnemies) {
        user.incrementKillCount();
    }

    enemyUnits.removeAll(destroyedEnemies);
    root.getChildren().removeAll(destroyedEnemies);
}
Ensured Proper Entry into the Boss Level

Problem: The game did not transition correctly to the Boss level when conditions were met, causing interruptions in gameplay flow.
Solution: Used an observer pattern in the main flow, enabling a seamless transition when conditions were met. Modified the goToNextLevel() method to notify observers when moving to the Boss level.
Code snippet:
java
复制代码
public void goToNextLevel(String levelName) {
    setChanged();
    notifyObservers(levelName);
}
Fixed Enemy Generation and Destruction Logic

Problem: Inconsistent logic in enemy generation and destruction led to display issues and improper removal of enemy objects from the screen.
Solution: Regularly spawn enemies in spawnEnemyUnits() and ensure that destroyed enemies are removed from the list and scene in removeAllDestroyedActors().
Code snippet:
java
复制代码
private void removeAllDestroyedActors() {
    removeDestroyedActors(enemyUnits);
}

private void removeDestroyedActors(List<ActiveActorDestructible> actors) {
    List<ActiveActorDestructible> destroyedActors = actors.stream()
        .filter(ActiveActorDestructible::isDestroyed)
        .collect(Collectors.toList());
    root.getChildren().removeAll(destroyedActors);
    actors.removeAll(destroyedActors);
}
Summary
These fixes ensure efficient scene initialization, accurate enemy kill tracking, smooth Boss level transition, and correct handling of enemy generation and destruction. This enhances game stability and overall user experience.
