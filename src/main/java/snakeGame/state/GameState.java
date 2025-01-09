package snakeGame.state;

import java.awt.Graphics;

public interface GameState {
    void update();
    void draw(Graphics g);
}
