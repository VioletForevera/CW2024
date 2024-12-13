package Entities;

import Core.ActiveActorDestructible;
import javafx.scene.Group;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the first mutation of the Boss enemy in the game.
 * This class defines unique behaviors, including custom movement patterns and firing projectiles in multiple directions.
 */
public class MutationBoss1 extends Boss {

    /**
     * The file path to the image resource representing the visual appearance of the mutation entity.
     * This image is used for rendering the entity in the game.
     */
    private static final String IMAGE_PATH = "/com/example/demo/images/mutation1.png";

    /**
     * The initial health of the entity.
     * This value represents the total health points the entity starts with.
     */
    private static final int HEALTH = 10;

    /**
     * The vertical velocity of the entity.
     * This value defines how fast the entity moves vertically.
     */
    private static final int VERTICAL_VELOCITY = 8;

    /**
     * The upper boundary for the entity's vertical position.
     * Prevents the entity from moving above this value.
     */
    private static final int Y_POSITION_UPPER_BOUND = -100;

    /**
     * The lower boundary for the entity's vertical position.
     * Prevents the entity from moving below this value.
     */
    private static final int Y_POSITION_LOWER_BOUND = 475;

    /**
     * The maximum number of frames during which the entity can move in the same direction.
     * After reaching this limit, the movement direction is reset or shuffled.
     */
    private static final int MAX_FRAMES_WITH_SAME_MOVE = 20;

    /**
     * The firing rate for the boss entity.
     * This value defines the probability (0 to 1) of firing a projectile during each frame.
     */
    public static final double BOSS_FIRE_RATE = 0.1;

    /**
     * A list representing the movement pattern of the entity.
     * Contains values that define the vertical movement during each frame.
     */
    private final List<Integer> movePattern;

    /**
     * Tracks the current position or step in a sequence of moves or actions.
     * This variable is used to determine the index of the current move being processed or executed.
     */
    private int indexOfCurrentMove;

    /**
     * Tracks the number of frames spent in the current movement.
     * Used to manage movement patterns and determine when to change the move direction.
     */
    private int framesInCurrentMove;



    /**
     * Constructs a new instance of MutationBoss1 with default properties.
     * Initializes movement patterns and health.
     */
    public MutationBoss1() {
        super();
        setImage(new Image(getClass().getResourceAsStream(IMAGE_PATH)));
        setHealth(HEALTH);

        movePattern = new ArrayList<>();
        initializeMovePattern();
        indexOfCurrentMove = 0;
        framesInCurrentMove = 0;

        System.out.println("MutationBoss1 initialized.");
    }

    /**
     * Initializes the movement pattern of the boss.
     * Adds vertical movement options (up, down, stop) and shuffles them for random behavior.
     */
    private void initializeMovePattern() {
        for (int i = 0; i < 5; i++) {
            movePattern.add(VERTICAL_VELOCITY);  // Move down
            movePattern.add(-VERTICAL_VELOCITY); // Move up
            movePattern.add(0);                  // Stop
        }
        Collections.shuffle(movePattern); // Randomize the pattern
        System.out.println("Movement pattern initialized: " + movePattern);
    }

    /**
     * Updates the boss's position based on its current movement pattern.
     * Ensures that the boss stays within the screen boundaries and switches movement periodically.
     */
    @Override
    public void updatePosition() {
        double initialTranslateY = getTranslateY();

        int moveStep = movePattern.get(indexOfCurrentMove);
        moveVertically(moveStep);
        double currentPosition = getLayoutY() + getTranslateY();

        if (currentPosition < Y_POSITION_UPPER_BOUND || currentPosition > Y_POSITION_LOWER_BOUND) {
            setTranslateY(initialTranslateY); // Reset to a valid position
        }

        framesInCurrentMove++;
        if (framesInCurrentMove >= MAX_FRAMES_WITH_SAME_MOVE) {
            framesInCurrentMove = 0;
            indexOfCurrentMove = (indexOfCurrentMove + 1) % movePattern.size();
            System.out.println("Switching to next move: " + movePattern.get(indexOfCurrentMove));
        }

        updateHitbox();
    }

    /**
     * Updates the actor's state, including position and health bar.
     */
    @Override
    public void updateActor() {
        updatePosition();
        updateHealthBar();
    }

    /**
     * Fires projectiles in multiple directions with predefined velocities.
     * Adds the projectiles to the scene and an enemy projectiles list.
     *
     * @param root              the group to which projectiles will be added.
     * @param enemyProjectiles  the list tracking all enemy projectiles.
     */
    public void fireProjectile(Group root, List<ActiveActorDestructible> enemyProjectiles) {
        if (Math.random() < BOSS_FIRE_RATE) {
            double xPos = getLayoutX() + getTranslateX();
            double yPos = getLayoutY() + getTranslateY() + PROJECTILE_Y_POSITION_OFFSET;

            BossProjectile straightProjectile = new BossProjectile(xPos, yPos, -15, 0);
            BossProjectile leftUpProjectile = new BossProjectile(xPos, yPos - 50, -12, -5);
            BossProjectile leftDownProjectile = new BossProjectile(xPos, yPos + 50, -12, 5);

            System.out.println("Firing projectiles from position: (" + xPos + ", " + yPos + ")");
            System.out.println("Straight projectile velocity: (-15, 0)");
            System.out.println("Left-up projectile velocity: (-12, -5)");
            System.out.println("Left-down projectile velocity: (-12, 5)");

            root.getChildren().addAll(straightProjectile, leftUpProjectile, leftDownProjectile);
            enemyProjectiles.add(straightProjectile);
            enemyProjectiles.add(leftUpProjectile);
            enemyProjectiles.add(leftDownProjectile);

            System.out.println("enemyProjectiles size after addition: " + enemyProjectiles.size());
        }
    }

    /**
     * Handles the boss taking damage. If health reaches zero, it is marked as destroyed and removed from the scene.
     */
    @Override
    public void takeDamage() {
        if (getHealth() <= 0) {
            System.out.println("MutationBoss1 is already destroyed. Ignoring damage.");
            return;
        }

        super.takeDamage();
        System.out.println("MutationBoss1 took damage. Remaining health: " + getHealth());

        if (getHealth() <= 0) {
            System.out.println("MutationBoss1 has been defeated.");
            this.destroy();

            Group parent = (Group) getParent();
            if (parent != null) {
                parent.getChildren().remove(this);
                System.out.println("MutationBoss1 removed from scene.");
            }
        }
    }
}
