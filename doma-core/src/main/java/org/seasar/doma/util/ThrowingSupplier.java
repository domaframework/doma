package org.seasar.doma.util;

/**
 * Represents a supplier of results that may throw an exception.
 *
 * @param <T> the type of results supplied by this supplier
 * @param <E> the type of exception thrown by this supplier
 */
@FunctionalInterface
public interface ThrowingSupplier<T, E extends Exception> {
  T get() throws E;
}
