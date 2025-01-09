package snakeGame.observer;

import java.util.Random;

public class Apple {
    private int x;
    private int y;
    private final int screenWidth;
    private final int screenHeight;
    private final int unitSize;
    private final Random random;

    private final AppleSubject appleSubject;

    public Apple(int screenWidth, int screenHeight, int unitSize) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.unitSize = unitSize;
        this.random = new Random();
        this.appleSubject = new AppleSubject();
        newApplePosition();
    }

    public void newApplePosition() {
        x = random.nextInt(screenWidth / unitSize) * unitSize;
        y = random.nextInt(screenHeight / unitSize) * unitSize;
    }

    public void appleEaten() {
        appleSubject.notifyAppleEaten();
    }

    public AppleSubject getAppleSubject() {
        return appleSubject;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
