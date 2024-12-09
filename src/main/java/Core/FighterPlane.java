package Core;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import sounds.MusicPlayer;

public abstract class FighterPlane extends ActiveActorDestructible {

	private int health;
	private Rectangle hitbox;
	private double hitboxOffsetX = 0; // 水平偏移量
	private double hitboxOffsetY = 0; // 垂直偏移量

	public FighterPlane(String imageName, int imageHeight, double initialXPos, double initialYPos, int health) {
		super(imageName, imageHeight, initialXPos, initialYPos);
		this.health = health;

		// 初始化 hitbox
		this.hitbox = new Rectangle();
		this.hitbox.setWidth(imageHeight * 0.8); // 默认宽度
		this.hitbox.setHeight(imageHeight * 0.8); // 默认高度
		this.hitbox.setFill(Color.TRANSPARENT); // 默认透明
		this.hitbox.setStroke(Color.RED); // 用于调试的红色边框
	}

	// 子类需要实现发射子弹的方法
	public abstract ActiveActorDestructible fireProjectile();

	@Override
	public void takeDamage() {
		health--;
		System.out.println("FighterPlane took damage. Remaining health: " + health);
		if (healthAtZero()) {
			// 播放爆炸音效，设置音量为 50%
			MusicPlayer.playEffect("/com/example/demo/images/explosion.wav", 0.7f);
			this.destroy();
			System.out.println("FighterPlane destroyed.");
		}
	}



	public void setHitboxSize(double width, double height) {
		if (hitbox != null) {
			this.hitbox.setWidth(width);
			this.hitbox.setHeight(height);

		}
	}

	public void setHitboxOffset(double offsetX, double offsetY) {
		this.hitboxOffsetX = offsetX;
		this.hitboxOffsetY = offsetY;
		System.out.println("Set hitbox offset: offsetX=" + offsetX + ", offsetY=" + offsetY);
	}

	public Rectangle getHitbox() {
		return hitbox;
	}

	public void updateHitbox() {
		if (hitbox != null) {
			hitbox.setLayoutX(getLayoutX() + getTranslateX() + hitboxOffsetX);
			hitbox.setLayoutY(getLayoutY() + getTranslateY() + hitboxOffsetY);
			;
		}
	}

	public void visualizeHitbox(Group root) {
		if (hitbox != null && !root.getChildren().contains(hitbox)) {
			root.getChildren().add(hitbox);
			System.out.println("Hitbox visualized in the scene for: " + this.getClass().getSimpleName());
		}
	}


	protected double getProjectileXPosition(double xPositionOffset) {
		return getLayoutX() + getTranslateX() + xPositionOffset;
	}

	protected double getProjectileYPosition(double yPositionOffset) {
		return getLayoutY() + getTranslateY() + yPositionOffset;
	}

	private boolean healthAtZero() {
		return health <= 0;
	}

	public int getHealth() {
		return health;
	}

	// 更新时调用以同步 hitbox 和飞机的位置
	@Override
	public void updateActor() {
		updatePosition(); // 更新飞机的位置
		updateHitbox();   // 同步更新 hitbox 的位置
		System.out.println("Updated FighterPlane actor.");
	}
	public void setHealth(int health) {
		this.health = health;
		System.out.println("Health set to: " + health);
	}

}
