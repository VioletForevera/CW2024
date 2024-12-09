### Game Project Update: Mutation Boss, Pause Function, and New Sounds
Overview
This update introduces several exciting features and improvements to the game, including a new boss character with unique attack patterns, a pause functionality for better gameplay control, and enhanced audio effects for a more immersive experience. Below, we outline the major additions, refactoring, and updates made in this release.

Key Features
1. **Mutation Boss with Special Attack Patterns**
The MutationBoss1 class is a new boss type introduced in this update. It inherits from the Boss class and features unique movement and attack patterns:

Randomized Movement: The boss has a predefined movement pattern (up, down, stay) that is shuffled to create dynamic behavior.
Triple Projectile Attack: The boss can fire projectiles in three directions — straight, left-up, and left-down — making it more challenging for the player.
Highlighted Code Snippet:

**Code snippet:**
```java
public void fireProjectile(Group root, List<ActiveActorDestructible> enemyProjectiles) {
    if (Math.random() < BOSS_FIRE_RATE) {
        double xPos = getLayoutX() + getTranslateX();
        double yPos = getLayoutY() + getTranslateY() + PROJECTILE_Y_POSITION_OFFSET;

        // Creating projectiles
        BossProjectile straightProjectile = new BossProjectile(xPos, yPos, -15, 0);
        BossProjectile leftUpProjectile = new BossProjectile(xPos, yPos - 50, -12, -5);
        BossProjectile leftDownProjectile = new BossProjectile(xPos, yPos + 50, -12, 5);

        // Add projectiles to scene and list
        root.getChildren().addAll(straightProjectile, leftUpProjectile, leftDownProjectile);
        enemyProjectiles.add(straightProjectile);
        enemyProjectiles.add(leftUpProjectile);
        enemyProjectiles.add(leftDownProjectile);

        System.out.println("Firing projectiles from position: (" + xPos + ", " + yPos + ")");
    }
}
```
The Mutation Boss also includes health management, collision detection, and shield synchronization with its movements.

2. **Pause Functionality**
A pause menu has been added to allow players to temporarily halt gameplay. This includes:

A resume button to continue the game.
A quit button to exit the game.
A translucent menu with intuitive design to enhance usability.
Highlighted Code Snippet:

**Code snippet:**
```java
private void togglePause() {
    isPaused = !isPaused;
    if (isPaused) {
        System.out.println("Game paused.");
        timeline.pause();
        pauseMenu.setVisible(true);
        pauseMenu.toFront();
    } else {
        System.out.println("Game resumed.");
        timeline.play();
        pauseMenu.setVisible(false);
    }
}
```
The pause menu can be triggered by pressing the P key during gameplay.

3. **Enhanced Sound Effects**
New audio effects have been added to make the game more engaging:

Explosion sound: Plays when enemy planes are destroyed.
Damage sound: Plays when the user takes damage.
These effects are seamlessly integrated into the existing game events, providing better feedback for players.
**Code snippet:**
```java
public void takeDamage() {
		health--;
		System.out.println("FighterPlane took damage. Remaining health: " + health);
		if (healthAtZero()) {
			// Play the explosion sound effect and set the volume to 70%.
			MusicPlayer.playEffect("/com/example/demo/images/explosion.wav", 0.7f);
			this.destroy();
			System.out.println("FighterPlane destroyed.");
		}
	}
```
4. **Off-Screen Projectile Cleanup**
To optimize performance, projectiles that leave the screen are now automatically removed. This prevents memory leaks and ensures smoother gameplay.

Highlighted Code Snippet:

**Code snippet:**
```java
private void handleEnemyPenetration() {
    for (ActiveActorDestructible enemy : enemyUnits) {
        if (enemyHasPenetratedDefenses(enemy)) {
            user.takeDamage();
            enemy.destroy();
        }
    }

    for (ActiveActorDestructible projectile : enemyProjectiles) {
        if (actorHasPenetratedDefenses(projectile)) {
            projectile.destroy();
        }
    }

    for (ActiveActorDestructible projectile : userProjectiles) {
        if (actorHasPenetratedDefenses(projectile)) {
            projectile.destroy();
        }
    }
}

private boolean actorHasPenetratedDefenses(ActiveActorDestructible actor) {
    return Math.abs(actor.getTranslateX()) > 1000;
}
```
This method ensures that all projectiles and enemy units exceeding screen boundaries are destroyed to maintain memory efficiency.

Updated Files
The following files were modified in this update:

Core/FighterPlane.java
Entities/Boss.java
Entities/BossProjectile.java
Entities/MutationBoss1.java
Entities/UserPlane.java
Levels/LevelParent.java
Levels/LevelThree.java
Ui/MainMenu.java
com/example/demo/controller/Controller.java
com/example/demo/controller/Main.java
sounds/MusicPlayer.java
resources/images/background1.png
resources/images/background2.png
resources/images/background3.png
Newly Added Assets
resources/images/explosion.wav: Sound effect for explosions.
resources/images/mutation1.png: Sprite image for the Mutation Boss.
