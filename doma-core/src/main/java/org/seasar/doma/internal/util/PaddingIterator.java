package org.seasar.doma.internal.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * An iterator that extends an existing {@link Iterator} with padding behavior.
 *
 * <p>The {@code PaddingIterator} guarantees that additional elements will be yielded even after the
 * original iterator has been exhausted. These additional elements will repeat the last element
 * produced by the original iterator until the specified padding size is reached.
 *
 * @param <E> the type of elements returned by this iterator
 */
public class PaddingIterator<E> implements Iterator<E> {

  private final Iterator<E> iterator;
  private E lastElement;
  private int paddingSize;

  /**
   * Constructs a {@code PaddingIterator} that decorates the specified {@link Iterator} with
   * additional padding behavior.
   *
   * @param iterator the original iterator to be decorated; must not be {@code null}
   * @param paddingSize the number of additional elements to produce after the original iterator is
   *     exhausted; must be {@code >= 0}
   * @throws NullPointerException if {@code iterator} is {@code null}
   * @throws IllegalArgumentException if {@code paddingSize} is less than 0
   */
  public PaddingIterator(Iterator<E> iterator, int paddingSize) {
    Objects.requireNonNull(iterator);
    if (paddingSize < 0) {
      throw new IllegalArgumentException("paddingSize must be greater than or equal to 0");
    }
    this.iterator = iterator;
    this.lastElement = null;
    this.paddingSize = paddingSize;
  }

  @Override
  public boolean hasNext() {
    return iterator.hasNext() || paddingSize > 0;
  }

  @Override
  public E next() {
    if (iterator.hasNext()) {
      E element = iterator.next();
      lastElement = element;
      return element;
    } else if (paddingSize > 0) {
      paddingSize--;
      return lastElement;
    }
    throw new NoSuchElementException();
  }
}
