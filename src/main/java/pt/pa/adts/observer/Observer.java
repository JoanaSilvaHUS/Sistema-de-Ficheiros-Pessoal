package pt.pa.adts.observer;

/**
 * An interface for objects that want to be notified about changes in the state
 * of objects implementing the Observable interface.
 *
 * @author 202100316, 201801045, 202100190, 202100198
 */
public interface Observer {

    /**
     * This method is called when the observed object is changed.
     *
     * @param subject the Observable object that changed.
     * @param obj     the additional information passed by the Observable object.
     */
    void update(Observable subject,Object obj);
}
