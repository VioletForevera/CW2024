package Entities;

import Core.ActiveActorDestructible;
import Core.FighterPlane;
import Ui.ShieldImage;
import javafx.scene.Group;

public class UserPlane extends FighterPlane {

	private static final String IMAGE_NAME = "userplane.png";
	private static final double Y_UPPER_BOUND = -40;
	private static final double Y_LOWER_BOUND = 600.0;
	private static final double INITIAL_X_POSITION = 5.0;
	private static final double INITIAL_Y_POSITION = 300.0;
	private static final int IMAGE_HEIGHT = 150;
	private static final int VERTICAL_VELOCITY = 8;
	private static final int PROJECTILE_X_POSITION_OFFSET = 110;
	private static final int PROJECTILE_Y_POSITION_OFFSET = 20;

	private int velocityMultiplier;
	private int numberOfKills;
	private boolean isShielded; // 是否激活护盾
	private final ShieldImage shieldImage; // 护盾图像
	private final Group root; // 场景根节点

	// 构造函数：接受初始生命值和根节点
	public UserPlane(int initialHealth, Group root) {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, initialHealth);
		velocityMultiplier = 0;
		isShielded = false;
		this.root = root;

		// 初始化护盾图像，并添加到场景
		shieldImage = new ShieldImage(INITIAL_X_POSITION, INITIAL_Y_POSITION);
		if (root != null) {
			root.getChildren().add(shieldImage); // 将护盾图像添加到场景中
		}

		// 初始化 hitbox 并可视化
		setHitboxSize(IMAGE_HEIGHT * 0.8, IMAGE_HEIGHT * 0.5); // 设置 hitbox 大小
		setHitboxOffset(0, 20); // 设置 hitbox 偏移量（向下移动 20 像素）
		if (root != null) {
			visualizeHitbox(root); // 可视化 hitbox
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

		// 更新 hitbox 位置
		updateHitbox();
	}

	@Override
	public void updateActor() {
		updatePosition();
	}

	@Override
	public ActiveActorDestructible fireProjectile() {
		double projectileX = getLayoutX() + PROJECTILE_X_POSITION_OFFSET;
		double projectileY = getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET);

		UserProjectile projectile = new UserProjectile(projectileX, projectileY, root); // 传入 root 以显示 hitbox
		if (root != null) {
			projectile.visualizeHitbox(root); // 可视化子弹的 hitbox
		}
		return projectile;
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
