package com.example.demo;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Boss extends FighterPlane {

	private static final String IMAGE_NAME = "bossplane.png";
	private static final double INITIAL_X_POSITION = 1000.0;
	private static final double INITIAL_Y_POSITION = 400;
	private static final double PROJECTILE_Y_POSITION_OFFSET = 75.0;
	private static final double BOSS_FIRE_RATE = 0.00004;
	private static final double BOSS_SHIELD_PROBABILITY = 0.002;
	private static final int IMAGE_HEIGHT = 300;
	private static final int VERTICAL_VELOCITY = 8;
	private static final int HEALTH = 100;
	private static final int MOVE_FREQUENCY_PER_CYCLE = 5;
	private static final int ZERO = 0;
	private static final int MAX_FRAMES_WITH_SAME_MOVE = 10;
	private static final int Y_POSITION_UPPER_BOUND = -100;
	private static final int Y_POSITION_LOWER_BOUND = 475;
	private static final int MAX_FRAMES_WITH_SHIELD = 500;

	private final List<Integer> movePattern;
	private boolean isShielded;
	private int consecutiveMovesInSameDirection;
	private int indexOfCurrentMove;
	private int framesWithShieldActivated;
	private final ShieldImage shieldImage;
	private final Rectangle healthBar; // 血条矩形
	private final double maxHealth; // 用于计算血条宽度

	// 无参数构造函数
	public Boss() {
		this(null); // 调用另一个构造函数，并传入 null
	}

	// 构造函数，接受 Group 作为参数
	public Boss(Group root) {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, HEALTH);
		this.movePattern = new ArrayList<>();
		this.consecutiveMovesInSameDirection = 0;
		this.indexOfCurrentMove = 0;
		this.framesWithShieldActivated = 0;
		this.isShielded = false;
		this.maxHealth = HEALTH;

		// 初始化护盾图像
		this.shieldImage = new ShieldImage(INITIAL_X_POSITION, INITIAL_Y_POSITION);
		if (root != null) {
			root.getChildren().add(shieldImage); // 将护盾图像添加到场景中
		}

		// 初始化血条
		healthBar = new Rectangle(100, 10); // 设置血条宽度为100，高度为10
		healthBar.setFill(Color.RED); // 设置血条颜色为红色
		healthBar.setLayoutX(INITIAL_X_POSITION); // 初始位置 X 坐标
		healthBar.setLayoutY(INITIAL_Y_POSITION + IMAGE_HEIGHT); // 紧贴在 Boss 下方
		if (root != null) {
			root.getChildren().add(healthBar); // 将血条添加到场景中
		}

		// 设置 hitbox 的大小
		setHitboxSize(IMAGE_HEIGHT * 9999, IMAGE_HEIGHT * 1);

		initializeMovePattern();
	}

	@Override
	public void updatePosition() {
		double initialTranslateY = getTranslateY();
		moveVertically(getNextMove());
		double currentPosition = getLayoutY() + getTranslateY();

		// 限制 Boss 的垂直位置
		if (currentPosition < Y_POSITION_UPPER_BOUND || currentPosition > Y_POSITION_LOWER_BOUND) {
			setTranslateY(initialTranslateY);
		}

		// 同步护盾和血条位置到 Boss 的位置
		shieldImage.setLayoutX(getLayoutX() + getTranslateX());
		shieldImage.setLayoutY(getLayoutY() + getTranslateY());
		healthBar.setLayoutX(getLayoutX() + getTranslateX());
		healthBar.setLayoutY(getLayoutY() + getTranslateY() + IMAGE_HEIGHT);

		// 更新 hitbox 的位置
		updateHitbox();
	}

	@Override
	public void updateActor() {
		updatePosition();
		updateShield();
		updateHealthBar();
	}

	@Override
	public ActiveActorDestructible fireProjectile() {
		if (Math.random() < BOSS_FIRE_RATE) {
			return new BossProjectile(getLayoutY() + getTranslateY() + PROJECTILE_Y_POSITION_OFFSET);
		}
		return null;
	}

	@Override
	public void takeDamage() {
		if (!isShielded) {
			super.takeDamage();
			updateHealthBar();
		}
	}

	public void updateHealthBar() {
		double healthPercentage = Math.max(0, getHealth() / maxHealth);
		healthBar.setWidth(healthPercentage * 100);

		healthBar.setLayoutX(getLayoutX() + getTranslateX());
		healthBar.setLayoutY(getLayoutY() + getTranslateY() + IMAGE_HEIGHT);
	}

	private void initializeMovePattern() {
		for (int i = 0; i < MOVE_FREQUENCY_PER_CYCLE; i++) {
			movePattern.add(VERTICAL_VELOCITY);
			movePattern.add(-VERTICAL_VELOCITY);
			movePattern.add(ZERO);
		}
		Collections.shuffle(movePattern);
	}

	private void updateShield() {
		if (isShielded) {
			framesWithShieldActivated++;
			shieldImage.showShield();
		} else if (shieldShouldBeActivated()) {
			activateShield();
			shieldImage.showShield();
		}
		if (shieldExhausted()) {
			deactivateShield();
			shieldImage.hideShield();
		}
	}

	private int getNextMove() {
		int currentMove = movePattern.get(indexOfCurrentMove);
		consecutiveMovesInSameDirection++;
		if (consecutiveMovesInSameDirection == MAX_FRAMES_WITH_SAME_MOVE) {
			Collections.shuffle(movePattern);
			consecutiveMovesInSameDirection = 0;
			indexOfCurrentMove++;
		}
		if (indexOfCurrentMove == movePattern.size()) {
			indexOfCurrentMove = 0;
		}
		return currentMove;
	}

	private boolean shieldShouldBeActivated() {
		return Math.random() < BOSS_SHIELD_PROBABILITY;
	}

	private boolean shieldExhausted() {
		return framesWithShieldActivated == MAX_FRAMES_WITH_SHIELD;
	}

	private void activateShield() {
		isShielded = true;
	}

	private void deactivateShield() {
		isShielded = false;
		framesWithShieldActivated = 0;
	}

	public ShieldImage getShieldImage() {
		return shieldImage;
	}

	public Rectangle getHealthBar() {
		return healthBar;
	}
}
