package Entities;

import Core.ActiveActorDestructible;
import Core.FighterPlane;
import javafx.scene.Group;

public class EnemyPlane extends FighterPlane {

	private static final String IMAGE_NAME = "enemyplane.png";
	private static final int IMAGE_HEIGHT = 150;
	private static final int HORIZONTAL_VELOCITY = -6;
	private static final double PROJECTILE_X_POSITION_OFFSET = -100.0;
	private static final double PROJECTILE_Y_POSITION_OFFSET = 50.0;
	private static final int INITIAL_HEALTH = 1;
	private static final double FIRE_RATE = 0.01;

	private double horizontalOffset = 40.0; // 水平偏移量
	private double verticalOffset = 60.0;  // 垂直偏移量

	public EnemyPlane(double initialXPos, double initialYPos, Group root) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos, INITIAL_HEALTH);

		// 初始化 hitbox 的大小
		setHitboxSize(IMAGE_HEIGHT * 0.9, IMAGE_HEIGHT * 0.3);

		// 可视化 hitbox（仅用于调试）
		visualizeHitbox(root);
	}

	@Override
	public void updatePosition() {
		// 敌机向左移动
		moveHorizontally(HORIZONTAL_VELOCITY);

		// 确保 hitbox 同步
		updateHitbox();
	}

	@Override
	public void updateActor() {
		updatePosition();
	}

	@Override
	public ActiveActorDestructible fireProjectile() {
		if (Math.random() < FIRE_RATE) {
			double projectileXPosition = getProjectileXPosition(PROJECTILE_X_POSITION_OFFSET);
			double projectileYPosition = getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET);
			return new EnemyProjectile(projectileXPosition, projectileYPosition);
		}
		return null;
	}

	@Override
	public void updateHitbox() {
		if (getHitbox() != null) {
			// 在原始位置的基础上增加偏移量
			getHitbox().setLayoutX(getLayoutX() + getTranslateX() + horizontalOffset);
			getHitbox().setLayoutY(getLayoutY() + getTranslateY() + verticalOffset);

			System.out.println("Updated EnemyPlane hitbox position: x=" + getHitbox().getLayoutX()
					+ ", y=" + getHitbox().getLayoutY());
		} else {
			System.out.println("Hitbox is null when updating position!");
		}
	}

	// 设置水平和垂直偏移量的方法
	public void setHitboxOffsets(double horizontalOffset, double verticalOffset) {
		this.horizontalOffset = horizontalOffset;
		this.verticalOffset = verticalOffset;
		System.out.println("Set EnemyPlane hitbox offsets: horizontalOffset=" + horizontalOffset
				+ ", verticalOffset=" + verticalOffset);
	}

	// 可视化 hitbox（仅用于调试）
	public void visualizeHitbox(Group root) {
		if (getHitbox() != null && !root.getChildren().contains(getHitbox())) {
			root.getChildren().add(getHitbox());
			System.out.println("Visualizing EnemyPlane hitbox: " + getHitbox());
		}
	}
}
