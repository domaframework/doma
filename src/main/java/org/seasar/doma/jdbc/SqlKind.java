package org.seasar.doma.jdbc;

/**
 * SQLの種別を示します。
 *
 * @author taedium
 */
public enum SqlKind {

  /** 検索 */
  SELECT,

  /** 挿入 */
  INSERT,

  /** 更新 */
  UPDATE,

  /** 削除 */
  DELETE,

  /** バッチ挿入 */
  BATCH_INSERT,

  /** バッチ更新 */
  BATCH_UPDATE,

  /** バッチ削除 */
  BATCH_DELETE,

  /** ストアドプロシージャー */
  PROCEDURE,

  /** ストアドファンクション */
  FUNCTION,

  /**
   * スクリプト
   *
   * @since 1.7.0
   */
  SCRIPT,

  SQL_PROCESSOR
}
