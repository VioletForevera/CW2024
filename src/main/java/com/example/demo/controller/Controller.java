package com.example.demo.controller;

import java.lang.reflect.Constructor;
import java.util.Observable;
import java.util.Observer;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import Levels.LevelParent;

/**
 * The Controller class manages the flow of the game, including level transitions and error handling.
 * It acts as an observer to the current game level and responds to level transition events.
 */
public class Controller implements Observer {

    /**
     * The fully qualified class name for the first level of the game.
     * This is used to dynamically load and initialize Level One during gameplay.
     */
    private static final String LEVEL_ONE_CLASS_NAME = "Levels.LevelOne";

    /**
     * The primary {@link Stage} used for displaying the game's graphical user interface.
     * This is the main window where the game content is rendered.
     */
    private final Stage stage;// Associated stage for the game window
    /**
     * The current active level instance in the game.
     * This field holds a reference to the level that is currently being played.
     */
    private LevelParent currentLevel = null;// The current active level instance

    /**
     * Constructs a Controller instance associated with a specific stage.
     *
     * @param stage the JavaFX stage to be managed by the controller
     */
    public Controller(Stage stage) {
        this.stage = stage;
    }

    /**
     * Launches the game by displaying the stage and loading the first level.
     * This method ensures the game starts in the JavaFX application thread.
     */
    public void launchGame() {
        Platform.runLater(() -> {
            try {
                System.out.println("Launching game...");
                stage.show(); // Ensure the stage is visible
                goToLevel(LEVEL_ONE_CLASS_NAME); // Load the first level
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error launching game", e);
            }
        });
    }

    /**
     * Switches to the specified level by its class name.
     * If the level is already loaded, no action is taken.
     *
     * @param className the fully qualified class name of the level to switch to
     * @throws Exception if the level cannot be loaded or instantiated
     */
    public void goToLevel(String className) throws Exception {
        if (currentLevel != null && currentLevel.getClass().getName().equals(className)) {
            return; // Level is already loaded, no need to reload
        }

        Class<?> myClass = Class.forName(className);
        Constructor<?> constructor = myClass.getConstructor(double.class, double.class);
        currentLevel = (LevelParent) constructor.newInstance(stage.getHeight(), stage.getWidth());
        currentLevel.addObserver(this);

        Scene scene = currentLevel.initializeScene();
        stage.setScene(scene);
        currentLevel.startGame();
    }

    /**
     * Responds to updates from the observed level, such as level transitions.
     * Executes the level transition logic on the JavaFX application thread.
     *
     * @param o   the observable object (the current level)
     * @param arg the argument passed by the observable (typically the next level's class name)
     */
    @Override
    public void update(Observable o, Object arg) {
        Platform.runLater(() -> {
            try {
                goToLevel((String) arg);
            } catch (Exception e) {
                showAlert("Error switching level", e);
            }
        });
    }

    /**
     * Displays an alert dialog with error details to the user.
     *
     * @param title the title of the alert dialog
     * @param e     the exception to display details for
     */
    public void showAlert(String title, Exception e) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText("An error occurred");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        });
        e.printStackTrace();
    }
}
