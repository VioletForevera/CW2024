package Entities;

import Core.ActiveActorDestructible;
import javafx.scene.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestUserPlane {

    private UserPlane userPlane;
    private Group mockGroup;

    @BeforeEach
    void setUp() {
        mockGroup = mock(Group.class); // 使用 Mockito 创建 Group 的模拟对象
        userPlane = new UserPlane(3, mockGroup); // 初始化 UserPlane 实例，初始生命值为 3
    }

    @Test
    void testInitialization() {
        assertNotNull(userPlane, "UserPlane should be initialized.");
        assertEquals(3, userPlane.getHealth(), "Initial health should be 3.");
        assertEquals(0, userPlane.getNumberOfKills(), "Initial number of kills should be 0.");
    }





    @Test
    void testBoundaryConstraints() {
        userPlane.setTranslateY(-1000); // 超出上边界
        userPlane.updatePosition();
        assertTrue(userPlane.getLayoutY() >= -40, "UserPlane should stay within upper boundary.");

        userPlane.setTranslateY(1000); // 超出下边界
        userPlane.updatePosition();
        assertTrue(userPlane.getLayoutY() <= 600, "UserPlane should stay within lower boundary.");

        userPlane.setTranslateX(-1000); // 超出左边界
        userPlane.updatePosition();
        assertTrue(userPlane.getLayoutX() >= 0, "UserPlane should stay within left boundary.");

        userPlane.setTranslateX(2000); // 超出右边界
        userPlane.updatePosition();
        assertTrue(userPlane.getLayoutX() <= 1300, "UserPlane should stay within right boundary.");
    }



    @Test
    void testTakeDamage() {
        userPlane.takeDamage();
        assertEquals(2, userPlane.getHealth(), "Health should decrease by 1 after taking damage.");
        userPlane.takeDamage();
        userPlane.takeDamage();
        assertTrue(userPlane.isDestroyed(), "UserPlane should be destroyed when health reaches 0.");
    }

    @Test
    void testIncrementHealth() {
        userPlane.incrementHealth();
        assertEquals(4, userPlane.getHealth(), "Health should increase by 1 after incrementHealth.");
    }

    @Test
    void testKillCount() {
        assertEquals(0, userPlane.getNumberOfKills(), "Initial kill count should be 0.");
        userPlane.incrementKillCount();
        assertEquals(1, userPlane.getNumberOfKills(), "Kill count should increase by 1 after incrementKillCount.");
    }

    @Test
    void testStopMovement() {
        userPlane.moveUp();
        userPlane.moveRight();
        userPlane.stop();
        userPlane.updatePosition();
        assertFalse(userPlane.isMoving(), "UserPlane should stop moving after stop is called.");
    }
}
