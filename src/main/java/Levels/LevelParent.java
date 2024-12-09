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
import javafx.scene.text.Text; // 新增
import javafx.scene.text.Font; // 新增
import javafx.scene.paint.Color; // 新增
import javafx.scene.control.Button; // 新增
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import Entities.MutationBoss1;


public abstract class LevelParent extends Observable {
	private boolean isLevelSwitching= false;

	private static final double SCREEN_HEIGHT_ADJUSTMENT = 150;
	private static final int MILLISECOND_DELAY = 40;
	private final double screenHeight;
	private final double screenWidth;
	private final double enemyMaximumYPosition;

	public final Group root;
	private final Timeline timeline;
	private final UserPlane user;
	private final Scene scene;
	private final ImageView background;

	private final List<ActiveActorDestructible> friendlyUnits;
	public final List<ActiveActorDestructible> enemyUnits;
	private final List<ActiveActorDestructible> userProjectiles;
	private final List<ActiveActorDestructible> enemyProjectiles;
	private final List<Heart> hearts; // 存储心形道具

	private int currentNumberOfEnemies;
	private LevelView levelView;

	private boolean isSceneInitialized = false;
	private boolean isBackgroundInitialized = false;
	private boolean isPaused = false; // 是否暂停的标志位
	private VBox pauseMenu;          // 暂停菜单

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
		// 初始化暂停菜单
		initializePauseMenu();
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

		// 添加键盘事件监听
		scene.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.P) {
				togglePause();
			}
		});

		return scene;
	}

	private void initializePauseMenu() {
		pauseMenu = new VBox(10); // 垂直布局，间隔 10 像素
		pauseMenu.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-padding: 20;"); // 设置样式
		pauseMenu.setPrefSize(300, 200); // 设置菜单宽度和高度

		// 修改菜单位置，直接指定坐标
		pauseMenu.setLayoutX(500); // 设置菜单左上角的 X 坐标
		pauseMenu.setLayoutY(100); // 设置菜单左上角的 Y 坐标

		Text pauseText = new Text("Game Paused");
		pauseText.setFont(new Font(20));
		pauseText.setFill(Color.WHITE);

		Button resumeButton = new Button("Resume");
		resumeButton.setOnAction(e -> togglePause()); // 点击恢复按钮，切换暂停状态

		Button quitButton = new Button("Quit");
		quitButton.setOnAction(e -> System.exit(0)); // 退出游戏

		pauseMenu.getChildren().addAll(pauseText, resumeButton, quitButton);
		pauseMenu.setVisible(false); // 初始隐藏
		root.getChildren().add(pauseMenu);
	}

	// 改进的 togglePause 方法
	private void togglePause() {
		isPaused = !isPaused; // 切换暂停状态
		if (isPaused) {
			System.out.println("Game paused.");
			timeline.pause(); // 暂停游戏逻辑
			pauseMenu.setVisible(true); // 显示暂停菜单
			pauseMenu.toFront(); // 确保菜单在最前
		} else {
			System.out.println("Game resumed.");
			timeline.play(); // 恢复游戏逻辑
			pauseMenu.setVisible(false); // 隐藏暂停菜单
		}
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
		Timeline switchTimeline = new Timeline(new KeyFrame(Duration.millis(2), e -> {
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
	public void cleanUpLevel() {
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
			if (enemy instanceof MutationBoss1) {
				((MutationBoss1) enemy).fireProjectile(root, enemyProjectiles); // 调用 MutationBoss1 特定的方法
			} else if (enemy instanceof Boss) {
				System.out.println("Boss detected. Calling its fireProjectile method...");
				ActiveActorDestructible projectile = ((Boss) enemy).fireProjectile(); // 调用 Boss 的方法
				if (projectile != null) {
					root.getChildren().add(projectile); // 添加到场景中
					enemyProjectiles.add(projectile);   // 添加到子弹列表
				}
			} else if (enemy instanceof FighterPlane) {
				System.out.println("Other FighterPlane detected. Calling its fireProjectile method...");
				ActiveActorDestructible projectile = ((FighterPlane) enemy).fireProjectile(); // 默认行为
				if (projectile != null) {
					root.getChildren().add(projectile); // 添加到场景中
					enemyProjectiles.add(projectile);   // 添加到子弹列表
				}
			} else {
				System.out.println("Unknown enemy type: " + enemy.getClass().getSimpleName());
			}
		});
	}



	private void updateActors() {
		friendlyUnits.forEach(unit -> unit.updateActor());
		enemyUnits.forEach(unit -> unit.updateActor());
		userProjectiles.forEach(projectile -> projectile.updateActor());
		enemyProjectiles.forEach(projectile -> {
			projectile.updateActor(); // 调用子弹的更新逻辑


			// 如果子弹超出屏幕，移除它
			if (projectile.getLayoutX() < 0) {
				root.getChildren().remove(projectile);
				enemyProjectiles.remove(projectile);
				System.out.println("Projectile removed for leaving screen bounds.");
			}
		});
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

		for (ActiveActorDestructible projectile : enemyProjectiles) {
			if (actorHasPenetratedDefenses(projectile)) {
				projectile.destroy();
			}
		}

		for (ActiveActorDestructible projectile : userProjectiles) {
			if (actorHasPenetratedDefenses(projectile)) {
				projectile.destroy();
			}
		}
	}

	private boolean actorHasPenetratedDefenses(ActiveActorDestructible actor) {
		return Math.abs(actor.getTranslateX()) > 1000;
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
		if (enemyUnits.contains(enemy)) {
			System.out.println("Enemy already in enemyUnits: " + enemy.getClass().getSimpleName());
			return;
		}

		enemyUnits.add(enemy);
		root.getChildren().add(enemy);
		System.out.println("Enemy added: " + enemy.getClass().getSimpleName());

		if (enemy instanceof Boss) {
			Boss boss = (Boss) enemy;

			if (!root.getChildren().contains(boss.getShieldImage())) {
				root.getChildren().add(boss.getShieldImage());
				System.out.println("Shield image added for Boss.");
			}
			if (!root.getChildren().contains(boss.getHealthBar())) {
				root.getChildren().add(boss.getHealthBar());
				System.out.println("Health bar added for Boss.");
			}
		}

		if (enemy instanceof ActiveActor) {
			((ActiveActor) enemy).visualizeHitbox(root);
			System.out.println("Hitbox visualized for: " + enemy.getClass().getSimpleName());
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