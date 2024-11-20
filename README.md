#Project Structure Refactoring
The project has been reorganized into multiple packages to improve readability and maintainability. Each package has a specific purpose:

**Core
Contains core functionality and base classes, such as ActiveActor, which serves as the parent class for all active entities in the game.

**Entities
Contains specific game entities, including:

Boss: The boss character in the game, which includes a shield and a health bar.
EnemyPlane and UserPlane: Represent enemy and player-controlled planes.
**Levels
Manages different game levels:

LevelOne: The first level of the game, where the player battles standard enemies.
LevelTwo: Introduces the boss fight along with additional mechanics.
**Ui
Manages user interface components, such as:

ShieldImage: Displays the shield effect for the boss or player.
HeartDisplay: Shows the player's remaining health visually.
#Issues Encountered
#Path Problems After Refactoring
Problem: During the package reorganization, the paths to image assets (e.g., shield.png, bossplane.png) were temporarily broken.
Solution: Adjusted all resource paths to use a consistent structure within the resources directory.
#Temporary Shield Visibility Issue
Problem: After refactoring, the boss's shield initially failed to display.
Solution: Ensured ShieldImage was correctly added to the scene's root node and its position updated with the boss's movements.
#Future Plans for Refactoring
Enhance Utils Package
Create utility classes for common tasks:

ResourceManager: Centralized resource loading for images and sounds.
CollisionDetector: Simplified collision detection between entities.
Extract Hitbox Management
Move hitbox-related logic from ActiveActor to a dedicated HitboxManager class for cleaner code and easier debugging.

Optimize LevelParent
Refactor to abstract common level logic, allowing child classes to focus only on unique features.

Introduce Design Patterns
Apply patterns such as:

Factory Pattern for enemy creation.
Observer Pattern for updating UI components based on game state changes.
Summary
This project structure and refactoring plan aim to enhance the codebase's maintainability and scalability, ensuring a more robust and modular design.
