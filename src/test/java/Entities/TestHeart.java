package Entities;
import Entities.Heart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestHeart {

    private Heart heart;

    @BeforeEach
    void setUp() {
        // 初始化 Heart 对象
        heart = new Heart(100, 200);
    }

    @Test
    void testInitialSetup() {
        // 验证初始状态
        assertFalse(heart.isDestroyed(), "Heart should not be destroyed initially");
        assertEquals(100, heart.getX(), "Initial X position is incorrect");
        assertEquals(200, heart.getY(), "Initial Y position is incorrect");
        assertNotNull(heart.getImage(), "Heart image should be loaded");
    }

    @Test
    void testDestroyMethod() {
        // 调用 destroy 方法
        heart.destroy();

        // 验证状态
        assertTrue(heart.isDestroyed(), "Heart should be destroyed after calling destroy()");
    }

    @Test
    void testUpdateActor() {
        // 调用 updateActor 方法，当前无实际逻辑
        heart.updateActor();

        // 验证没有异常，未来可扩展测试
        assertFalse(heart.isDestroyed(), "UpdateActor should not affect the destroyed state");
    }
}
