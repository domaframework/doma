package org.seasar.doma.jdbc.tx;

import java.sql.Connection;

/**
 * トランザクション分離レベルを示します。
 *
 * @author taedium
 * @since 1.1.0
 */
public enum TransactionIsolationLevel {

  /** @see Connection#TRANSACTION_READ_UNCOMMITTED　 */
  READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),

  /** @see Connection#TRANSACTION_READ_COMMITTED */
  READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),

  /** @see Connection#TRANSACTION_REPEATABLE_READ */
  REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),

  /** @see Connection#TRANSACTION_SERIALIZABLE */
  SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE),

  DEFAULT(-1);

  private int level;

  private TransactionIsolationLevel(int level) {
    this.level = level;
  }

  /**
   * トランザクション分離レベルをあらわす定数を返します。
   *
   * @return トランザクション分離レベルをあらわす定数
   * @see Connection#TRANSACTION_READ_UNCOMMITTED
   * @see Connection#TRANSACTION_READ_COMMITTED
   * @see Connection#TRANSACTION_REPEATABLE_READ
   * @see Connection#TRANSACTION_SERIALIZABLE
   */
  public int getLevel() {
    return level;
  }
}
