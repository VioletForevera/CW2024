package Levels;

import Core.ActiveActorDestructible;
import Entities.EnemyPlane;
import javafx.scene.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sounds.MusicPlayer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestLevelOne {

    private LevelOne levelOne;
    private double screenHeight = 800;
    private double screenWidth = 1200;
    private Group mockRoot;

    @BeforeEach
    void setUp() {
        mockRoot = mock(Group.class); // 模拟 Group 对象
        levelOne = Mockito.spy(new LevelOne(screenHeight, screenWidth)); // 使用 spy 监视 LevelOne 实例
        doReturn(mockRoot).when(levelOne).getRoot(); // 模拟 getRoot 方法返回值
    }



    @Test
    void testCheckIfGameOver_UserDestroyed() {
        doReturn(true).when(levelOne).userIsDestroyed(); // 模拟用户被摧毁的情况

        levelOne.checkIfGameOver();

        verify(levelOne).loseGame(); // 验证是否调用了 loseGame 方法
    }

    @Test
    void testCheckIfGameOver_UserReachedKillTarget() {
        doReturn(false).when(levelOne).userIsDestroyed(); // 确保用户未被摧毁
        doReturn(true).when(levelOne).userHasReachedKillTarget(); // 模拟达到击杀目标

        levelOne.checkIfGameOver();

        verify(levelOne).goToNextLevel("Levels.LevelTwo"); // 验证是否调用了 goToNextLevel 方法
    }

    @Test
    void testSpawnEnemyUnits() {
        doReturn(0).when(levelOne).getCurrentNumberOfEnemies(); // 模拟当前敌人数量为 0
        doReturn(600.0).when(levelOne).getEnemyMaximumYPosition(); // 模拟敌人最大 Y 位置

        levelOne.spawnEnemyUnits();

        verify(levelOne, atMost(5)).addEnemyUnit(any(EnemyPlane.class)); // 验证最多生成 5 个敌人
    }

}
