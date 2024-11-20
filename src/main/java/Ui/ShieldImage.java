package Ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ShieldImage extends ImageView {

	private static final String IMAGE_NAME = "/com/example/demo/images/shield.png";
	private static final int SHIELD_SIZE = 200;

	public ShieldImage(double xPosition, double yPosition) {
		this.setLayoutX(xPosition);
		this.setLayoutY(yPosition);

		// 调试代码：检查图片路径加载是否成功
		java.net.URL shieldImageUrl = getClass().getResource(IMAGE_NAME);
		System.out.println("Shield Image URL: " + shieldImageUrl); // 打印URL

		if (shieldImageUrl != null) {
			this.setImage(new Image(shieldImageUrl.toExternalForm()));
		} else {
			System.out.println("Shield image not found at the specified path.");
		}

		this.setVisible(false);
		this.setFitHeight(SHIELD_SIZE);
		this.setFitWidth(SHIELD_SIZE);
	}

	public void showShield() {
		this.setVisible(true);
	}

	public void hideShield() {
		this.setVisible(false);
	}
}
