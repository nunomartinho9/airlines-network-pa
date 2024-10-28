package observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents the observer pattern
 */
public class Subject implements Observable{
    private List<Observer> observers;

    /**
     * Creates new observers
     */
    public Subject() {
        observers = new ArrayList<>();
    }

    /**
     * Adds a observer
     * @param observer
     */
    @Override
    public void addObserver(Observer observer) {
        if(!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Alerts all the observers
     * @param arg
     */
    @Override
    public void notifyObservers(Object arg) {
        for(Observer o : observers) {
            o.update(this, arg);
        }
    }

    @Override
    public String toString() {
        return "Subject{" +
                "observers=" + observers +
                '}';
    }
}
