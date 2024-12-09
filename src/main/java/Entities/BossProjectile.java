package Entities;

import Core.Projectile;

public class BossProjectile extends Projectile {

	private static final String IMAGE_NAME = "fireball.png";
	private static final int IMAGE_HEIGHT = 75;

	private double velocityX; // 水平速度
	private double velocityY; // 垂直速度

	// 构造器支持初始位置和速度
	public BossProjectile(double initialXPos, double initialYPos, double velocityX, double velocityY) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);
		this.velocityX = velocityX;
		this.velocityY = velocityY;
	}

	// 更新位置的方法
	@Override
	public void updatePosition() {
		moveHorizontally(velocityX);
		moveVertically(velocityY);
	}

	@Override
	public void updateActor() {
		updatePosition();
	}

	// 可选：用于设置或更新速度的方法
	public void setVelocity(double velocityX, double velocityY) {
		this.velocityX = velocityX;
		this.velocityY = velocityY;
	}
}
