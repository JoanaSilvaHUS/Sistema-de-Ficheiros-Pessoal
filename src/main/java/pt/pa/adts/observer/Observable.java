package pt.pa.adts.observer;

/**
 * An interface for objects that can be observed by observers.
 * Implements the Observer design pattern.
 *
 * @author 202100316, 201801045, 202100190, 202100198
 */
public interface Observable {
    /**
     * Add Observers
     * @param observers
     */
    public void addObservers(Observer... observers);

    /**
     * Add Observer
     * @param observer
     */
    public void addObserver(Observer observer);

    /**
     * Remove Observer
     * @param observer
     */
    public void removeObserver(Observer observer);

    /**
     * Notify Observers
     * @param obj
     */
    public void notifyObservers(Object obj);
}
