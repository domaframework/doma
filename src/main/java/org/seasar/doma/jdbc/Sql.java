package org.seasar.doma.jdbc;

import java.util.List;

/**
 * SQLを表します。
 *
 * <p>SQLとSQL実行時のパラメータをカプセル化します。また、SQLのバインド変数をパラメータで置換した文字列やSQLファイルのパスを保持します。
 *
 * <p>このインタフェースの実装はスレッドセーフであることを要求されません。
 *
 * @author taedium
 * @param <P> パラメータの種別を表す型
 */
public interface Sql<P extends SqlParameter> {

  /**
   * SQLの種別を返します。
   *
   * @return SQLの種別
   */
  SqlKind getKind();

  /**
   * 未加工SQLを返します。
   *
   * <p>バインド変数は {@code ?} で表されます。
   *
   * @return 未加工SQL
   */
  String getRawSql();

  /**
   * フォーマット済みSQLを返します。
   *
   * <p>バインド変数 {@code ?} が、 {@link SqlLogFormattingVisitor} の実装によって適切な文字列に置換されたSQLです。
   *
   * @return フォーマット済みSQL
   */
  String getFormattedSql();

  /**
   * 未加工SQLが記述されているSQLファイルのパスを返します。
   *
   * @return SQLファイルのパス、SQLが自動生成された場合は {@code null}
   */
  String getSqlFilePath();

  /**
   * バインド変数へのパラメータのリストを返します。
   *
   * @return バインド変数のパラメータのリスト
   */
  List<P> getParameters();

  /**
   * SQLのログの表示形式を返します。
   *
   * @return SQLのログの表示形式
   */
  SqlLogType getSqlLogType();
}
