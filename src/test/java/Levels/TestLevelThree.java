package Levels;

import Entities.Boss;
import Entities.MutationBoss1;
import javafx.collections.FXCollections;
import javafx.scene.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestLevelThree {

    private LevelThree levelThree;
    private Group mockRoot;

    @BeforeEach
    void setUp() {
        // 创建模拟的 Group 对象
        mockRoot = mock(Group.class);
        when(mockRoot.getChildren()).thenReturn(FXCollections.observableArrayList());

        // 初始化 LevelThree 并覆盖 getRoot 方法
        levelThree = new LevelThree(800, 1200) {
            @Override
            protected Group getRoot() {
                return mockRoot; // 返回模拟的 Group
            }
        };
    }


    @Test
    void testInstantiateLevelView() {
        assertNotNull(levelThree.instantiateLevelView(), "LevelView should be instantiated.");
    }

    @Test
    void testSpawnEnemyUnits() {
        Boss mockBoss = mock(Boss.class);
        MutationBoss1 mockMutationBoss = mock(MutationBoss1.class);

        // 模拟 Boss 和 MutationBoss1 的状态
        when(mockBoss.isDestroyed()).thenReturn(false);
        when(mockMutationBoss.isDestroyed()).thenReturn(true);

        levelThree.spawnEnemyUnits();

        assertEquals(1, levelThree.getCurrentNumberOfEnemies(), "One enemy should be added.");
    }

    @Test
    void testCheckIfGameOver_UserDestroyed() {
        // 模拟用户已被销毁
        levelThree.getUser().destroy();
        levelThree.checkIfGameOver();
        assertTrue(levelThree.getUser().isDestroyed(), "Game should end when user is destroyed.");
    }

    @Test
    void testCheckIfGameOver_BossDestroyed() {
        MutationBoss1 mockMutationBoss = mock(MutationBoss1.class);
        when(mockMutationBoss.isDestroyed()).thenReturn(true);

        levelThree.checkIfGameOver();

    }
}
