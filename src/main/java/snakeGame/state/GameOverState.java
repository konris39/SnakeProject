package snakeGame.state;

import snakeGame.GamePanel;

import java.awt.Graphics;

public class GameOverState implements GameState {
    private final GamePanel gamePanel;

    public GameOverState(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics g) {
        gamePanel.drawGameOver(g);
    }
}
