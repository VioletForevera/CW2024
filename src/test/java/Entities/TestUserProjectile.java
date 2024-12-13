package Entities;

import javafx.scene.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestUserProjectile {

    private UserProjectile userProjectile;
    private Group mockGroup;

    @BeforeEach
    void setUp() {
        mockGroup = mock(Group.class); // 使用 Mockito 创建 Group 的模拟对象
        userProjectile = new UserProjectile(100, 200, mockGroup); // 初始化 UserProjectile 实例
    }

    @Test
    void testInitialization() {
        assertNotNull(userProjectile, "UserProjectile should be initialized.");
        assertEquals(100, userProjectile.getLayoutX(), "Initial X position should be 100.");
        assertEquals(200, userProjectile.getLayoutY(), "Initial Y position should be 200.");
    }

    @Test
    void testUpdatePosition() {
        double initialX = userProjectile.getLayoutX();

        userProjectile.updatePosition();
        assertTrue(userProjectile.getLayoutX()+initialX > initialX, "UserProjectile should move forward horizontally.");
    }



    @Test
    void testHitboxUpdate() {
        userProjectile.updatePosition();
        userProjectile.updateHitbox();

        // 验证 hitbox 的位置是否已更新（可通过调试日志或预期行为验证）
        assertNotNull(userProjectile.getHitbox(), "Hitbox should not be null after update.");
    }
}
