package Entities;

import Core.ActiveActorDestructible;
import javafx.scene.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestMutationBoss1 {

    private MutationBoss1 boss;
    private Group mockGroup;
    private List<ActiveActorDestructible> mockProjectiles;

    @BeforeEach
    void setUp() {
        mockGroup = mock(Group.class); // 使用 Mockito 创建 Group 的模拟对象
        mockProjectiles = new ArrayList<>();
        boss = new MutationBoss1(); // 初始化 MutationBoss1 实例
    }

    @Test
    void testInitialization() {
        assertNotNull(boss, "Boss should be initialized.");
        assertEquals(10, boss.getHealth(), "Initial health should be 10.");
    }



    @Test
    void testTakeDamage() {
        boss.takeDamage(); // 触发伤害
        assertEquals(9, boss.getHealth(), "Health should decrease by 1 after taking damage.");
        boss.setHealth(1);
        boss.takeDamage(); // 触发致命伤害
        assertTrue(boss.isDestroyed(), "Boss should be destroyed when health reaches 0.");
    }


}
