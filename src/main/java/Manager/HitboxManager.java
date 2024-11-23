package manager;

import javafx.scene.Group;
import Core.ActiveActor;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class HitboxManager {

    private final List<ActiveActor> actors = new ArrayList<>();  // 存储所有注册的 actor

    // 注册一个新的 Actor（包含它的 hitbox）
    public void registerActor(ActiveActor actor) {
        if (!actors.contains(actor)) {
            actors.add(actor);
            System.out.println("Actor registered with hitbox.");
        }
    }

    // 移除已注册的 Actor
    public void unregisterActor(ActiveActor actor) {
        actors.remove(actor);
        System.out.println("Actor unregistered from hitbox manager.");
    }

    // 更新所有 Actor 的 hitbox
    public void updateHitboxes() {
        for (ActiveActor actor : actors) {
            actor.updateActorHitbox();  // 使用公共方法更新 hitbox
        }
    }

    // 检查两个 Actor 是否发生碰撞
    public boolean checkCollision(ActiveActor actor1, ActiveActor actor2) {
        // 检查两个 hitbox 是否相交，返回布尔值
        return actor1.getHitbox().getBoundsInParent().intersects(actor2.getHitbox().getBoundsInParent());
    }

    // 可视化所有 Actor 的 hitbox（仅用于调试）
    public void visualizeAllHitboxes(Group root) {
        for (ActiveActor actor : actors) {
            actor.visualizeHitbox(root);  // 将每个 actor 的 hitbox 添加到场景中
        }
    }

    // 创建并返回一个新的 hitbox
    public Rectangle createHitbox() {
        Rectangle hitbox = new Rectangle();
        hitbox.setWidth(50);  // 默认宽度
        hitbox.setHeight(50); // 默认高度
        hitbox.setFill(javafx.scene.paint.Color.TRANSPARENT);  // 设置透明
        hitbox.setStroke(javafx.scene.paint.Color.RED);        // 设置红色边框，便于调试
        return hitbox;
    }
}
