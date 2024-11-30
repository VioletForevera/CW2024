package Entities;

import Core.Projectile;
import javafx.scene.Group;

public class UserProjectile extends Projectile {

	private static final String IMAGE_NAME = "userfire.png";
	private static final int IMAGE_HEIGHT = 125;
	private static final int HORIZONTAL_VELOCITY = 15;

	private double horizontalOffset = 50.0; // 水平偏移量
	private double verticalOffset = 50.0;  // 垂直偏移量

	public UserProjectile(double initialXPos, double initialYPos, Group root) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);

		// 初始化 hitbox 的大小
		setHitboxSize(IMAGE_HEIGHT * 0.3, IMAGE_HEIGHT * 0.2);

		// 将 hitbox 可视化（调试用）
		visualizeHitbox(root);
	}

	@Override
	public void updatePosition() {
		// 子弹向前移动
		moveHorizontally(HORIZONTAL_VELOCITY);

		// 确保 hitbox 同步
		updateHitbox();
	}

	@Override
	public void updateActor() {
		updatePosition(); // 更新位置
	}

	@Override
	protected void updateHitbox() {
		if (getHitbox() != null) {
			// 在原始位置的基础上增加偏移量
			getHitbox().setLayoutX(getLayoutX() + getTranslateX() + horizontalOffset);
			getHitbox().setLayoutY(getLayoutY() + getTranslateY() + verticalOffset);

			;
		}
	}

	// 可视化 hitbox（仅用于调试）
	public void visualizeHitbox(Group root) {
		if (getHitbox() != null && !root.getChildren().contains(getHitbox())) {
			root.getChildren().add(getHitbox());
		}
	}

	// 设置水平和垂直偏移量的方法
	public void setHitboxOffsets(double horizontalOffset, double verticalOffset) {
		this.horizontalOffset = horizontalOffset;
		this.verticalOffset = verticalOffset;
		System.out.println("Set hitbox offsets: horizontalOffset=" + horizontalOffset
				+ ", verticalOffset=" + verticalOffset);
	}
}
