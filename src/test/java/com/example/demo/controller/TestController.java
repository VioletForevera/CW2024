package com.example.demo.controller;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Levels.LevelParent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestController {

    private Controller controller;
    private Stage mockStage;

    @BeforeAll
    static void initJavaFX() {
        // Initialize JavaFX runtime
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        mockStage = mock(Stage.class); // Mock Stage object
        controller = new Controller(mockStage);
    }




    @Test
    void testGoToLevelExceptionHandling() {
        // Simulate an exception when loading a level
        Exception exception = assertThrows(Exception.class, () -> {
            controller.goToLevel("NonExistentLevel");
        });

        assertTrue(exception instanceof ClassNotFoundException, "Expected ClassNotFoundException");
    }

    @Test
    void testUpdateMethod() {
        // Simulate an update from an Observable
        String mockLevelName = "Levels.LevelTwo";

        Platform.runLater(() -> {
            try {
                controller.update(null, mockLevelName);

            } catch (Exception e) {
                fail("Exception thrown during update method test: " + e.getMessage());
            }
        });
    }


}
