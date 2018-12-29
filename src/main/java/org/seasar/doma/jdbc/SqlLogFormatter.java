package org.seasar.doma.jdbc;

/**
 * 値をSQLのログ用文字列に変換するフォーマッタです。
 *
 * @author taedium
 * @param <T> 値
 */
public interface SqlLogFormatter<T> {

  /**
   * 値をSQLのログ用文字列に変換します。
   *
   * @param value 値
   * @return ログ用文字列
   */
  String convertToLogFormat(T value);
}
