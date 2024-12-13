# COMP2042 Coursework

## Table of Contents
- [GitHub Repository](#github-repository)
- [Compilation Instructions](#compilation-instructions)
- [Implemented and Working Properly](#implemented-and-working-properly)
- [Implemented but Not Working Properly](#implemented-but-not-working-properly)
- [Features Not Implemented](#features-not-implemented)
- [New Java Classes](#new-java-classes)
- [Modified Java Classes](#modified-java-classes)
- [Unexpected Problems](#unexpected-problems)

## GitHub Repository
- GitHub Link: [Repository Link](https://github.com/VioletForevera/DMS-CW2024.git)




## Implemented and Working Properly

**Multi-level flow (LevelOne, LevelTwo, LevelThree):**  
Players can defeat enemies or bosses to fulfill passing conditions. The game transitions smoothly between levels, and the logic is accurate.

**Enemy Generation and Cleanup Mechanics:**  
Enemies spawn at the right time and location, and are instantly removed when destroyed, ensuring correct kill counts and efficient memory usage.

**Boss Mechanics (Shield, Life Bar, Adjustable Hitbox):**  
Bosses are equipped with shield display (can be shown or hidden), a dynamic life bar (width updates on damage), and a `setHitboxSize()` method to flexibly adjust the hitbox size, improving battle strategy and debugging accuracy.

**Mutation Boss (MutationBoss1) features:**  
Add a new special boss class `MutationBoss1` with random vertical movement and multi-directional bullet shooting mode to increase the challenge and depth of the level. This feature has been successfully integrated and works properly in the corresponding levels.

**Main Menu, Background Music and Volume Control:**  
The main menu interface has a customizable background image and a looping soundtrack. A slider in the settings screen adjusts the music volume in real time, enhancing the player experience.

**Heart-shaped props and life value display (Heart, HeartDisplay):**  
Heart-shaped props can be randomly generated, and players pick them up to restore life. `HeartDisplay` shows the current life value in real time. The UI updates instantly when the player is injured or regains life.

**Visualized and adjustable Hitbox:**  
The Hitbox is visualized and adjustable by offset and size, aiding accurate collision detection and debugging optimization.

**GlobalMusic:**  
A singleton class `GlobalMusic` keeps the background music playing continuously between levels without reloading or initializing music resources.

**Pause menu and win/lose menu (PauseMenu, WinImage, GameOverImage):**  
Pressing a specific key (e.g. `P`) pauses the game and displays `PauseMenu`. On win/lose, `WinImage`/`GameOverImage` appears, offering exit or limited return-to-menu options, seamlessly integrated into the game flow.

**Projectiles are automatically cleared off-screen:**  
Bullets and enemies out of screen range are automatically destroyed, preventing memory buildup and maintaining stable performance.

---

## Implemented but Not Working Properly

**The ability to return to the main menu to restart the game:**  
Tried to let players return to the main menu and restart the whole game process after the game ends. Due to the JavaFX `Application.launch()` limitation, restarting in the same process triggers `IllegalStateException`. Attempts to hide the window and rebuild the scene were unsuccessful, so this feature is not fully functional in the current version.

---

## Features Not Implemented

**Restart the game flow completely from the main menu:**  
Originally planned to allow the player to restart the entire game cycle from the main menu without exiting the app. After multiple attempts, this feature was abandoned. Players must close and restart the application to restart the game.

---

## New Java Classes

Comparing the structure before and after the changes, the following classes are new and introduce new features and a clearer structure:

**MutationBoss1 (Entities package):**  
A special Boss class adding random vertical movement and multi-directional bullet shooting mode to enhance challenge and variety.

**Heart (Entities package):**  
Generates a pickable heart-shaped prop that restores the player’s life after picking it up. Works with `HeartDisplay` to synchronize UI with health changes.

**MainMenu (Ui package):**  
Main menu interface class, including "Start Game", "Settings", and "Exit" buttons and background music logic. It provides entry for players to start the game, adjust settings, or exit.

**GameWindow (Ui package):**  
A class used to manage the main stage or window of the game (if such functionality is implemented), responsible for scene display and switching, providing a unified display platform for the main menu, levels, and other interfaces.

**MusicPlayer (sounds package):**  
Music and sound effects management class. Provides `play()`, `stop()`, `setVolume()`, `playEffect()` and other methods for background music and sound effects. With `GlobalMusic` running as a single instance or independently, it ensures flexible and efficient audio control.


---

## Modified Java Classes

**LevelParent and its subclasses (LevelOne, LevelTwo, LevelThree)**  
**6/11 Update:**  
- Added `isSceneInitialized` to avoid duplicate initialization of scenes.  
- Updated `updateKillCount()` for accurate kill counts.  
- Used `setChanged()`, `notifyObservers()` in `goToNextLevel()` to ensure accurate BossLevel transition.  
- Optimized `spawnEnemyUnits()`, `removeAllDestroyedActors()` to improve enemy generation cleanup.

**24/11 (LevelThree integration):**  
- Added `LevelThree` class to introduce random enemy generation and a kill target determination mechanism.

**30/11 (Heart Props):**  
- Added `spawnHearts()` to `LevelParent` to periodically generate `Heart`. Picking up Heart restores life and calls `addHearts()` to update UI.

**2/12 (to avoid duplicating into LevelTwo):**  
- Added `isLevelSwitching` flag to control level switching rhythm, reset after a delay to prevent entering the same level repeatedly.

Subsequent updates integrate `PauseMenu`, `GlobalMusic`, and `Win/GameOver` menu triggers to ensure stable level and UI interaction.

**Boss Classes (Updated 7/11, 13/11, 15/11)**  
**Updated 7/11 (Shield display):**  
- New `ShieldImage shieldImage` in Boss constructor, dynamically show/hide shield and synchronize position in `updatePosition()`, `updateShield()`.

**Updated 13/11,15/11 (lifebar and Hitbox):**  
- Add `healthBar` and `updateHealthBar()` to dynamically adjust the life bar width according to boss health.  
- Adjust the size and position of the collision box by `setHitboxSize()` and `updateHitbox()` to improve the accuracy of collision detection.

**MainMenu Class (Updated on 13/11)**  
- Add `BackgroundPanel` or similar component to display the background image of the main menu.  
- Initialize `MusicPlayer/GlobalMusic` instance, `play()` BGM when the menu is displayed.  
- Add volume slider listener in Settings, call `setVolume()` to adjust music volume in real-time when value changes.

**UserPlane, UserProjectile, BossProjectile, FighterPlane and other entity classes**  
**UserPlane:**  
- Add `moveLeft()`, `moveRight()` for omni-directional movement, interacting with keyboard events to improve player mobility.

**UserProjectile, BossProjectile:**  
- `BossProjectile` supports multi-directional shooting mode.  
- `UserProjectile` automatically calls `destroy()` when off-screen to optimize performance and memory usage.

**FighterPlane:**  
- Call `playEffect()` in `takeDamage()` to play injury sound and update UI according to health changes.

**Ui related classes (WinImage, GameOverImage, PauseMenu, HeartDisplay, ShieldImage)**  
**WinImage, GameOverImage:**  
- Call `showWinImage()/showGameOverImage()` when the player wins/loses to show the corresponding menu and provide an exit option.

**PauseMenu:**  
- Add a new pause menu class with Resume and Quit buttons. Control pause/resume in `togglePause()`, trigger pause by `P` key, allowing mid-game adjustments.

**HeartDisplay:**  
- Display the initial number of hearts when initializing. `addHeart()` and `removeHeart()` update the UI when the player's life changes.

**ShieldImage:**  
- Collaborate with Boss, update shield display and position when Boss moves or shield status changes.

**Controller, Main classes (to be updated later)**  
- Adjust the startup process to avoid repeating `Application.launch()` which raises exceptions.  
- Use `isLevelSwitching` flag and delay timer to control level switching rhythm.  
- Schedule `GlobalMusic`, `MainMenu`, `PauseMenu`, `Win/GameOver` menu calls in `Controller` to harmonize UI with level switching and music playing.

**MusicPlayer, GlobalMusic (multiple updates)**  
- `GlobalMusic` single instance ensures continuous background music play between levels.  
- `playEffect()` plays sound effects in time during events like player injury or enemy explosion, improving feedback.  
- Call `GlobalMusic.getInstance().play()` at the beginning of the MainMenu and level to play music seamlessly.

---

## Unexpected Problems

**Repeated scene initialization:**  
The `isSceneInitialized` flag and checks in `initializeScene()` prevent duplicate scene setups.

**Inaccurate kill count:**  
Increase kill count before removing enemies in `updateKillCount()` to ensure accurate counts.

**Boss shield display abnormality:**  
Add `ShieldImage` to Boss class and synchronize position/state in `updatePosition()` and `updateShield()` to fix shield issues.

**Go to BossLevel exception:**  
Call `setChanged()`, `notifyObservers(levelName)` in `goToNextLevel()` ensuring observers respond correctly to level transitions.

**Return to main menu to restart exception:**  
Abandoned after several attempts due to JavaFX limitations (`launch()` cannot be restarted in the same process).

**Resource path and package structure refactoring issue:**  
Use `getResource()` after refactoring package structure to load resource files and solve path loading failures.

**Repeated triggering of multiple level switches:**  
Use `isLevelSwitching` flag and delay reset strategy to avoid entering the same level multiple times in a row.
