package org.seasar.doma.jdbc;

/**
 * The formatter that converts the value to the SQL log format.
 *
 * @param <T> the value type
 */
public interface SqlLogFormatter<T> {

  /**
   * Converts the value to the SQL log format.
   *
   * @param value the value
   * @return the SQL log format
   */
  String convertToLogFormat(T value);
}
