package snakeGame.strategy;

public class MediumSpeedStrategy implements SpeedStrategy {
    @Override
    public int getDelay() {
        return 70;
    }
}
