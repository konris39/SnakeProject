package snakeGame.strategy;

public class SlowSpeedStrategy implements SpeedStrategy {
    @Override
    public int getDelay() {
        return 90;
    }
}
