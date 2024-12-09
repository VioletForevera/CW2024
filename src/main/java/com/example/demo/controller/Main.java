package com.example.demo.controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Main extends Application {

	private static Main instance; // 单例实例
	private Stage primaryStage;
	private static boolean isJavaFxInitialized = false; // 标志是否已初始化 JavaFX
	private static Controller controller; // 保存 Controller 实例

	@Override
	public void start(Stage stage) {
		isJavaFxInitialized = true; // 标志 JavaFX 已初始化
		instance = this; // 初始化单例实例
		primaryStage = stage;

		stage.setTitle("Sky Battle");
		stage.setWidth(1300);
		stage.setHeight(750);
		stage.setResizable(false);

		controller = new Controller(stage);
		controller.launchGame(); // 启动游戏
	}

	public static synchronized Main getInstance() {
		if (instance == null) {
			throw new IllegalStateException("Main instance has not been initialized yet.");
		}
		return instance;
	}

	public static void startOrRestartGame() {
		if (!isJavaFxInitialized) {
			System.out.println("Launching JavaFX application...");
			Application.launch(Main.class); // 启动 JavaFX 应用
		} else {
			System.out.println("Restarting game...");
			restartGame();
			//Platform.runLater(() -> getInstance().restartGame()); // 确保重启在 JavaFX 线程中运行
		}
	}

	public static void restartGame() {
		if (controller != null) {
			controller.restartGame(); // 调用 Controller 的 restartGame 方法
		} else {
			System.out.println("Controller is null. Cannot restart the game.");
		}
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
