package Levels;

import Core.ActiveActorDestructible;
import Entities.EnemyPlane;

public class LevelTwo extends LevelParent {

    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background2.png";  // 第三关的背景图
    private static final String NEXT_LEVEL = "Levels.LevelThree";  // 假设下一关是 Boss 关
    private static final int TOTAL_ENEMIES = 8;  // 本关敌人总数
    private static final int KILLS_TO_ADVANCE = 15;  // 玩家需要击杀的敌人数量才能进入下一关
    private static final double ENEMY_SPAWN_PROBABILITY = .30;  // 敌人生成概率稍微增加
    private static final int PLAYER_INITIAL_HEALTH = 5;  // 玩家初始生命

    public LevelTwo(double screenHeight, double screenWidth) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH);
    }

    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            loseGame();  // 玩家死亡，游戏结束
        } else if (userHasReachedKillTarget()) {
            goToNextLevel(NEXT_LEVEL);  // 如果达到击杀目标，跳转到下一关
        }
    }

    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());  // 将玩家单位加入场景
    }

    @Override
    protected void spawnEnemyUnits() {
        int currentNumberOfEnemies = getCurrentNumberOfEnemies();
        for (int i = 0; i < TOTAL_ENEMIES - currentNumberOfEnemies; i++) {
            if (Math.random() < ENEMY_SPAWN_PROBABILITY) {
                double newEnemyInitialYPosition = Math.random() * getEnemyMaximumYPosition();
                // 传递 getRoot() 参数用于敌人生成和可视化
                ActiveActorDestructible newEnemy = new EnemyPlane(getScreenWidth(), newEnemyInitialYPosition, getRoot());
                addEnemyUnit(newEnemy);  // 添加敌人
            }
        }
    }

    @Override
    protected LevelView instantiateLevelView() {
        return new LevelView(getRoot(), PLAYER_INITIAL_HEALTH);  // 实例化关卡视图
    }

    private boolean userHasReachedKillTarget() {
        return getUser().getNumberOfKills() >= KILLS_TO_ADVANCE;  // 玩家是否已达到击杀目标
    }
}
