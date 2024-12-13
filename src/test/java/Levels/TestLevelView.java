package Levels;

import Ui.GameOverImage;
import Ui.HeartDisplay;
import Ui.WinImage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TestLevelView {

    private LevelView levelView;
    private Group mockRoot;
    private ObservableList<Node> mockChildren;

    @BeforeEach
    void setUp() {
        // 模拟 Group 和其子节点列表
        mockRoot = mock(Group.class);
        mockChildren = FXCollections.observableArrayList();
        when(mockRoot.getChildren()).thenReturn(mockChildren);

        // 创建测试的 LevelView 实例
        levelView = new LevelView(mockRoot, 3);
    }

    @Test
    void testShowHeartDisplay() {
        // 调用方法并验证结果
        levelView.showHeartDisplay();

        // 确认 HeartDisplay 的容器被添加到根节点
        assertFalse(mockChildren.isEmpty(), "Root should contain heart display.");
    }

    @Test
    void testShowWinImage() {
        // 调用方法
        levelView.showWinImage();

        // 验证 WinImage 被添加
        assertTrue(mockChildren.stream().anyMatch(node -> node instanceof WinImage),
                "Root should contain WinImage.");
    }

    @Test
    void testShowGameOverImage() {
        // 调用方法
        levelView.showGameOverImage();

        // 验证 GameOverImage 被添加
        assertTrue(mockChildren.stream().anyMatch(node -> node instanceof GameOverImage),
                "Root should contain GameOverImage.");
    }



}
