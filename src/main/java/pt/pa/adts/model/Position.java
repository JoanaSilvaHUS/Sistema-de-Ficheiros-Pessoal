package pt.pa.adts.model;

import java.io.Serializable;

/**
 * An interface for a position, which is a holder object storing a single element.
 *
 * @author 202100316, 201801045, 202100190, 202100198
 */
public interface Position<E> extends Serializable {

  /**
   * Return the element stored at this position.
   *
   * @return the element stored at this position.
   */
  E element();
}
