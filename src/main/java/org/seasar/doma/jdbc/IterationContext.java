package org.seasar.doma.jdbc;

/**
 * {@link IterationCallback}の実行コンテキストです。
 *
 * @author taedium
 */
public class IterationContext {

  /** {@link #exit()} が呼びだされたかどうかを示します。 */
  protected boolean exited;

  /**
   * {@link #exit()} が呼びだされたかどうかを返します。
   *
   * @return {@link #exit()} が呼ばれていたら {@code true}
   */
  public boolean isExited() {
    return exited;
  }

  /** {@link IterationCallback}の処理から抜け出します。 */
  public void exit() {
    this.exited = true;
  }
}
