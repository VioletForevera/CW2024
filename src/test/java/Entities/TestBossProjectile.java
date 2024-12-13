package Entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestBossProjectile {

    private BossProjectile bossProjectile;

    @BeforeEach
    void setUp() {
        // 初始化 BossProjectile 对象，初始位置为 (100, 200)，速度为 (5, -3)
        bossProjectile = new BossProjectile(100, 200, 5, -3);
    }

    @Test
    void testInitialPosition() {
        // 检查初始位置
        assertEquals(100, bossProjectile.getLayoutX(), "Initial X position should be 100.");
        assertEquals(200, bossProjectile.getLayoutY(), "Initial Y position should be 200.");
    }

    @Test
    void testUpdatePosition() {
        // 更新位置
        bossProjectile.updatePosition();

        // 检查更新后的位置
        assertEquals(105, bossProjectile.getLayoutX()+ bossProjectile.getTranslateX(), "X position should be updated to 105.");
        assertEquals(197, bossProjectile.getLayoutY()+ bossProjectile.getTranslateY(), "Y position should be updated to 197.");
    }

    @Test
    void testSetVelocity() {
        // 设置新的速度
        bossProjectile.setVelocity(10, 15);

        // 检查更新速度后的行为
        bossProjectile.updatePosition();
        assertEquals(110, bossProjectile.getLayoutX()+ bossProjectile.getTranslateX(), "X position should be updated to 110 with new velocity.");
        assertEquals(215, bossProjectile.getLayoutY()+ bossProjectile.getTranslateY(), "Y position should be updated to 215 with new velocity.");
    }

    @Test
    void testUpdateActor() {
        // 调用 updateActor，检查是否更新位置
        bossProjectile.updateActor();
        assertEquals(105, bossProjectile.getLayoutX() + bossProjectile.getTranslateX(), "X position should be updated by updateActor.");
        assertEquals(197, bossProjectile.getLayoutY() + bossProjectile.getTranslateY(), "Y position should be updated by updateActor.");
    }
}
