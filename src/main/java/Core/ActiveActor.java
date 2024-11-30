package Core;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class ActiveActor extends ImageView {

	private static final String IMAGE_LOCATION = "/com/example/demo/images/";
	private Rectangle hitbox; // 用于碰撞检测的 hitbox

	public ActiveActor(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		this.setImage(new javafx.scene.image.Image(getClass().getResource(IMAGE_LOCATION + imageName).toExternalForm()));
		this.setLayoutX(initialXPos);
		this.setLayoutY(initialYPos);
		this.setFitHeight(imageHeight);
		this.setPreserveRatio(true);

		// 初始化 hitbox
		hitbox = new Rectangle();
		hitbox.setFill(Color.TRANSPARENT); // 确保 hitbox 透明
		hitbox.setStroke(Color.RED); // 边框颜色设置为红色用于可视化调试
		hitbox.setStrokeWidth(1);
		updateHitbox(); // 初始化时同步 hitbox 位置
	}

	public abstract void updatePosition();

	// 更新 hitbox 的位置
	protected void updateHitbox() {
		hitbox.setX(getLayoutX() + getTranslateX());
		hitbox.setY(getLayoutY() + getTranslateY());
	}

	// 公共方法用于更新 hitbox，供外部类调用
	public void updateActorHitbox() {
		updateHitbox(); // 调用 protected 方法
	}

	// 设置 hitbox 的大小
	public void setHitboxSize(double width, double height) {
		if (hitbox != null) {
			this.hitbox.setWidth(width);
			this.hitbox.setHeight(height);
			updateHitbox(); // 确保每次调整尺寸后立即同步位置
		}
	}

	// 获取当前的 hitbox
	public Rectangle getHitbox() {
		return hitbox;
	}

	// 可视化 hitbox（仅用于调试）
	public void visualizeHitbox(javafx.scene.Group root) {
		if (!root.getChildren().contains(hitbox)) {
			root.getChildren().add(hitbox);
		}
	}

	// 移动函数
	protected void moveHorizontally(double horizontalMove) {
		this.setTranslateX(getTranslateX() + horizontalMove);
		updateHitbox();
	}

	protected void moveVertically(double verticalMove) {
		this.setTranslateY(getTranslateY() + verticalMove);
		updateHitbox();
	}
}
