Synchronize Hitbox with Object Movement
Ensured that the hitbox dynamically updates its position as the game object moves.
Code snippet:
java
复制代码
public void updateHitbox() {
    if (hitbox != null) {
        hitbox.setLayoutX(getLayoutX() + getTranslateX() + hitboxOffsetX);
        hitbox.setLayoutY(getLayoutY() + getTranslateY() + hitboxOffsetY);
        System.out.println("Updated hitbox position: x=" + hitbox.getLayoutX() + ", y=" + hitbox.getLayoutY());
    } else {
        System.out.println("Hitbox is null when updating position!");
    }
}
