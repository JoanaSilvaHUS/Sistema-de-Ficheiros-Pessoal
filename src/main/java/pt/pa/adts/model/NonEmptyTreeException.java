package pt.pa.adts.model;

/**
 * Signals that an operation requiring an empty tree is performed on a non-empty tree.
 * This exception is thrown when attempting to perform an operation that requires
 * an empty tree, but the tree is already populated with elements.
 *
 * @author 202100316, 201801045, 202100190, 202100198
 */
public class NonEmptyTreeException extends RuntimeException {

  /**
   * Constructs a NonEmptyTreeException with the specified detail message.
   *
   * @param err The detail message.
   */
  public NonEmptyTreeException(String err) {
    super(err);
  }
}
