package Ui;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class WinImage extends Pane {

	private static final String IMAGE_NAME = "/com/example/demo/images/youwin.png";
	private static final int HEIGHT = 500;
	private static final int WIDTH = 600;

	private final ImageView winImage;
	private final Button exitButton;
	private final Button returnButton;

	public WinImage(double xPosition, double yPosition) {
		// Setup win image
		winImage = new ImageView(new Image(getClass().getResource(IMAGE_NAME).toExternalForm()));
		winImage.setFitHeight(HEIGHT);
		winImage.setFitWidth(WIDTH);
		winImage.setLayoutX(0);
		winImage.setLayoutY(0);

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
		this.getChildren().addAll(winImage, exitButton, returnButton);
		this.setLayoutX(xPosition);
		this.setLayoutY(yPosition);
		this.setVisible(false); // Initially hidden
	}

	// Display the win screen
	public void showWinImage() {
		this.setVisible(true);
	}

	// Return to menu logic (placeholder for implementation)
	private void returnToMenu() {
		System.out.println("Returning to main menu...");
		// 隐藏当前窗口（假设 WinImage 在主窗口的 Scene 中）
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
