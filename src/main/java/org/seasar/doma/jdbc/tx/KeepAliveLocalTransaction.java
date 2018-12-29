package org.seasar.doma.jdbc.tx;

import javax.sql.DataSource;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.JdbcLogger;

/**
 * 明示的に破棄されるまでJDBC接続を維持し続けるローカルトランザクションです。
 *
 * <p>ただし、例外が発生した場合、接続は閉じられます。
 *
 * <p>このクラスはスレッドセーフです。
 *
 * <pre>
 * KeepAliveLocalTransaction tx = AppConfig.getKeepAliveLocalTransaction();
 * tx.init();
 * try {
 *     try {
 *         tx.begin();
 *         Employee employee = dao.selectById(1);
 *         employee.setName(&quot;hoge&quot;);
 *         employee.setJobType(JobType.PRESIDENT);
 *         dao.update(employee);
 *         tx.commit();
 *     } finally {
 *         tx.rollback();
 *     }
 *     try {
 *         tx.begin();
 *         Employee employee = dao.selectById(2);
 *         employee.setName(&quot;foo&quot;);
 *         employee.setJobType(JobType.SALESMAN);
 *         dao.update(employee);
 *         tx.commit();
 *     } finally {
 *         tx.rollback();
 *     }
 * } finally {
 *     tx.destroy();
 * }
 * </pre>
 *
 * @author taedium
 * @since 1.21.0
 */
public class KeepAliveLocalTransaction extends LocalTransaction {

  /**
   * インスタンスを構築します。
   *
   * @param dataSource データソース
   * @param localTxContextHolder ローカルトランザクションコンテキストのホルダー
   * @param jdbcLogger JDBCに関するロガー
   */
  protected KeepAliveLocalTransaction(
      DataSource dataSource,
      ThreadLocal<LocalTransactionContext> localTxContextHolder,
      JdbcLogger jdbcLogger) {
    super(dataSource, localTxContextHolder, jdbcLogger);
  }

  /**
   * デフォルトのトランザクション分離レベルを指定してインスタンスを構築します。
   *
   * @param dataSource データソース
   * @param localTxContextHolder ローカルトランザクションコンテキストのホルダー
   * @param jdbcLogger JDBCに関するロガー
   * @param defaultTransactionIsolationLevel デフォルトのトランザクション分離レベル
   */
  protected KeepAliveLocalTransaction(
      DataSource dataSource,
      ThreadLocal<LocalTransactionContext> localTxContextHolder,
      JdbcLogger jdbcLogger,
      TransactionIsolationLevel defaultTransactionIsolationLevel) {
    super(dataSource, localTxContextHolder, jdbcLogger, defaultTransactionIsolationLevel);
  }

  /**
   * トランザクションコンテキストを初期化します。
   *
   * <p>この操作によりJDBCの接続が確立されます。
   *
   * <p>このメソッドを呼び出さずに最初の {@link #begin()} を呼び出した場合、その時点でJDBCの接続が確立されます。
   *
   * @throws JdbcException JDBCの接続に失敗した場合
   */
  public void init() {
    getLocalTransactionContext();
  }

  @Override
  protected LocalTransactionContext getLocalTransactionContext() {
    LocalTransactionContext context = localTxContextHolder.get();
    if (context != null) {
      return context;
    }
    return super.getLocalTransactionContext();
  }

  /**
   * トランザクションコンテキストを破棄します。
   *
   * <p>この操作によりJDBCの接続が閉じられます。
   *
   * <p>このメソッドは、実行時例外をスローしません。
   */
  public void destroy() {
    LocalTransactionContext context = localTxContextHolder.get();
    if (context == null) {
      return;
    }
    release(context, "destroy");
  }

  /** トランザクションを終了しますがJDBCの接続は閉じません。 */
  @Override
  protected void endInternal(LocalTransactionContext context, String callerMethodName) {
    jdbcLogger.logTransactionEnded(className, callerMethodName, context.getId());
  }

  @Override
  protected boolean isActiveInternal(LocalTransactionContext context) {
    return context != null && context.getId() != null;
  }
}
