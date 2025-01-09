package snakeGame.observer;

import java.util.ArrayList;
import java.util.List;

public class AppleSubject {
    private final List<AppleObserver> observers = new ArrayList<>();

    public void attach(AppleObserver observer) {
        observers.add(observer);
    }

    public void detach(AppleObserver observer) {
        observers.remove(observer);
    }

    public void notifyAppleEaten() {
        for (AppleObserver observer : observers) {
            observer.onAppleEaten();
        }
    }
}
