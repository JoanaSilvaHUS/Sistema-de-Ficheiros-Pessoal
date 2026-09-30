package pt.pa.adts.model;

/**
 * Signals that an invalid position is accessed in a data structure.
 * This exception is thrown when attempting to access or manipulate
 * a position that is not valid for a particular data structure.
 *
 * @author 202100316, 201801045, 202100190, 202100198
 */
public class InvalidPositionException extends RuntimeException {

    /**
     * Constructs an InvalidPositionException with the specified detail message.
     *
     * @param s The detail message.
     */
    public InvalidPositionException(String s) {
        super(s);
    }

    /**
     * Constructs a new InvalidPositionException with no specified detail message.
     */
    public InvalidPositionException() {
    }
}

