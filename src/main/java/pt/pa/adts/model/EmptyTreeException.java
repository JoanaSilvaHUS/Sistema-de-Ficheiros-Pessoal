package pt.pa.adts.model;

/**
 * Runtime exception thrown when attempting to access the root of an empty tree.
 *
 * This exception is thrown when an operation expects the tree to have a root,
 * but the tree is empty. It extends the {@code RuntimeException} class.
 *
 * @author 202100316, 201801045, 202100190, 202100198
 */
public class EmptyTreeException extends RuntimeException {

  /**
   * Constructs a new {@code EmptyTreeException} with the specified error message.
   *
   * @param err The error message associated with the exception.
   */
  public EmptyTreeException(String err) {
    super(err);
  }
}
