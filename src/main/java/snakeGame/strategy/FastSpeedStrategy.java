package snakeGame.strategy;

public class FastSpeedStrategy implements SpeedStrategy {
    @Override
    public int getDelay() {
        return 50;
    }
}
