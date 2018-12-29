package org.seasar.doma.jdbc;

/**
 * スクリプト内のSQLブロックのコンテキストです。
 *
 * <p>スクリプトの解析中に、SQLがステートメントではなくブロック（ステートメントの集合）として扱われているかどうかを判断するために使用されます。
 *
 * <p>
 *
 * @author taedium
 * @since 1.7.0
 */
public interface ScriptBlockContext {

  /**
   * SQLのキーワードを追加します。
   *
   * @param keyword SQLのキーワード
   */
  void addKeyword(String keyword);

  /**
   * ブロックを処理しているかどうかを返します。
   *
   * @return ブロックを処理している場合 {@code true}
   */
  boolean isInBlock();
}
