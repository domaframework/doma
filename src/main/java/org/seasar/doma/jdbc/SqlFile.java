package org.seasar.doma.jdbc;

import org.seasar.doma.DomaNullPointerException;

/**
 * SQLファイルです。
 *
 * <p>SQLファイルのパス、SQLの文字列、SQLの文字列を解析した結果をカプセル化します。
 *
 * @author taedium
 */
public class SqlFile {

  /** SQLファイルのパス */
  protected final String path;

  /** SQLの文字列 */
  protected final String sql;

  /** SQLの解析結果 */
  protected final SqlNode sqlNode;

  /**
   * @param path SQLファイルのパス
   * @param sql SQLの文字列
   * @param sqlNode SQLの解析結果
   */
  public SqlFile(String path, String sql, SqlNode sqlNode) {
    if (path == null) {
      throw new DomaNullPointerException("path");
    }
    if (sql == null) {
      throw new DomaNullPointerException("sql");
    }
    if (sqlNode == null) {
      throw new DomaNullPointerException("sqlNode");
    }
    this.path = path;
    this.sql = sql;
    this.sqlNode = sqlNode;
  }

  /**
   * SQLファイルのパスを返します。
   *
   * @return SQLファイルのパス
   */
  public String getPath() {
    return path;
  }

  /**
   * SQLの文字列を返します。
   *
   * @return SQLの文字列
   */
  public String getSql() {
    return sql;
  }

  /**
   * SQLの解析結果を返します。
   *
   * <p>呼び出し側でSQLの解析結果を変更してはいけません。
   *
   * @return SQLの解析結果
   */
  public SqlNode getSqlNode() {
    return sqlNode;
  }

  @Override
  public String toString() {
    return sqlNode.toString();
  }
}
