### Added Features: Collision Management, Hitbox Management, and Level Three Integration
To enhance the game and improve its modularity, the following features were implemented:

1. **Collision Manager**
The CollisionManager class was created to handle all collision logic between actors in the game. This ensures precise and efficient collision detection and resolution.

Responsibilities:
Detect collisions between two lists of destructible actors.
Handle the effects of collisions, such as reducing health or destroying objects.
Key Logic:
- **Code snippet:**
```java
public class CollisionManager {
    public static void handleCollisions(List<ActiveActorDestructible> actors1, List<ActiveActorDestructible> actors2) {
        for (ActiveActorDestructible actor : actors1) {
            for (ActiveActorDestructible otherActor : actors2) {
                if (actor instanceof ActiveActor && otherActor instanceof ActiveActor) {
                    Node actorHitbox = ((ActiveActor) actor).getHitbox();
                    Node otherActorHitbox = ((ActiveActor) otherActor).getHitbox();
                    if (actorHitbox.getBoundsInParent().intersects(otherActorHitbox.getBoundsInParent())) {
                        resolveCollision(actor, otherActor);
                    }
                }
            }
        }
    }

    private static void resolveCollision(ActiveActorDestructible actor, ActiveActorDestructible otherActor) {
        actor.takeDamage();
        otherActor.takeDamage();

        if (actor.isDestroyed()) actor.destroy();
        if (otherActor.isDestroyed()) otherActor.destroy();
    }
}
```
2. **Hitbox Manager**
The HitboxManager class was developed to manage the hitboxes of all active actors in the game, facilitating accurate collision detection and debugging.

Responsibilities:
Register and unregister actors with hitboxes.
Update the positions and sizes of hitboxes dynamically.
Visualize hitboxes for debugging purposes.
Key Logic:
- **Code snippet:**
     ```java
public class HitboxManager {
    private final List<ActiveActor> actors = new ArrayList<>();

    public void registerActor(ActiveActor actor) {
        if (!actors.contains(actor)) {
            actors.add(actor);
            System.out.println("Actor registered with hitbox.");
        }
    }

    public void unregisterActor(ActiveActor actor) {
        actors.remove(actor);
        System.out.println("Actor unregistered from hitbox manager.");
    }

    public void updateHitboxes() {
        for (ActiveActor actor : actors) {
            actor.updateActorHitbox();
        }
    }

    public boolean checkCollision(ActiveActor actor1, ActiveActor actor2) {
        return actor1.getHitbox().getBoundsInParent().intersects(actor2.getHitbox().getBoundsInParent());
    }

    public void visualizeAllHitboxes(Group root) {
        for (ActiveActor actor : actors) {
            actor.visualizeHitbox(root);
        }
    }

    public Rectangle createHitbox() {
        Rectangle hitbox = new Rectangle(50, 50);
        hitbox.setFill(javafx.scene.paint.Color.TRANSPARENT);
        hitbox.setStroke(javafx.scene.paint.Color.RED);
        return hitbox;
    }
}
```
3. **Level Three: New Gameplay Layer**
The LevelThree class was added to introduce a new, challenging level. It integrates seamlessly with the CollisionManager and HitboxManager.

Features of Level Three:
A unique background image.
Increased enemy spawn probability.
A kill target requirement for progression.
Dynamic enemy spawning logic.
Key Logic:
- **Code snippet:**
     ```java
public class LevelThree extends LevelParent {
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background3.png";
    private static final String NEXT_LEVEL = "Levels.LevelTwo";
    private static final int TOTAL_ENEMIES = 8;
    private static final int KILLS_TO_ADVANCE = 15;
    private static final double ENEMY_SPAWN_PROBABILITY = 0.30;
    private static final int PLAYER_INITIAL_HEALTH = 5;

    public LevelThree(double screenHeight, double screenWidth) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH);
    }

    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            loseGame();
        } else if (userHasReachedKillTarget()) {
            goToNextLevel(NEXT_LEVEL);
        }
    }

    @Override
    protected void spawnEnemyUnits() {
        int currentNumberOfEnemies = getCurrentNumberOfEnemies();
        for (int i = 0; i < TOTAL_ENEMIES - currentNumberOfEnemies; i++) {
            if (Math.random() < ENEMY_SPAWN_PROBABILITY) {
                double newEnemyInitialYPosition = Math.random() * getEnemyMaximumYPosition();
                ActiveActorDestructible newEnemy = new EnemyPlane(getScreenWidth(), newEnemyInitialYPosition, getRoot());
                addEnemyUnit(newEnemy);
            }
        }
    }

    private boolean userHasReachedKillTarget() {
        return getUser().getNumberOfKills() >= KILLS_TO_ADVANCE;
    }
}
```
Integration Workflow
Collision Detection with CollisionManager:
LevelThree uses the CollisionManager to resolve collisions between the player and enemies dynamically.
Hitbox Management with HitboxManager:
The hitboxes for all actors in LevelThree are registered and updated through HitboxManager.
Hitbox positions and sizes are dynamically updated to align with actor movement.
Enhanced Gameplay Experience in LevelThree:
The addition of a custom background and unique spawning logic offers a challenging gameplay environment.
