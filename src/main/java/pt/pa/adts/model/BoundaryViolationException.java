package pt.pa.adts.model;

/**
 * Signals that the boundaries of a data structure have been illegally
 * traversed (e.g. past the end of a list).
 * @author 202100316, 201801045, 202100190, 202100198
 */

public class BoundaryViolationException  extends RuntimeException {

  /**
   * Constructs a new {@code BoundaryViolationException} with the specified error message.
   *
   * @param message The error message explaining the illegal traversal.
   */
  public BoundaryViolationException (String message) {
    super (message);
  }
}
