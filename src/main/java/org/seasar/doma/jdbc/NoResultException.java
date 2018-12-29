package org.seasar.doma.jdbc;

import org.seasar.doma.message.Message;

/**
 * 1件以上存在することを期待する検索系SQLの結果が0件である場合にスローされる例外です。
 *
 * @author taedium
 */
public class NoResultException extends JdbcException {

  private static final long serialVersionUID = 1L;

  /** SQLの種別 */
  protected final SqlKind kind;

  /** 未加工SQL */
  protected final String rawSql;

  /** フォーマット済みSQL */
  protected final String formattedSql;

  /** SQLファイルのパス */
  protected final String sqlFilePath;

  /**
   * 0件の結果を返したSQLを指定してインスタンスを構築します。
   *
   * @param logType ログタイプ
   * @param sql SQL
   */
  public NoResultException(SqlLogType logType, Sql<?> sql) {
    this(logType, sql.getKind(), sql.getRawSql(), sql.getFormattedSql(), sql.getSqlFilePath());
  }

  /**
   * 0件の結果を返した未加工SQLとフォーマット済みSQLを指定してインスタンスを構築します。
   *
   * @param logType ログタイプ
   * @param kind SQLの種別
   * @param rawSql 未加工SQL
   * @param formattedSql フォーマット済みSQL
   * @param sqlFilePath SQLファイルのパス
   */
  public NoResultException(
      SqlLogType logType, SqlKind kind, String rawSql, String formattedSql, String sqlFilePath) {
    super(Message.DOMA2005, sqlFilePath, choiceSql(logType, rawSql, formattedSql));
    this.kind = kind;
    this.rawSql = rawSql;
    this.formattedSql = formattedSql;
    this.sqlFilePath = sqlFilePath;
  }

  /**
   * SQLの種別を返します。
   *
   * @return SQLの種別
   * @since 1.5.0
   */
  public SqlKind getKind() {
    return kind;
  }

  /**
   * 未加工SQLを返します。
   *
   * @return 未加工SQL
   */
  public String getRawSql() {
    return rawSql;
  }

  /**
   * フォーマット済みSQLを返します。
   *
   * @return フォーマット済みSQL
   */
  public String getFormattedSql() {
    return formattedSql;
  }

  /**
   * SQLファイルのパスを返します。
   *
   * @return SQLファイルのパス、SQLが自動生成された場合 {@code null}
   */
  public String getSqlFilePath() {
    return sqlFilePath;
  }
}
