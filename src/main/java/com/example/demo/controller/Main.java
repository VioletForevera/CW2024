package com.example.demo.controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * The Main class serves as the entry point for the Sky Battle game application.
 * It initializes the JavaFX application and manages the primary game stage.
 */
public class Main extends Application {

	/**
	 * Singleton instance of the Main class, ensuring a single instance is used throughout the application.
	 */
	private static Main instance;

	/**
	 * Flag to indicate whether JavaFX is initialized.
	 * This prevents re-initialization of JavaFX components.
	 */
	private static boolean isJavaFxInitialized = false;

	/**
	 * Instance of the game Controller, responsible for managing the game logic and interactions.
	 */
	private static Controller controller;


	/**
	 * Starts the JavaFX application by initializing the game stage and launching the Controller.
	 *
	 * @param stage the primary stage for this application
	 */
	@Override
	public void start(Stage stage) {
		isJavaFxInitialized = true; // Mark JavaFX as initialized
		instance = this; // Initialize the singleton instance

		stage.setTitle("Sky Battle"); // Set the title of the game window
		stage.setWidth(1300); // Set the width of the window
		stage.setHeight(750); // Set the height of the window
		stage.setResizable(false); // Disable window resizing

		controller = new Controller(stage); // Create a new Controller instance
		controller.launchGame(); // Launch the game
	}

	/**
	 * Returns the singleton instance of the Main class.
	 *
	 * @return the singleton instance of Main
	 * @throws IllegalStateException if the instance has not been initialized
	 */
	public static synchronized Main getInstance() {
		if (instance == null) {
			throw new IllegalStateException("Main instance has not been initialized yet.");
		}
		return instance;
	}

	/**
	 * Launches the JavaFX application if it has not already been initialized.
	 * This method ensures the application is started in the correct context.
	 */
	public static void startGame() {
		if (!isJavaFxInitialized) {
			System.out.println("Launching JavaFX application...");
			Application.launch(Main.class); // Launch the JavaFX application
		}
	}

	/**
	 * The main method serves as the entry point of the application.
	 * It calls the {@code launch} method to start the JavaFX application.
	 *
	 * @param args the command-line arguments passed to the application
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
