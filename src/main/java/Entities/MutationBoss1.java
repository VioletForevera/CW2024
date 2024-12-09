package Entities;

import Core.ActiveActorDestructible;
import javafx.scene.Group;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MutationBoss1 extends Boss {

    private static final String IMAGE_PATH = "/com/example/demo/images/mutation1.png";
    private static final int IMAGE_HEIGHT = 250;
    private static final int HEALTH = 120;
    private static final int VERTICAL_VELOCITY = 8;
    private static final int Y_POSITION_UPPER_BOUND = -100;
    private static final int Y_POSITION_LOWER_BOUND = 475;
    private static final int MAX_FRAMES_WITH_SAME_MOVE = 20;
    public static final double BOSS_FIRE_RATE = 0.1;
    private final List<Integer> movePattern;
    private int indexOfCurrentMove;
    private int framesInCurrentMove;
    private List<ActiveActorDestructible> enemyProjectiles;

    public MutationBoss1() {
        super();
        setImage(new Image(getClass().getResourceAsStream(IMAGE_PATH)));
        setHealth(HEALTH);
        setHitboxSize(IMAGE_HEIGHT * 0.8, IMAGE_HEIGHT * 0.6);

        movePattern = new ArrayList<>();
        initializeMovePattern();
        indexOfCurrentMove = 0;
        framesInCurrentMove = 0;

        System.out.println("MutationBoss1 initialized.");
    }

    public MutationBoss1(Group root) {
        this();
        if (root != null) {
            root.getChildren().add(getShieldImage());
            root.getChildren().add(getHealthBar());
            System.out.println("MutationBoss1 added to the scene.");
        }
    }

    private void initializeMovePattern() {
        // 添加多种垂直移动模式：上移、下移、停留
        for (int i = 0; i < 5; i++) {
            movePattern.add(VERTICAL_VELOCITY);  // 向下移动
            movePattern.add(-VERTICAL_VELOCITY); // 向上移动
            movePattern.add(0);                  // 停止
        }
        Collections.shuffle(movePattern); // 打乱模式，增加随机性
        System.out.println("Movement pattern initialized: " + movePattern);
    }

    @Override
    public void updatePosition() {
        double initialTranslateY = getTranslateY();

        // 获取当前移动步伐
        int moveStep = movePattern.get(indexOfCurrentMove);

        // 执行移动
        moveVertically(moveStep);
        double currentPosition = getLayoutY() + getTranslateY();

        // 边界检查，确保在屏幕范围内
        if (currentPosition < Y_POSITION_UPPER_BOUND || currentPosition > Y_POSITION_LOWER_BOUND) {
            setTranslateY(initialTranslateY); // 恢复到合法位置
        }

        // 切换到下一个移动模式
        framesInCurrentMove++;
        if (framesInCurrentMove >= MAX_FRAMES_WITH_SAME_MOVE) {
            framesInCurrentMove = 0;
            indexOfCurrentMove = (indexOfCurrentMove + 1) % movePattern.size();
            System.out.println("Switching to next move: " + movePattern.get(indexOfCurrentMove));
        }

        // 同步护盾和血条位置
        getShieldImage().setLayoutX(getLayoutX() + getTranslateX());
        getShieldImage().setLayoutY(getLayoutY() + getTranslateY());
        getHealthBar().setLayoutX(getLayoutX() + getTranslateX());
        getHealthBar().setLayoutY(getLayoutY() + getTranslateY() + IMAGE_HEIGHT);

        // 更新碰撞箱位置
        updateHitbox();
    }



    @Override
    public void updateActor() {
        updatePosition();  // 更新位置
        updateHealthBar(); // 更新血条

    }

    public void fireProjectile(Group root, List<ActiveActorDestructible> enemyProjectiles) {
        if (Math.random() < BOSS_FIRE_RATE) {
            double xPos = getLayoutX() + getTranslateX();
            double yPos = getLayoutY() + getTranslateY() + PROJECTILE_Y_POSITION_OFFSET;

            // 创建三个方向的子弹
            BossProjectile straightProjectile = new BossProjectile(xPos, yPos, -15, 0);
            BossProjectile leftUpProjectile = new BossProjectile(xPos, yPos - 50, -12, -5);
            BossProjectile leftDownProjectile = new BossProjectile(xPos, yPos + 50, -12, 5);

            // 调试信息
            System.out.println("Firing projectiles from position: (" + xPos + ", " + yPos + ")");
            System.out.println("Straight projectile velocity: (-15, 0)");
            System.out.println("Left-up projectile velocity: (-12, -5)");
            System.out.println("Left-down projectile velocity: (-12, 5)");

            // 将子弹添加到场景和列表
            root.getChildren().addAll(straightProjectile, leftUpProjectile, leftDownProjectile);
            enemyProjectiles.add(straightProjectile);
            enemyProjectiles.add(leftUpProjectile);
            enemyProjectiles.add(leftDownProjectile);

            // 确认添加
            System.out.println("enemyProjectiles size after addition: " + enemyProjectiles.size());
        }
    }




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
            this.destroy(); // 标记为已销毁

            // 从场景和敌人列表中移除
            Group parent = (Group) getParent();
            if (parent != null) {
                parent.getChildren().remove(this);
                System.out.println("MutationBoss1 removed from scene.");
            }
        }
    }
}
