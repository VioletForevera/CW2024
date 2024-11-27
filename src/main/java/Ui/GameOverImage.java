package Ui;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class GameOverImage extends Pane {

	private static final String IMAGE_NAME = "/com/example/demo/images/gameover.png";
	private static final int HEIGHT = 800;
	private static final int WIDTH = 1400;

	private final ImageView gameOverImage;
	private final Button exitButton;
	private final Button returnButton;

	public GameOverImage(double xPosition, double yPosition) {
		// Setup game over image
		gameOverImage = new ImageView(new Image(getClass().getResource(IMAGE_NAME).toExternalForm()));
		gameOverImage.setFitHeight(HEIGHT);
		gameOverImage.setFitWidth(WIDTH);
		gameOverImage.setLayoutX(0);
		gameOverImage.setLayoutY(100);

		// Setup Exit button
		exitButton = new Button("Exit Game");
		styleButton(exitButton);
		exitButton.setLayoutX(WIDTH / 2 - 150); // Left side
		exitButton.setLayoutY(HEIGHT - 80);
		exitButton.setOnAction(event -> System.exit(0)); // Exit the application

		// Setup Return button
		returnButton = new Button("Return to Menu");
		styleButton(returnButton);
		returnButton.setLayoutX(WIDTH / 2 + 30); // Right side
		returnButton.setLayoutY(HEIGHT - 80);
		returnButton.setOnAction(event -> returnToMenu()); // Return to main menu logic

		// Add components to the pane
		this.getChildren().addAll(gameOverImage, exitButton, returnButton);
		this.setLayoutX(xPosition);
		this.setLayoutY(yPosition);
	}

	// Return to menu logic (placeholder for implementation)
	private void returnToMenu() {
		System.out.println("Returning to main menu...");
		// 隐藏当前窗口（假设 GameOverImage 在主窗口的 Scene 中）
		this.getScene().getWindow().hide();

		// 创建并显示主菜单
		javax.swing.SwingUtilities.invokeLater(() -> {
			MainMenu mainMenu = new MainMenu();
			mainMenu.setVisible(true);
		});
	}

	// Apply styles to buttons
	private void styleButton(Button button) {
		button.setStyle(
				"-fx-font-size: 16px; " +
						"-fx-background-color: #4CAF50; " + // Green background
						"-fx-text-fill: white; " + // White text
						"-fx-background-radius: 10; " + // Rounded corners
						"-fx-padding: 10 20 10 20;" // Padding for a larger button
		);

		button.setOnMouseEntered(event ->
				button.setStyle(
						"-fx-font-size: 16px; " +
								"-fx-background-color: #45a049; " + // Darker green on hover
								"-fx-text-fill: white; " +
								"-fx-background-radius: 10; " +
								"-fx-padding: 10 20 10 20;"
				)
		);

		button.setOnMouseExited(event ->
				button.setStyle(
						"-fx-font-size: 16px; " +
								"-fx-background-color: #4CAF50; " +
								"-fx-text-fill: white; " +
								"-fx-background-radius: 10; " +
								"-fx-padding: 10 20 10 20;"
				)
		);
	}
}
