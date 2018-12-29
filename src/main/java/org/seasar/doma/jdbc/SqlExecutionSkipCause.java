package org.seasar.doma.jdbc;

/**
 * SQLの実行がスキップされる原因の列挙です。
 *
 * @author taedium
 */
public enum SqlExecutionSkipCause {

  /** 更新対象のエンティティのステートが変更されていないことを示します。 */
  STATE_UNCHANGED,

  /** バッチ処理対象のエンティティが1件も存在しないことを示します。 */
  BATCH_TARGET_NONEXISTENT
}
