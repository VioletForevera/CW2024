package Levels;

import Entities.Boss;
import Entities.MutationBoss1;

public class LevelThree extends LevelParent {

	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background3.png";
	private static final int PLAYER_INITIAL_HEALTH = 5;
	private final Boss boss;
	private final MutationBoss1 mutationBoss1;
	private LevelViewLevelTwo levelView;
	private boolean secondPhaseActive = false;

	public LevelThree(double screenHeight, double screenWidth) {
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH);
		boss = new Boss();
		mutationBoss1 = new MutationBoss1();
	}

	@Override
	protected void initializeFriendlyUnits() {
		getRoot().getChildren().add(getUser());
	}

	@Override
	protected void checkIfGameOver() {
		if (userIsDestroyed()) {
			loseGame();
		}
		else if (mutationBoss1.isDestroyed()) {
			winGame();
		}
	}


	private void spawnSecondPhaseBoss() {
		System.out.println("Spawning second phase boss...");
		MutationBoss1 secondPhaseBoss = new MutationBoss1(getRoot());
		addEnemyUnit(secondPhaseBoss);
		secondPhaseActive = true; // 标记第二形态已激活
		System.out.println("Second phase boss added to the scene.");
	}

	@Override
	protected void spawnEnemyUnits() {
		if (getCurrentNumberOfEnemies() == 0) {
			if (!boss.isDestroyed()) {
				addEnemyUnit(boss);
			} else if (!mutationBoss1.isDestroyed()) {
				addEnemyUnit(mutationBoss1);
			}
		}
	}

	@Override
	protected LevelView instantiateLevelView() {
		levelView = new LevelViewLevelTwo(getRoot(), PLAYER_INITIAL_HEALTH);
		return levelView;
	}

}