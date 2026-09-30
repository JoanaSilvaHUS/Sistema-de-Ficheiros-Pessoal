package pt.pa.adts.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing an object that can be observed by other objects (observers).
 * Implements the Observable interface.
 *
 * @author 202100316, 201801045, 202100190, 202100198
 */
public class Subject implements Observable{

    private List<Observer> observers;

    /**
     * Constructs a new Subject object with an empty list of observers.
     */
    public Subject(){this.observers = new ArrayList<>();}

    /**
     * Adds one or more observers to the list of observers. If an observer is already in the list, it won't be added again.
     *
     * @param observers The observers to be added.
     */
    @Override
    public void addObservers(Observer... observers) {
        for (Observer obs : observers)
            if(!this.observers.contains(obs))
                this.observers.add(obs);
    }

    /**
     * Adds an observer to the list of observers. If the observer is already in the list, it won't be added again.
     *
     * @param observer The observer to be added.
     */
    @Override
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    /**
     * Removes an observer from the list of observers.
     *
     * @param observer The observer to be removed.
     */
    @Override
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    /**
     * Notifies all registered observers about a change in the observed object.
     *
     * @param object The object representing the change.
     */
    @Override
    public void notifyObservers(Object object) {
        for (Observer obs : this.observers)
            obs.update(this,object);
    }
}
