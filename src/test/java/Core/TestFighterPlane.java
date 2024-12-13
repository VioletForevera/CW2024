package Core;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestFighterPlane {

    private TestFighterPlaneImpl fighterPlane;

    // Test subclass of FighterPlane
    static class TestFighterPlaneImpl extends FighterPlane {
        public TestFighterPlaneImpl(int health) {
            // 不传递图片路径，避免加载资源的相关问题
            super("", 100, 0, 0, health);
        }

        @Override
        public ActiveActorDestructible fireProjectile() {
            return new ActiveActorDestructible("", 10, getLayoutX(), getLayoutY()) {
                @Override
                public void updatePosition() {
                    // Mock implementation
                }

                @Override
                public void updateActor() {
                    // Mock implementation
                }

                @Override
                public void takeDamage() {
                    destroy(); // Mock behavior
                }
            };
        }

        @Override
        public void updatePosition() {
            // Mock update logic
        }
    }

    @BeforeEach
    void setUp() {
        fighterPlane = new TestFighterPlaneImpl(5);
    }

    @Test
    void testTakeDamage() {
        assertEquals(5, fighterPlane.getHealth(), "Initial health should be 5.");
        fighterPlane.takeDamage();
        assertEquals(4, fighterPlane.getHealth(), "Health should decrease by 1 after taking damage.");
        fighterPlane.setHealth(1);
        fighterPlane.takeDamage();
        assertTrue(fighterPlane.isDestroyed(), "FighterPlane should be destroyed when health reaches 0.");
    }

    @Test
    void testSetHealth() {
        fighterPlane.setHealth(10);
        assertEquals(10, fighterPlane.getHealth(), "Health should be updated to 10.");
    }

    @Test
    void testUpdateActor() {
        assertDoesNotThrow(() -> fighterPlane.updateActor(), "updateActor should not throw any exceptions.");
    }
}
