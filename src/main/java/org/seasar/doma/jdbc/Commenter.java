package org.seasar.doma.jdbc;

/**
 * SQLのコメンターです。
 *
 * <p>この処理はSQLテンプレートの処理が完了した後で行われます。
 *
 * <p>このインタフェースでは、バインド変数を追加するなど、コメントを追加する以外のことを実行してはいけません。
 *
 * @author nakamura-to
 * @since 2.1.0
 */
public interface Commenter {

  /**
   * SQLにコメントを追記します。
   *
   * <p>デフォルトでは何も行いません。
   *
   * @param sql SQL
   * @param context コンテキスト
   * @return コメントが追記されたSQL
   */
  default String comment(String sql, CommentContext context) {
    return sql;
  }
}
