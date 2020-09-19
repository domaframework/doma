package org.seasar.doma.jdbc;

import org.seasar.doma.DomaNullPointerException;

/**
 * A controller for the transaction whose attribute may be {@literal REQUIRES_NEW}.
 *
 * <p>This object may increment identity values efficiently in a {@literal REQUIRES_NEW}
 * transaction.
 *
 * <p>The implementation class must be thread safe.
 */
public interface RequiresNewController {

  /**
   * Executes a {@literal REQUIRES_NEW} transaction.
   *
   * @param <R> the result type
   * @param callback the callback that is executed in a {@literal REQUIRES_NEW} transaction
   * @return the result
   * @throws DomaNullPointerException if ｛@code callback} is {@code null}
   * @throws Throwable thrown, if any exception is thrown in the｛@code callback} execution
   */
  @SuppressWarnings("RedundantThrows")
  default <R> R requiresNew(Callback<R> callback) throws Throwable {
    if (callback == null) {
      throw new DomaNullPointerException("callback");
    }
    return callback.execute();
  }

  /**
   * A callback that is executed in a {@literal REQUIRES_NEW} transaction.
   *
   * @param <R> the result type
   */
  interface Callback<R> {

    /**
     * Executes this callback.
     *
     * @return the result
     */
    R execute();
  }
}
