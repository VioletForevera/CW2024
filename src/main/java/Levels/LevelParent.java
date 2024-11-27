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
import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.Node;
import javafx.util.Duration;

public abstract class LevelParent extends Observable {

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

			// 键盘按下事件
			background.setOnKeyPressed(e -> {
				KeyCode kc = e.getCode();
				if (kc == KeyCode.UP) user.moveUp();
				if (kc == KeyCode.DOWN) user.moveDown();
				if (kc == KeyCode.LEFT) user.moveLeft(); // 新增：向左移动
				if (kc == KeyCode.RIGHT) user.moveRight(); // 新增：向右移动
				if (kc == KeyCode.SPACE) fireProjectile(); // 发射子弹
			});

			// 键盘释放事件
			background.setOnKeyReleased(e -> {
				KeyCode kc = e.getCode();
				if (kc == KeyCode.UP || kc == KeyCode.DOWN) user.stopVertical();
				if (kc == KeyCode.LEFT || kc == KeyCode.RIGHT) user.stopHorizontal(); // 新增：停止水平移动
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

		for (ActiveActorDestructible enemy : enemyUnits) {
			if (enemy instanceof Boss) {
				((Boss) enemy).updateHealthBar();
			}
		}
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
