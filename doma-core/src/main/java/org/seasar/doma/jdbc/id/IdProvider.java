package org.seasar.doma.jdbc.id;

/** An identity provider. */
public interface IdProvider {

  /**
   * Whether this provider can return the identity value.
   *
   * @return {@code true} if this provider can return the identity value
   */
  boolean isAvailable();

  /**
   * Provides the identity value.
   *
   * @return the identity value
   * @throws IllegalStateException if an illegal state is detected
   * @throws UnsupportedOperationException if this method is not supported
   */
  long get();
}
