package Levels;

import Core.ActiveActorDestructible;
import Entities.EnemyPlane;
import Entities.UserPlane;

import javafx.scene.Group;
import javafx.scene.Scene;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestLevelParent {

    private LevelParent levelParent;
    private Group mockRoot;
    private UserPlane mockUser;

    @BeforeEach
    void setUp() {
        // 创建 LevelParent 的匿名子类进行测试
        levelParent = new LevelParent("/com/example/demo/images/background1.png", 800, 1200, 5) {
            @Override
            protected void initializeFriendlyUnits() {
                // 模拟友方单位初始化
                getRoot().getChildren().add(getUser());
            }

            @Override
            protected void checkIfGameOver() {
                // 模拟游戏结束检查
                if (userIsDestroyed()) {
                    loseGame();
                }
            }

            @Override
            protected void spawnEnemyUnits() {
                // 模拟敌人生成
                ActiveActorDestructible enemy = new EnemyPlane(1200, 100, getRoot());
                addEnemyUnit(enemy);
            }

            @Override
            protected LevelView instantiateLevelView() {
                // 返回 Mock 的 LevelView
                return mock(LevelView.class);
            }
        };

        mockRoot = mock(Group.class);
        mockUser = mock(UserPlane.class);

        // 替换内部组件
        levelParent.root.getChildren().clear();
    }

    @Test
    void testInitializeScene() {
        Scene scene = levelParent.initializeScene();
        assertNotNull(scene, "Scene should not be null after initialization.");
        assertTrue(levelParent.getRoot().getChildren().contains(levelParent.getUser()), "UserPlane should be added to the root.");
    }

    @Test
    void testTogglePause() {
        // 模拟游戏开始
        levelParent.startGame();

        // 验证暂停功能
        levelParent.togglePause();
        assertTrue(levelParent.isPaused, "Game should be paused.");
        assertTrue(levelParent.pauseMenu.isVisible(), "Pause menu should be visible when paused.");

        // 验证恢复功能
        levelParent.togglePause();
        assertFalse(levelParent.isPaused, "Game should be resumed.");
        assertFalse(levelParent.pauseMenu.isVisible(), "Pause menu should not be visible when resumed.");
    }

    @Test
    void testSpawnEnemyUnits() {
        levelParent.spawnEnemyUnits();
        assertFalse(levelParent.enemyUnits.isEmpty(), "Enemy units should be spawned.");
    }

    @Test
    void testGoToNextLevel() {
        levelParent.goToNextLevel("Levels.LevelTwo");
        assertTrue(levelParent.isLevelSwitching, "Level switching flag should be true during transition.");
    }










}
