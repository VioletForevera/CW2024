package com.example.demo;

import java.util.*;
import java.util.stream.Collectors;
import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.util.Duration;
import javafx.scene.Node;

public abstract class LevelParent extends Observable {

	private static final double SCREEN_HEIGHT_ADJUSTMENT = 150;
	private static final int MILLISECOND_DELAY = 50;
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

	private int currentNumberOfEnemies;
	private LevelView levelView;

	private boolean isSceneInitialized = false;
	private boolean isBackgroundInitialized = false;

	public LevelParent(String backgroundImageName, double screenHeight, double screenWidth, int playerInitialHealth) {
		this.root = new Group();
		this.scene = new Scene(root, screenWidth, screenHeight);
		this.timeline = new Timeline();
		this.user = new UserPlane(playerInitialHealth);
		this.friendlyUnits = new ArrayList<>();
		this.enemyUnits = new ArrayList<>();
		this.userProjectiles = new ArrayList<>();
		this.enemyProjectiles = new ArrayList<>();

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
		setChanged();
		notifyObservers(levelName);
	}

	private void updateScene() {
		removeAllDestroyedActors();
		spawnEnemyUnits();
		updateActors();
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
				if (kc == KeyCode.SPACE) fireProjectile();
			});
			background.setOnKeyReleased(e -> {
				KeyCode kc = e.getCode();
				if (kc == KeyCode.UP || kc == KeyCode.DOWN) user.stop();
			});
			root.getChildren().add(background);
			isBackgroundInitialized = true;
		} else {
			System.out.println("Background already initialized.");
		}
	}

	private void fireProjectile() {
		ActiveActorDestructible projectile = user.fireProjectile();
		root.getChildren().add(projectile);
		userProjectiles.add(projectile);
	}

	private void generateEnemyFire() {
		enemyUnits.forEach(enemy -> spawnEnemyProjectile(((FighterPlane) enemy).fireProjectile()));
	}

	private void spawnEnemyProjectile(ActiveActorDestructible projectile) {
		if (projectile != null) {
			root.getChildren().add(projectile);
			enemyProjectiles.add(projectile);
		}
	}

	private void updateActors() {
		friendlyUnits.forEach(plane -> plane.updateActor());
		enemyUnits.forEach(enemy -> enemy.updateActor());
		userProjectiles.forEach(projectile -> projectile.updateActor());
		enemyProjectiles.forEach(projectile -> projectile.updateActor());

		// 更新Boss血条位置
		for (ActiveActorDestructible enemy : enemyUnits) {
			if (enemy instanceof Boss) {
				((Boss) enemy).updateHealthBar(); // 确保Boss血条跟随Boss移动
			}
		}
	}

	private void removeAllDestroyedActors() {
		removeDestroyedActors(friendlyUnits);
		removeDestroyedActors(enemyUnits);
		removeDestroyedActors(userProjectiles);
		removeDestroyedActors(enemyProjectiles);

		// 移除被销毁的Boss的血条
		for (ActiveActorDestructible enemy : enemyUnits) {
			if (enemy instanceof Boss && enemy.isDestroyed()) {
				root.getChildren().remove(((Boss) enemy).getHealthBar()); // 移除Boss的血条
			}
		}
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
				// 检查是否是 ActiveActor 类型，并使用其 hitbox 进行碰撞检测
				if (actor instanceof ActiveActor && otherActor instanceof ActiveActor) {
					Node actorHitbox = ((ActiveActor) actor).getHitbox();
					Node otherActorHitbox = ((ActiveActor) otherActor).getHitbox();
					if (actorHitbox.getBoundsInParent().intersects(otherActorHitbox.getBoundsInParent())) {
						// 碰撞后处理伤害逻辑
						actor.takeDamage();
						otherActor.takeDamage();
						// 如果被销毁，则调用 destroy 方法
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
			System.out.println("Destroyed an enemy, current kill count: " + user.getNumberOfKills());
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
			root.getChildren().add(boss.getShieldImage());  // 添加护盾
			root.getChildren().add(boss.getHealthBar());    // 添加血条
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
