package com.example.demo;

public class EnemyPlane extends FighterPlane {

	private static final String IMAGE_NAME = "enemyplane.png";
	private static final int IMAGE_HEIGHT = 150;
	private static final int HORIZONTAL_VELOCITY = -6;
	private static final double PROJECTILE_X_POSITION_OFFSET = -100.0;
	private static final double PROJECTILE_Y_POSITION_OFFSET = 50.0;
	private static final int INITIAL_HEALTH = 1;
	private static final double FIRE_RATE = 0.01;

	public EnemyPlane(double initialXPos, double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos, INITIAL_HEALTH);

		// 设置较小的 hitbox 尺寸
		setHitboxSize(IMAGE_HEIGHT * 0.7, IMAGE_HEIGHT * 0.5); // 宽度为图片高度的 50%，高度为图片高度的 30%
	}

	@Override
	public void updatePosition() {
		moveHorizontally(HORIZONTAL_VELOCITY);
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
	public void updateActor() {
		updatePosition();
		updateHitbox(); // 更新 hitbox 的位置
	}
}
