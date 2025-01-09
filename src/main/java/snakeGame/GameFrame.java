package snakeGame;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final MenuPanel menuPanel;
    private final SettingsPanel settingsPanel;
    private final GamePanel gamePanel;

    public GameFrame() {
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        menuPanel = new MenuPanel(this);
        settingsPanel = new SettingsPanel(this);
        gamePanel = new GamePanel(this);

        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(settingsPanel, "Settings");
        mainPanel.add(gamePanel, "Game");

        this.add(mainPanel);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        showMenu();
    }

    public void showMenu() {
        cardLayout.show(mainPanel, "Menu");
    }

    public void showSettings() {
        cardLayout.show(mainPanel, "Settings");
    }

    public void startGame() {
        cardLayout.show(mainPanel, "Game");
        gamePanel.startGame();
    }

    public void setGameDelay(int delay) {
        gamePanel.setGameDelay(delay);
    }
}
