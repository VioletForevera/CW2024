package Levels;

import java.util.*;
import java.util.stream.Collectors;

import Core.ActiveActor;
import Core.ActiveActorDestructible;
import Core.FighterPlane;
import Entities.Boss;
import Entities.UserPlane;
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

/**
 * Abstract base class for all game levels.
 * Serves as the parent class for specific levels, providing common functionality
 * such as initializing friendly and enemy units, handling game-over conditions,
 * and transitioning between levels.
 * Extends {@link Observable} to support observer-based notifications for game state changes.
 */

public abstract class LevelParent extends Observable {
	/**
	 * Indicates whether the level is currently switching to another level.
	 * Prevents multiple level transitions from being triggered simultaneously.
	 */
	protected boolean isLevelSwitching= false;

	/**
	 * Adjustment value used to calculate the maximum Y-coordinate for enemy movement.
	 * Ensures enemies stay within the visible screen area.
	 */
	private static final double SCREEN_HEIGHT_ADJUSTMENT = 150;
	/**
	 * Delay in milliseconds for each frame in the game loop.
	 * Determines the update frequency of the game logic and rendering.
	 */
	private static final int MILLISECOND_DELAY = 40;
	/**
	 * The height of the game screen.
	 * Used for positioning and movement calculations.
	 */
	private final double screenHeight;
	/**
	 * The width of the game screen.
	 * Used for positioning and movement calculations.
	 */
	private final double screenWidth;
	/**
	 * The maximum Y-coordinate that enemies can move to.
	 * Calculated based on the screen height and a fixed adjustment value.
	 */
	private final double enemyMaximumYPosition;
	/**
	 * The root container for all graphical elements in the level.
	 * Serves as the base group for JavaFX scene graph nodes.
	 */
	public final Group root;
	/**
	 * The timeline responsible for running the game loop.
	 * Manages the periodic execution of game logic and rendering updates.
	 */
	private final Timeline timeline;
	/**
	 * The player's plane object.
	 * Represents the player's character in the game.
	 */
	private final UserPlane user;
	/**
	 * The JavaFX Scene object representing the level.
	 * Contains all visual elements and handles user input.
	 */
	private final Scene scene;
	/**
	 * The background image of the level.
	 * Provides the visual backdrop for the game environment.
	 */
	private final ImageView background;
	/**
	 * A list of all friendly units in the level, including the player's plane.
	 * Used for managing interactions and collisions.
	 */
	private final List<ActiveActorDestructible> friendlyUnits;
	/**
	 * A list of all enemy units currently present in the level.
	 * Used for managing interactions, collisions, and rendering.
	 */
	public final List<ActiveActorDestructible> enemyUnits;
	/**
	 * A list of all projectiles fired by the player.
	 * Used for managing projectile interactions and collisions.
	 */
	private final List<ActiveActorDestructible> userProjectiles;
	/**
	 * A list of all projectiles fired by enemies.
	 * Used for managing projectile interactions and collisions.
	 */
	private final List<ActiveActorDestructible> enemyProjectiles;
	/**
	 * A list of heart items available in the level.
	 * Hearts can be collected by the player to restore health.
	 */
	private final List<Heart> hearts; // 存储心形道具
	/**
	 * The current number of enemies in the level.
	 * Updated dynamically as enemies are spawned and destroyed.
	 */
	private int currentNumberOfEnemies;
	/**
	 * The view component for the level.
	 * Manages the display of UI elements such as the player's health bar.
	 */
	private LevelView levelView;
	/**
	 * Indicates whether the scene has been initialized.
	 * Ensures that initialization logic is only executed once.
	 */
	private boolean isSceneInitialized = false;
	/**
	 * Indicates whether the background has been initialized.
	 * Ensures that background setup is only performed once.
	 */
	private boolean isBackgroundInitialized = false;
	/**
	 * Indicates whether the game is currently paused.
	 * Used to toggle game logic and display the pause menu.
	 */
	protected boolean isPaused = false; // 是否暂停的标志位
	/**
	 * The pause menu displayed when the game is paused.
	 * Provides options to resume or quit the game.
	 */
	protected VBox pauseMenu;          // 暂停菜单
	/**
	 * Constructs a new instance of {@code LevelParent} with the specified parameters.
	 * This constructor initializes the game level with a background image, screen dimensions,
	 * initial player health, and various game components such as friendly units, enemy units,
	 * projectiles, and a timeline for the game loop.
	 *
	 * @param backgroundImageName the file path to the background image.
	 * @param screenHeight the height of the game screen.
	 * @param screenWidth the width of the game screen.
	 * @param playerInitialHealth the initial health of the player's plane.
	 */

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

	/**
	 * Initializes the friendly units for the level.
	 * This method is responsible for adding the player's plane and other allied entities to the game scene.
	 * It must be implemented by subclasses to define level-specific initialization logic.
	 */
	protected abstract void initializeFriendlyUnits();

	/**
	 * Checks if the game is over in the current level.
	 * This method determines whether the player has lost or won the level and triggers appropriate actions.
	 * It must be implemented by subclasses to handle level-specific game-over conditions.
	 */
	protected abstract void checkIfGameOver();

	/**
	 * Spawns enemy units in the level.
	 * This method manages the addition of enemy planes or other adversarial entities to the game scene.
	 * Subclasses must implement this method to define level-specific enemy spawning behavior.
	 */
	protected abstract void spawnEnemyUnits();

	/**
	 * Instantiates the level view for the current level.
	 * The level view handles the UI elements, such as health bars and progress indicators, for the level.
	 * Subclasses must implement this method to create and return a level-specific view instance.
	 *
	 * @return the {@link LevelView} instance for the current level.
	 */
	protected abstract LevelView instantiateLevelView();


	/**
	 * Initializes the game scene with necessary components and event listeners.
	 * <p>
	 * This method ensures that the background, friendly units, and visual elements like
	 * the heart display are initialized. It also adds a key press listener to handle
	 * user input for pausing the game. If the scene is already initialized, it avoids
	 * reinitializing the components.
	 * </p>
	 *
	 * @return the initialized {@link Scene} object for the game level.
	 */

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

	/**
	 * Initializes the pause menu UI and adds it to the game root.
	 * <p>
	 * The pause menu consists of a styled {@link VBox} containing a text label
	 * ("Game Paused"), a resume button to resume the game, and a quit button to exit
	 * the application. The menu is hidden by default and can be displayed when the game
	 * is paused.
	 * </p>
	 */
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
	/**
	 * Toggles the game's pause state.
	 * <p>
	 * When the game is paused, the timeline is stopped, and the pause menu is displayed.
	 * Resuming the game hides the pause menu and restarts the timeline. This method ensures
	 * the pause and resume actions are reflected visually and logically.
	 * </p>
	 */

	// 改进的 togglePause 方法
	protected void togglePause() {
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

	/**
	 * Starts the game by focusing on the background and playing the game timeline.
	 * <p>
	 * If the game timeline is not already running, this method sets the focus on the
	 * background element and starts the timeline animation, which drives the game loop.
	 * </p>
	 */

	public void startGame() {
		background.requestFocus();
		if (timeline.getStatus() != Animation.Status.RUNNING) {
			timeline.play();
		}
	}
	/**
	 * Transitions to the specified next game level.
	 * <p>
	 * This method handles the level-switching logic, ensuring that the current level is cleaned
	 * up before moving to the next one. It prevents duplicate calls by using a flag and uses a
	 * {@link Timeline} for delayed execution of the level transition. Observers are notified of the
	 * next level's name to facilitate the transition.
	 * </p>
	 *
	 * @param levelName the fully qualified class name of the next level to load.
	 */

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


	/**
	 * Cleans up resources and resets the current level state.
	 * <p>
	 * This method clears all actors, projectiles, and other game objects from the scene graph
	 * and associated lists. It ensures that the current level's resources are fully released
	 * before transitioning to a new level.
	 * </p>
	 */
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

	/**
	 * Updates the game scene during each frame of the timeline.
	 * <p>
	 * This method handles various aspects of the game loop, including:
	 * <ul>
	 *   <li>Removing destroyed actors</li>
	 *   <li>Spawning enemy units and power-ups</li>
	 *   <li>Updating actor positions</li>
	 *   <li>Handling collisions and interactions</li>
	 *   <li>Updating kill counts and the level view</li>
	 *   <li>Checking for game-over conditions</li>
	 * </ul>
	 */
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
	/**
	 * Initializes the game timeline for managing the game loop.
	 * <p>
	 * This method creates a {@link Timeline} with an indefinite cycle count and associates a
	 * {@link KeyFrame} that updates the game scene at regular intervals defined by the
	 * {@code MILLISECOND_DELAY}.
	 * </p>
	 */

	private void initializeTimeline() {
		timeline.setCycleCount(Timeline.INDEFINITE);
		KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> updateScene());
		timeline.getKeyFrames().add(gameLoop);
	}

	/**
	 * Initializes the game background and sets up input listeners.
	 * <p>
	 * This method ensures that the background is correctly displayed and interactive. It assigns
	 * keyboard input listeners for controlling the user's plane and firing projectiles. If the
	 * background has already been initialized, this method logs a message and skips reinitialization.
	 * </p>
	 */
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

	/**
	 * Fires a projectile from the user's plane.
	 * <p>
	 * This method invokes the user's `fireProjectile` method to create a new projectile.
	 * If a projectile is successfully created, it is added to the scene and the list of
	 * active user projectiles.
	 * </p>
	 */
	private void fireProjectile() {
		ActiveActorDestructible projectile = user.fireProjectile();
		if (projectile != null) {
			root.getChildren().add(projectile);
			/*if (projectile instanceof UserProjectile) {
				((UserProjectile) projectile).visualizeHitbox(root);
			}*/
			userProjectiles.add(projectile);
		}
	}
	/**
	 * Spawns heart items randomly on the screen.
	 * <p>
	 * This method generates heart items with a specified probability. Hearts are
	 * added to the scene and move from right to left while oscillating vertically.
	 * When the movement animation completes, the heart is removed from the scene
	 * and the list of hearts.
	 * </p>
	 */

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

	/**
	 * Handles collisions between the user's plane and hearts.
	 * <p>
	 * This method detects collisions between the user's plane and heart items.
	 * When a collision is detected, the user's health is incremented, the level view
	 * is updated to reflect the new health, and the heart is removed from the scene
	 * and the list of hearts.
	 * </p>
	 */
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

	/**
	 * Generates projectiles fired by enemy units.
	 * <p>
	 * This method iterates through all enemy units and calls their respective
	 * `fireProjectile` methods to create projectiles. The projectiles are added
	 * to the scene and the list of active enemy projectiles.
	 * </p>
	 * <p>
	 * Specific behaviors are implemented for {@link MutationBoss1} and {@link Boss},
	 * while other {@link FighterPlane} enemies follow a default firing behavior.
	 * </p>
	 */
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

	/**
	 * Updates the state of all active actors in the scene.
	 * <p>
	 * This method updates the positions and logic for all friendly units, enemy units,
	 * user projectiles, and enemy projectiles. It also removes enemy projectiles that
	 * exit the screen.
	 * </p>
	 */

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

	/**
	 * Removes all destroyed actors from the scene and their respective lists.
	 * <p>
	 * This method iterates through all categories of actors (friendly units, enemy units,
	 * user projectiles, and enemy projectiles) and removes any that are marked as destroyed.
	 * </p>
	 */
	private void removeAllDestroyedActors() {
		removeDestroyedActors(friendlyUnits);
		removeDestroyedActors(enemyUnits);
		removeDestroyedActors(userProjectiles);
		removeDestroyedActors(enemyProjectiles);
	}

	/**
	 * Removes destroyed actors from a specified list and the scene.
	 * <p>
	 * This helper method filters out destroyed actors from the provided list, removes
	 * them from the scene graph, and updates the list to exclude the destroyed actors.
	 * </p>
	 *
	 * @param actors the list of actors to check for destruction and remove.
	 */
	private void removeDestroyedActors(List<ActiveActorDestructible> actors) {
		List<ActiveActorDestructible> destroyedActors = actors.stream()
				.filter(ActiveActorDestructible::isDestroyed)
				.collect(Collectors.toList());
		root.getChildren().removeAll(destroyedActors);
		actors.removeAll(destroyedActors);
	}

	/**
	 * Handles collisions between friendly units and enemy units.
	 * <p>
	 * This method detects and processes collisions between all friendly units (e.g., the player's plane)
	 * and enemy units. Upon collision, the involved actors take damage and are destroyed if their health
	 * reaches zero.
	 * </p>
	 */
	private void handlePlaneCollisions() {
		handleCollisions(friendlyUnits, enemyUnits);
	}

	/**
	 * Handles collisions between user projectiles and enemy units.
	 * <p>
	 * This method checks for intersections between the hitboxes of user-fired projectiles and enemy units.
	 * When a collision is detected, both the projectile and the enemy take damage, and are destroyed if
	 * their health reaches zero.
	 * </p>
	 */
	private void handleUserProjectileCollisions() {
		handleCollisions(userProjectiles, enemyUnits);
	}

	/**
	 * Handles collisions between enemy projectiles and friendly units.
	 * <p>
	 * This method detects intersections between the hitboxes of enemy-fired projectiles and friendly units.
	 * Upon collision, the involved actors take damage and are destroyed if their health drops to zero.
	 * </p>
	 */
	private void handleEnemyProjectileCollisions() {
		handleCollisions(enemyProjectiles, friendlyUnits);
	}

	/**
	 * Generalized collision handling between two groups of actors.
	 * <p>
	 * This method iterates through two lists of actors and checks for intersections between their hitboxes.
	 * If a collision is detected, both actors take damage, and are marked as destroyed if their health reaches zero.
	 * </p>
	 *
	 * @param actors1 the first list of actors to check for collisions.
	 * @param actors2 the second list of actors to check for collisions.
	 */
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

	/**
	 * Handles cases where enemies or projectiles penetrate defensive boundaries.
	 * <p>
	 * This method checks if enemy units or projectiles have exceeded the defensive boundaries
	 * (e.g., moved off-screen or into restricted areas). Upon detection, the relevant enemy or
	 * projectile is marked as destroyed, and the user may take damage in case of enemy penetration.
	 * </p>
	 */
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
	/**
	 * Determines if an actor has penetrated defensive boundaries.
	 * <p>
	 * This method checks if the actor's position exceeds predefined screen boundaries, indicating
	 * that it has penetrated defenses.
	 * </p>
	 *
	 * @param actor the actor to check for boundary penetration.
	 * @return {@code true} if the actor has penetrated defenses, {@code false} otherwise.
	 */

	private boolean actorHasPenetratedDefenses(ActiveActorDestructible actor) {
		return Math.abs(actor.getTranslateX()) > 1000;
	}
	/**
	 * Updates the level view to reflect changes in the user's state.
	 * <p>
	 * This method synchronizes the visual elements of the level view, such as hearts or health indicators,
	 * based on the user's current health.
	 * </p>
	 */

	private void updateLevelView() {
		levelView.removeHearts(user.getHealth());
	}
	/**
	 * Updates the kill count based on destroyed enemies.
	 * <p>
	 * This method identifies enemies that have been destroyed, increments the user's kill count for each,
	 * and removes the destroyed enemies from the scene and enemy list.
	 * </p>
	 */

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
	/**
	 * Checks if an enemy has exceeded the screen width, indicating a penetration.
	 * <p>
	 * This method evaluates whether an enemy has moved off-screen horizontally, signaling
	 * that it has bypassed defenses.
	 * </p>
	 *
	 * @param enemy the enemy to check.
	 * @return {@code true} if the enemy has penetrated defenses, {@code false} otherwise.
	 */

	private boolean enemyHasPenetratedDefenses(ActiveActorDestructible enemy) {
		return Math.abs(enemy.getTranslateX()) > screenWidth;
	}
	/**
	 * Triggers the win game sequence.
	 * <p>
	 * This method stops the game timeline and displays a win image to indicate that the user has won the game.
	 * </p>
	 */

	protected void winGame() {
		timeline.stop();
		levelView.showWinImage();
	}
	/**
	 * Triggers the lose game sequence.
	 * <p>
	 * This method stops the game timeline and displays a game-over image to indicate that the user has lost the game.
	 * </p>
	 */

	protected void loseGame() {
		timeline.stop();
		levelView.showGameOverImage();
	}
	/**
	 * Retrieves the user's plane.
	 *
	 * @return the {@link UserPlane} representing the player's plane.
	 */

	protected UserPlane getUser() {
		return user;
	}
	/**
	 * Retrieves the root node of the scene graph.
	 *
	 * @return the {@link Group} root node of the game scene.
	 */

	protected Group getRoot() {
		return root;
	}
	/**
	 * Retrieves the current number of enemy units in the game.
	 *
	 * @return the count of enemy units.
	 */

	protected int getCurrentNumberOfEnemies() {
		return enemyUnits.size();
	}
	/**
	 * Adds an enemy unit to the game scene and enemy list.
	 * <p>
	 * This method ensures that the enemy is added to the scene graph and the list of active enemy units.
	 * If the enemy is a boss, it also ensures the boss's shield image and health bar are added to the scene.
	 * </p>
	 *
	 * @param enemy the enemy unit to add.
	 */

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


	}

	/**
	 * Retrieves the maximum Y position allowed for enemy units.
	 *
	 * @return the maximum Y position for enemy units.
	 */

	protected double getEnemyMaximumYPosition() {
		return enemyMaximumYPosition;
	}
	/**
	 * Retrieves the width of the game screen.
	 *
	 * @return the width of the screen.
	 */

	protected double getScreenWidth() {
		return screenWidth;
	}
	/**
	 * Checks if the user's plane is destroyed.
	 *
	 * @return {@code true} if the user's plane is destroyed, {@code false} otherwise.
	 */

	protected boolean userIsDestroyed() {
		return user.isDestroyed();
	}
	/**
	 * Updates the current count of active enemy units.
	 * <p>
	 * This method synchronizes the count of active enemy units with the size of the enemy list.
	 * </p>
	 */
	private void updateNumberOfEnemies() {
		currentNumberOfEnemies = enemyUnits.size();
	}
}