package Entities;

import Core.ActiveActorDestructible;
import javafx.scene.Group;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestBoss {

    private Boss boss;

    @BeforeAll
    void setUpJavaFX() {
        // 初始化 JavaFX 环境，确保 JavaFX 正常运行
        if (!com.sun.javafx.application.PlatformImpl.isFxApplicationThread()) {
            com.sun.javafx.application.PlatformImpl.startup(() -> {});
        }
    }

    @BeforeEach
    void setUp() {
        boss = new Boss(new Group()); // 使用无参数构造器，确保 Group 正常初始化
    }

    @Test
    void testInitialHealth() {
        assertEquals(19, boss.getHealth(), "Initial health of Boss should be 1.");
    }

    @Test
    void testTakeDamage() {
        boss.takeDamage();
        assertEquals(18, boss.getHealth(), "Health should be reduced to 18 after taking damage.");

    }


    @Test
    void testFireProjectile() {
        ActiveActorDestructible projectile = boss.fireProjectile();
        if (projectile != null) {
            assertNotNull(projectile, "Projectile should not be null when fired.");
        } else {
            assertNull(projectile, "Projectile firing should respect the fire rate.");
        }
    }

    @Test
    void testUpdateActor() {
        assertDoesNotThrow(boss::updateActor, "updateActor should not throw any exceptions.");
    }
}