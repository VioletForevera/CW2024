package Levels;

import java.util.*;
import java.util.stream.Collectors;

import Core.ActiveActor;
import Core.ActiveActorDestructible;
import Core.FighterPlane;
import Entities.Boss;
import Entities.EnemyProjectile;
import Entities.UserPlane;
import Entities.UserProjectile;
import Entities.Heart;
import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.Node;
import javafx.util.Duration;

public abstract class LevelParent extends Observable {
	private boolean isLevelSwitching= false;

	private static final double SCREEN_HEIGHT_ADJUSTMENT = 150;
	private static final int MILLISECOND_DELAY = 40;
	private final double screenHeight;
	private final double screenWidth;
	private final double enemyMaximumYPosition;

	private final Group root;
	private final Timeline timeline;
	private final UserPlane user;
	private final Scene scene;
	private final ImageView background;

	private final List<ActiveActorDestructible> friendlyUnits;
	private final List<ActiveActorDestructible> enemyUnits;
	private final List<ActiveActorDestructible> userProjectiles;
	private final List<ActiveActorDestructible> enemyProjectiles;
	private final List<Heart> hearts; // 存储心形道具

	private int currentNumberOfEnemies;
	private LevelView levelView;

	private boolean isSceneInitialized = false;
	private boolean isBackgroundInitialized = false;

	public LevelParent(String backgroundImageName, double screenHeight, double screenWidth, int playerInitialHealth) {
		this.root = new Group();
		this.scene = new Scene(root, screenWidth, screenHeight);
		this.timeline = new Timeline();
		this.user = new UserPlane(playerInitialHealth, root);
		this.friendlyUnits = new ArrayList<>();
		this.enemyUnits = new ArrayList<>();
		this.userProjectiles = new ArrayList<>();
		this.enemyProjectiles = new ArrayList<>();
		this.hearts = new ArrayList<>(); // 初始化心形道具列表

		this.background = new ImageView(new Image(getClass().getResource(backgroundImageName).toExternalForm()));
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.enemyMaximumYPosition = screenHeight - SCREEN_HEIGHT_ADJUSTMENT;
		this.levelView = instantiateLevelView();
		this.currentNumberOfEnemies = 0;
		initializeTimeline();
		friendlyUnits.add(user);
	}

	protected abstract void initializeFriendlyUnits();

	protected abstract void checkIfGameOver();

	protected abstract void spawnEnemyUnits();

	protected abstract LevelView instantiateLevelView();

	public Scene initializeScene() {
		if (!isSceneInitialized) {
			System.out.println("Initializing scene...");
			initializeBackground();
			initializeFriendlyUnits();
			levelView.showHeartDisplay();
			isSceneInitialized = true;
		} else {
			System.out.println("Scene is already initialized.");
		}
		return scene;
	}

	public void startGame() {
		background.requestFocus();
		if (timeline.getStatus() != Animation.Status.RUNNING) {
			timeline.play();
		}
	}

	public void goToNextLevel(String levelName) {
		if (isLevelSwitching) {
			System.out.println("Already switching levels. Ignoring call.");
			return; // 防止重复调用
		}

		System.out.println("Switching to next level: " + levelName);
		isLevelSwitching = true; // 设置标志位，防止重复调用

		// 停止当前的游戏循环
		if (timeline != null && timeline.getStatus() == Animation.Status.RUNNING) {
			timeline.stop();
		}

		// 清理当前关卡资源
		cleanUpLevel();

		// 延迟执行关卡切换，确保资源完全清理完成
		Timeline switchTimeline = new Timeline(new KeyFrame(Duration.millis(100), e -> {
			try {
				setChanged();
				notifyObservers(levelName); // 通知观察者切换关卡
			} finally {
				isLevelSwitching = false; // 切换完成后重置标志位
			}
		}));
		switchTimeline.setCycleCount(1);
		switchTimeline.play();
	}
	// 清理关卡的辅助方法
	protected void cleanUpLevel() {
		System.out.println("Cleaning up current level...");

		// 停止动画和清理场景资源
		root.getChildren().clear();

		// 清空所有单位列表
		friendlyUnits.clear();
		enemyUnits.clear();
		userProjectiles.clear();
		enemyProjectiles.clear();
		hearts.clear();

		System.out.println("Level cleaned up successfully.");
	}


	private void updateScene() {
		removeAllDestroyedActors();
		spawnEnemyUnits();
		spawnHearts(); // 调用生成心形道具的方法
		updateActors();
		handleHeartCollisions();
		generateEnemyFire();
		updateNumberOfEnemies();
		handleEnemyPenetration();
		handleUserProjectileCollisions();
		handleEnemyProjectileCollisions();
		handlePlaneCollisions();
		updateKillCount();
		updateLevelView();
		checkIfGameOver();
	}

	private void initializeTimeline() {
		timeline.setCycleCount(Timeline.INDEFINITE);
		KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> updateScene());
		timeline.getKeyFrames().add(gameLoop);
	}

	private void initializeBackground() {
		if (!isBackgroundInitialized) {
			System.out.println("Initializing background...");
			background.setFocusTraversable(true);
			background.setFitHeight(screenHeight);
			background.setFitWidth(screenWidth);

			background.setOnKeyPressed(e -> {
				KeyCode kc = e.getCode();
				if (kc == KeyCode.UP) user.moveUp();
				if (kc == KeyCode.DOWN) user.moveDown();
				if (kc == KeyCode.LEFT) user.moveLeft();
				if (kc == KeyCode.RIGHT) user.moveRight();
				if (kc == KeyCode.SPACE) fireProjectile();
			});

			background.setOnKeyReleased(e -> {
				KeyCode kc = e.getCode();
				if (kc == KeyCode.UP || kc == KeyCode.DOWN) user.stopVertical();
				if (kc == KeyCode.LEFT || kc == KeyCode.RIGHT) user.stopHorizontal();
			});

			root.getChildren().add(background);
			isBackgroundInitialized = true;
		} else {
			System.out.println("Background already initialized.");
		}
	}

	private void fireProjectile() {
		ActiveActorDestructible projectile = user.fireProjectile();
		if (projectile != null) {
			root.getChildren().add(projectile);
			if (projectile instanceof UserProjectile) {
				((UserProjectile) projectile).visualizeHitbox(root);
			}
			userProjectiles.add(projectile);
		}
	}

	private void spawnHearts() {
		double spawnProbability = 0.01; // 心形生成的概率
		if (Math.random() < spawnProbability) {
			double xPos = screenWidth; // 心形从屏幕右边生成
			double yPos = Math.random() * (screenHeight - SCREEN_HEIGHT_ADJUSTMENT); // 随机生成 y 坐标

			Heart heart = new Heart(xPos, yPos); // 创建心形对象
			hearts.add(heart); // 添加到心形列表
			root.getChildren().add(heart); // 将心形添加到场景

			// 创建从右向左移动的水平动画
			TranslateTransition horizontalTransition = new TranslateTransition(Duration.seconds(5), heart);
			horizontalTransition.setFromX(0); // 初始位置
			horizontalTransition.setToX(-screenWidth); // 最终位置（移动到屏幕外）

			// 创建上下抖动的垂直动画
			Timeline verticalShake = new Timeline(new KeyFrame(Duration.millis(200), e -> {
				heart.setTranslateY(heart.getTranslateY() + 5); // 向下移动
			}), new KeyFrame(Duration.millis(400), e -> {
				heart.setTranslateY(heart.getTranslateY() - 5); // 向上移动
			}));
			verticalShake.setCycleCount(Animation.INDEFINITE); // 无限循环

			// 当水平动画结束时停止抖动并清理
			horizontalTransition.setOnFinished(e -> {
				verticalShake.stop(); // 停止抖动动画
				hearts.remove(heart); // 从列表中移除
				root.getChildren().remove(heart); // 从场景中移除
			});

			// 开始动画
			horizontalTransition.play();
			verticalShake.play();
			System.out.println("Heart spawned at: " + xPos + ", " + yPos); // 调试输出
		}
	}

	private void handleHeartCollisions() {
		List<Heart> collectedHearts = new ArrayList<>();
		for (Heart heart : hearts) {
			if (user.getHitbox().getBoundsInParent().intersects(heart.getBoundsInParent())) {
				user.incrementHealth(); // 玩家生命值增加
				levelView.addHearts(user.getHealth()); // 更新左上角爱心
				collectedHearts.add(heart);
				root.getChildren().remove(heart); // 从场景中移除心形
			}
		}
		hearts.removeAll(collectedHearts); // 从列表中移除收集的心形
	}

	private void generateEnemyFire() {
		enemyUnits.forEach(enemy -> {
			ActiveActorDestructible projectile = ((FighterPlane) enemy).fireProjectile();
			if (projectile != null) {
				root.getChildren().add(projectile);
				if (projectile instanceof EnemyProjectile) {
					((EnemyProjectile) projectile).visualizeHitbox(root);
				}
				enemyProjectiles.add(projectile);
			}
		});
	}

	private void updateActors() {
		friendlyUnits.forEach(plane -> plane.updateActor());
		enemyUnits.forEach(enemy -> enemy.updateActor());
		userProjectiles.forEach(projectile -> projectile.updateActor());
		enemyProjectiles.forEach(projectile -> projectile.updateActor());
		hearts.forEach(Heart::updateActor);
	}

	private void removeAllDestroyedActors() {
		removeDestroyedActors(friendlyUnits);
		removeDestroyedActors(enemyUnits);
		removeDestroyedActors(userProjectiles);
		removeDestroyedActors(enemyProjectiles);
	}

	private void removeDestroyedActors(List<ActiveActorDestructible> actors) {
		List<ActiveActorDestructible> destroyedActors = actors.stream()
				.filter(ActiveActorDestructible::isDestroyed)
				.collect(Collectors.toList());
		root.getChildren().removeAll(destroyedActors);
		actors.removeAll(destroyedActors);
	}

	private void handlePlaneCollisions() {
		handleCollisions(friendlyUnits, enemyUnits);
	}

	private void handleUserProjectileCollisions() {
		handleCollisions(userProjectiles, enemyUnits);
	}

	private void handleEnemyProjectileCollisions() {
		handleCollisions(enemyProjectiles, friendlyUnits);
	}

	private void handleCollisions(List<ActiveActorDestructible> actors1, List<ActiveActorDestructible> actors2) {
		for (ActiveActorDestructible actor : actors2) {
			for (ActiveActorDestructible otherActor : actors1) {
				if (actor instanceof ActiveActor && otherActor instanceof ActiveActor) {
					Node actorHitbox = ((ActiveActor) actor).getHitbox();
					Node otherActorHitbox = ((ActiveActor) otherActor).getHitbox();
					if (actorHitbox.getBoundsInParent().intersects(otherActorHitbox.getBoundsInParent())) {
						actor.takeDamage();
						otherActor.takeDamage();
						if (actor.isDestroyed()) actor.destroy();
						if (otherActor.isDestroyed()) otherActor.destroy();
					}
				}
			}
		}
	}

	private void handleEnemyPenetration() {
		for (ActiveActorDestructible enemy : enemyUnits) {
			if (enemyHasPenetratedDefenses(enemy)) {
				user.takeDamage();
				enemy.destroy();
			}
		}
	}

	private void updateLevelView() {
		levelView.removeHearts(user.getHealth());
	}

	private void updateKillCount() {
		List<ActiveActorDestructible> destroyedEnemies = enemyUnits.stream()
				.filter(ActiveActorDestructible::isDestroyed)
				.collect(Collectors.toList());

		for (ActiveActorDestructible enemy : destroyedEnemies) {
			user.incrementKillCount();
		}

		enemyUnits.removeAll(destroyedEnemies);
		root.getChildren().removeAll(destroyedEnemies);

		currentNumberOfEnemies = enemyUnits.size();
	}

	private boolean enemyHasPenetratedDefenses(ActiveActorDestructible enemy) {
		return Math.abs(enemy.getTranslateX()) > screenWidth;
	}

	protected void winGame() {
		timeline.stop();
		levelView.showWinImage();
	}

	protected void loseGame() {
		timeline.stop();
		levelView.showGameOverImage();
	}

	protected UserPlane getUser() {
		return user;
	}

	protected Group getRoot() {
		return root;
	}

	protected int getCurrentNumberOfEnemies() {
		return enemyUnits.size();
	}

	protected void addEnemyUnit(ActiveActorDestructible enemy) {
		enemyUnits.add(enemy);
		root.getChildren().add(enemy);

		if (enemy instanceof Boss) {
			Boss boss = (Boss) enemy;
			root.getChildren().add(boss.getShieldImage());
			root.getChildren().add(boss.getHealthBar());
		}

		if (enemy instanceof ActiveActor) {
			((ActiveActor) enemy).visualizeHitbox(root);
		}
	}

	protected double getEnemyMaximumYPosition() {
		return enemyMaximumYPosition;
	}

	protected double getScreenWidth() {
		return screenWidth;
	}

	protected boolean userIsDestroyed() {
		return user.isDestroyed();
	}

	private void updateNumberOfEnemies() {
		currentNumberOfEnemies = enemyUnits.size();
	}
}
