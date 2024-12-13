package Entities;
import Entities.EnemyPlane;
import Core.ActiveActorDestructible;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestEnemyPlane {

    private EnemyPlane enemyPlane;

    @BeforeEach
    void setUp() {
        // 初始化敌机
        enemyPlane = new EnemyPlane(500, 300, null);
    }

    @Test
    void testInitialSetup() {
        // 验证初始位置和生命值
        assertEquals(500, enemyPlane.getLayoutX(), "Initial X position is incorrect");
        assertEquals(300, enemyPlane.getLayoutY(), "Initial Y position is incorrect");
        assertEquals(1, enemyPlane.getHealth(), "Initial health is incorrect");

        // 验证 Hitbox 是否初始化
        assertNotNull(enemyPlane.getHitbox(), "Hitbox should not be null");
    }

    @Test
    void testUpdatePosition() {
        // 更新位置
        enemyPlane.updatePosition();

        // 验证位置更新
        assertEquals(500, enemyPlane.getLayoutX(), "X position did not update correctly");
    }

    @Test
    void testFireProjectile() {
        // 模拟发射子弹
        ActiveActorDestructible projectile = enemyPlane.fireProjectile();

        // 如果发射子弹，则验证位置
        if (projectile != null) {
            assertEquals(enemyPlane.getLayoutX() - 100, projectile.getLayoutX(), "Projectile X position is incorrect");
            assertEquals(enemyPlane.getLayoutY() + 50, projectile.getLayoutY(), "Projectile Y position is incorrect");
        }
    }

    @Test
    void testUpdateHitbox() {
        // 更新 Hitbox
        enemyPlane.updateHitbox();

        // 验证 Hitbox 位置
        assertNotNull(enemyPlane.getHitbox(), "Hitbox should not be null");
        assertEquals(enemyPlane.getLayoutX() + 40, enemyPlane.getHitbox().getLayoutX(), "Hitbox X position is incorrect");
        assertEquals(enemyPlane.getLayoutY() + 60, enemyPlane.getHitbox().getLayoutY(), "Hitbox Y position is incorrect");
    }

    @Test
    void testUpdateActor() {
        // 更新 Actor
        enemyPlane.updateActor();

        // 验证位置更新
        assertEquals(500, enemyPlane.getLayoutX(), "X position did not update correctly");
    }
}
