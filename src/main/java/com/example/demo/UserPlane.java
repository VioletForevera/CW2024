package com.example.demo;

import javafx.scene.Group;

public class UserPlane extends FighterPlane {

	private static final String IMAGE_NAME = "userplane.png";
	private static final double Y_UPPER_BOUND = -40;
	private static final double Y_LOWER_BOUND = 600.0;
	private static final double INITIAL_X_POSITION = 5.0;
	private static final double INITIAL_Y_POSITION = 300.0;
	private static final int IMAGE_HEIGHT = 150;
	private static final int VERTICAL_VELOCITY = 8;
	private static final int PROJECTILE_X_POSITION = 110;
	private static final int PROJECTILE_Y_POSITION_OFFSET = 20;
	private int velocityMultiplier;
	private int numberOfKills;
	private boolean isShielded; // 是否激活护盾
	private final ShieldImage shieldImage; // 护盾图像

	// 构造函数：接受初始生命值和根节点
	public UserPlane(int initialHealth, Group root) {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, initialHealth);
		velocityMultiplier = 0;
		isShielded = false;

		// 初始化护盾图像，并添加到场景
		shieldImage = new ShieldImage(INITIAL_X_POSITION, INITIAL_Y_POSITION);
		if (root != null) {
			root.getChildren().add(shieldImage); // 将护盾图像添加到场景中
		}
	}

	// 重载构造函数：只接受初始生命值
	public UserPlane(int initialHealth) {
		this(initialHealth, null); // 调用另一个构造函数
	}

	@Override
	public void updatePosition() {
		if (isMoving()) {
			double initialTranslateY = getTranslateY();
			this.moveVertically(VERTICAL_VELOCITY * velocityMultiplier);
			double newPosition = getLayoutY() + getTranslateY();
			if (newPosition < Y_UPPER_BOUND || newPosition > Y_LOWER_BOUND) {
				this.setTranslateY(initialTranslateY);
			}
		}
		// 更新护盾位置，使其与玩家位置同步
		shieldImage.setLayoutX(getLayoutX() + getTranslateX());
		shieldImage.setLayoutY(getLayoutY() + getTranslateY());
	}

	@Override
	public void updateActor() {
		updatePosition();
	}

	@Override
	public ActiveActorDestructible fireProjectile() {
		return new UserProjectile(PROJECTILE_X_POSITION, getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET));
	}

	private boolean isMoving() {
		return velocityMultiplier != 0;
	}

	public void moveUp() {
		velocityMultiplier = -1;
	}

	public void moveDown() {
		velocityMultiplier = 1;
	}

	public void stop() {
		velocityMultiplier = 0;
	}

	public int getNumberOfKills() {
		return numberOfKills;
	}

	public void incrementKillCount() {
		numberOfKills++;
	}

	// 激活护盾
	public void activateShield() {
		isShielded = true;
		shieldImage.showShield(); // 显示护盾图像
	}

	// 关闭护盾
	public void deactivateShield() {
		isShielded = false;
		shieldImage.hideShield(); // 隐藏护盾图像
	}

	public boolean isShieldActive() {
		return isShielded;
	}

	@Override
	public void takeDamage() {
		if (isShieldActive()) {
			// 护盾激活状态下，不扣除生命值，直接返回
			System.out.println("Shield is active, no damage taken.");
		} else {
			// 调用父类方法减少生命值
			super.takeDamage();
		}
	}

	// 获取护盾图像，以便在游戏场景中显示
	public ShieldImage getShieldImage() {
		return shieldImage;
	}
}
