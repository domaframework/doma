package org.seasar.doma.jdbc;

import org.seasar.doma.wrapper.Wrapper;

/**
 * A function that converts the {@link Wrapper} values to the SQL log formats.
 *
 * <p>The implementation class is not required to be thread safe.
 */
public interface SqlLogFormattingFunction {

  /**
   * Apply this function.
   *
   * @param <V> the basic type
   * @param wrapper the wrapper value
   * @param formatter the formatter
   * @return the converted string
   */
  <V> String apply(Wrapper<V> wrapper, SqlLogFormatter<V> formatter);
}
