package Ui;

import com.example.demo.controller.Main;
import sounds.MusicPlayer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainMenu extends JFrame {

    private MusicPlayer musicPlayer;
    private int volumeLevel = 50; // 初始音量

    public MainMenu() {
        // 使用类路径加载背景音乐
        musicPlayer = new MusicPlayer("/com/example/demo/images/sound.wav");
        musicPlayer.play();
        musicPlayer.setVolume(volumeLevel / 100.0f);

        // 设置窗口标题和基本配置
        setTitle("Game Menu");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 设置背景图片
        setContentPane(new BackgroundPanel());

        // 创建按钮
        JButton startButton = createStyledButton("Start Game");
        JButton settingsButton = createStyledButton("Settings");
        JButton exitButton = createStyledButton("Exit");

        // 添加按钮事件监听器
        startButton.addActionListener(e -> startGame());
        settingsButton.addActionListener(e -> openSettings());
        exitButton.addActionListener(e -> exitGame());

        // 设置按钮布局
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(startButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(settingsButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(exitButton);
        buttonPanel.add(Box.createVerticalGlue());

        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setForeground(Color.WHITE);
        return button;
    }

    private void startGame() {
        System.out.println("Game Started!");
        this.dispose();
        musicPlayer.stop(); // 开始游戏时停止背景音乐

        new Thread(() -> {
            try {
                Main.main(new String[]{});
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    private void openSettings() {
        JDialog settingsDialog = new JDialog(this, "Settings", true);
        settingsDialog.setSize(400, 300);
        settingsDialog.setLocationRelativeTo(this);

        JPanel dialogPanel = new JPanel();
        dialogPanel.setBackground(new Color(50, 50, 50));
        dialogPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        dialogPanel.setLayout(new GridLayout(2, 1, 10, 10));

        JLabel volumeLabel = new JLabel("Volume");
        volumeLabel.setForeground(Color.WHITE);
        volumeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        volumeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JSlider volumeSlider = new JSlider(0, 100, volumeLevel);
        volumeSlider.setMajorTickSpacing(20);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);
        volumeSlider.setBackground(new Color(50, 50, 50));
        volumeSlider.setForeground(Color.WHITE);

        volumeSlider.addChangeListener(e -> {
            volumeLevel = volumeSlider.getValue();
            musicPlayer.setVolume(volumeLevel / 100.0f);
        });

        dialogPanel.add(volumeLabel);
        dialogPanel.add(volumeSlider);

        settingsDialog.setContentPane(dialogPanel);
        settingsDialog.setVisible(true);
    }

    private void exitGame() {
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainMenu menu = new MainMenu();
            menu.setVisible(true);
        });
    }

    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            backgroundImage = new ImageIcon(getClass().getResource("/com/example/demo/images/mainMenuBackground.png")).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
