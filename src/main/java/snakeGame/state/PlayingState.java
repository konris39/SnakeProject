package snakeGame.state;

import snakeGame.GamePanel;

import java.awt.*;

public class PlayingState implements GameState {
    private final GamePanel gamePanel;

    public PlayingState(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void update() {
        gamePanel.move();
        gamePanel.checkApple();
        gamePanel.checkCollisions();
    }

    @Override
    public void draw(Graphics g) {
        gamePanel.drawGame(g);
    }
}
