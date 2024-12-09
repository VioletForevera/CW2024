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

public class Controller implements Observer {

    private static final String LEVEL_ONE_CLASS_NAME = "Levels.LevelThree";
    private final Stage stage; // 关联的舞台
    private LevelParent currentLevel = null; // 当前活动关卡实例

    public Controller(Stage stage) {
        this.stage = stage;
    }

    public void launchGame() {
        Platform.runLater(() -> {
            try {
                System.out.println("Launching game...");
                stage.show(); // 确保舞台可见
                goToLevel(LEVEL_ONE_CLASS_NAME); // 加载第一关
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error launching game", e);
            }
        });
    }

    public void restartGame() {
        Platform.runLater(() -> {
            try {
                System.out.println("Restarting game...");
                if (currentLevel != null) {
                    currentLevel = null; // 清理当前关卡
                }
                goToLevel(LEVEL_ONE_CLASS_NAME); // 重新加载第一关
            } catch (Exception e) {
                showAlert("Error restarting game", e);
            }
        });
    }

    private void goToLevel(String className) throws Exception {
        if (currentLevel != null && currentLevel.getClass().getName().equals(className)) {
            return; // 当前关卡已加载，无需重复加载
        }

        Class<?> myClass = Class.forName(className);
        Constructor<?> constructor = myClass.getConstructor(double.class, double.class);
        currentLevel = (LevelParent) constructor.newInstance(stage.getHeight(), stage.getWidth());
        currentLevel.addObserver(this);

        Scene scene = currentLevel.initializeScene();
        stage.setScene(scene);
        currentLevel.startGame();
    }

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

    private void showAlert(String title, Exception e) {
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
