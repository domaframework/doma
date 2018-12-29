package org.seasar.doma.jdbc;

/**
 * 悲観的排他制御の種別です。
 *
 * @author taedium
 */
public enum SelectForUpdateType {

  /** 通常の方法を用いることを示します。 */
  NORMAL,

  /** ロックを取得するまで待機しないことを示します。 */
  NOWAIT,

  /** ロックを取得するまで待機することを示します */
  WAIT
}
