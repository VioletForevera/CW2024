package manager;

import Core.ActiveActor;
import Core.ActiveActorDestructible;
import javafx.scene.Node;

import java.util.List;

public class CollisionManager {

    // 处理两个列表中对象的碰撞
    public static void handleCollisions(List<ActiveActorDestructible> actors1, List<ActiveActorDestructible> actors2) {
        for (ActiveActorDestructible actor : actors1) {
            for (ActiveActorDestructible otherActor : actors2) {
                if (actor instanceof ActiveActor && otherActor instanceof ActiveActor) {
                    Node actorHitbox = ((ActiveActor) actor).getHitbox();
                    Node otherActorHitbox = ((ActiveActor) otherActor).getHitbox();
                    if (actorHitbox.getBoundsInParent().intersects(otherActorHitbox.getBoundsInParent())) {
                        resolveCollision(actor, otherActor);
                    }
                }
            }
        }
    }

    // 处理单个碰撞的逻辑
    private static void resolveCollision(ActiveActorDestructible actor, ActiveActorDestructible otherActor) {
        actor.takeDamage();
        otherActor.takeDamage();

        if (actor.isDestroyed()) {
            actor.destroy();
        }

        if (otherActor.isDestroyed()) {
            otherActor.destroy();
        }
    }
}
