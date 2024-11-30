package Entities;

import Core.ActiveActorDestructible;
import Core.FighterPlane;
import javafx.scene.Group;

public class UserPlane extends FighterPlane {

	private static final String IMAGE_NAME = "userplane.png";
	private static final double Y_UPPER_BOUND = -40;
	private static final double Y_LOWER_BOUND = 600.0;
	private static final double X_LEFT_BOUND = 0.0;
	private static final double X_RIGHT_BOUND = 1300.0;
	private static final double INITIAL_X_POSITION = 5.0;
	private static final double INITIAL_Y_POSITION = 300.0;
	private static final int IMAGE_HEIGHT = 150;
	private static final int VERTICAL_VELOCITY = 8;
	private static final int HORIZONTAL_VELOCITY = 8;
	private static final int PROJECTILE_X_POSITION_OFFSET = 60; // 调整子弹相对飞机的水平偏移量
	private static final int PROJECTILE_Y_POSITION_OFFSET = 10; // 调整子弹相对飞机的垂直偏移量

	private int verticalVelocityMultiplier;
	private int horizontalVelocityMultiplier;
	private int numberOfKills;
	private int health; // 玩家当前的生命值
	private final Group root;

	public UserPlane(int initialHealth, Group root) {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, initialHealth);
		this.health = initialHealth;
		verticalVelocityMultiplier = 0;
		horizontalVelocityMultiplier = 0;
		this.root = root;

		setHitboxSize(IMAGE_HEIGHT * 0.8, IMAGE_HEIGHT * 0.5);
		setHitboxOffset(0, 20);
		if (root != null) {
			visualizeHitbox(root);
		}
	}

	public UserPlane(int initialHealth) {
		this(initialHealth, null);
	}

	@Override
	public void updatePosition() {
		if (isMoving()) {
			double initialTranslateY = getTranslateY();
			double initialTranslateX = getTranslateX();

			this.moveVertically(VERTICAL_VELOCITY * verticalVelocityMultiplier);
			double newPositionY = getLayoutY() + getTranslateY();
			if (newPositionY < Y_UPPER_BOUND || newPositionY > Y_LOWER_BOUND) {
				this.setTranslateY(initialTranslateY);
			}

			this.moveHorizontally(HORIZONTAL_VELOCITY * horizontalVelocityMultiplier);
			double newPositionX = getLayoutX() + getTranslateX();
			if (newPositionX < X_LEFT_BOUND || newPositionX > X_RIGHT_BOUND) {
				this.setTranslateX(initialTranslateX);
			}
		}
		updateHitbox();
	}

	@Override
	public void updateActor() {
		updatePosition();
	}

	@Override
	public ActiveActorDestructible fireProjectile() {
		double projectileX = getLayoutX() + getTranslateX() + PROJECTILE_X_POSITION_OFFSET;
		double projectileY = getLayoutY() + getTranslateY() + PROJECTILE_Y_POSITION_OFFSET;

		UserProjectile projectile = new UserProjectile(projectileX, projectileY, root);
		if (root != null) {
			projectile.visualizeHitbox(root);
		}
		return projectile;
	}

	@Override
	public void takeDamage() {
		if (health > 0) {
			health--; // 减少生命值
			if (health <= 0) {
				destroy(); // 调用 destroy 方法标记对象已被摧毁
			}
		}
	}

	public void incrementHealth() {
		health++;
	}

	public int getHealth() {
		return health;
	}

	private boolean isMoving() {
		return verticalVelocityMultiplier != 0 || horizontalVelocityMultiplier != 0;
	}

	public void moveUp() {
		verticalVelocityMultiplier = -1;
	}

	public void moveDown() {
		verticalVelocityMultiplier = 1;
	}

	public void moveLeft() {
		horizontalVelocityMultiplier = -1;
	} 

	public void moveRight() {
		horizontalVelocityMultiplier = 1;
	}

	public void stopVertical() {
		verticalVelocityMultiplier = 0;
	}

	public void stopHorizontal() {
		horizontalVelocityMultiplier = 0;
	}

	public void stop() {
		stopVertical();
		stopHorizontal();
	}

	public int getNumberOfKills() {
		return numberOfKills;
	}

	public void incrementKillCount() {
		numberOfKills++;
	}
}
