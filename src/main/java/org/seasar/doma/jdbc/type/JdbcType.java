package org.seasar.doma.jdbc.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.SqlLogFormatter;

/**
 * {@literal JDBC} の型を表現します。 型ごとに異なる処理を抽象化します。
 *
 * <p>このインタフェースの実装はスレッドセーフでなければいけません。
 *
 * <p>
 *
 * @author taedium
 * @param <T> JDBCで扱う型
 */
public interface JdbcType<T> extends SqlLogFormatter<T> {

  /**
   * {@link ResultSet} から値を取得します。
   *
   * @param resultSet 結果セット
   * @param index インデックス
   * @return 値
   * @throws DomaNullPointerException {@code resultSet} が {@code null} の場合
   * @throws DomaIllegalArgumentException {@code index} が {@literal 1} 以下の場合
   * @throws SQLException SQL例外
   */
  T getValue(ResultSet resultSet, int index) throws DomaNullPointerException, SQLException;

  /**
   * {@link PreparedStatement} に値を設定します。
   *
   * @param preparedStatement 文
   * @param index インデックス
   * @param value 値
   * @throws DomaNullPointerException {@code preparedStatement} が {@code null} の場合
   * @throws DomaIllegalArgumentException {@code index} が {@literal 1} 以下の場合
   * @throws SQLException SQL例外
   */
  void setValue(PreparedStatement preparedStatement, int index, T value) throws SQLException;

  /**
   * {@link CallableStatement} にOUTパラメータを登録します。
   *
   * @param callableStatement 文
   * @param index インデックス
   * @throws DomaNullPointerException {@code callableStatement} が {@code null} の場合
   * @throws DomaIllegalArgumentException {@code index} が {@literal 1} 以下の場合
   * @throws SQLException SQL例外
   */
  void registerOutParameter(CallableStatement callableStatement, int index) throws SQLException;

  /**
   * {@link CallableStatement} から値を取得します。
   *
   * @param callableStatement 文
   * @param index インデックス
   * @return 値
   * @throws DomaNullPointerException {@code callableStatement} が {@code null} の場合
   * @throws DomaIllegalArgumentException {@code index} が {@literal 1} 以下の場合
   * @throws SQLException SQL例外
   */
  T getValue(CallableStatement callableStatement, int index) throws SQLException;
}
