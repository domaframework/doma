package org.seasar.doma.jdbc;

import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.seasar.doma.DomaNullPointerException;

/**
 * 出力先に {@link java.util.logging.Logger} を使用したJDBCロガーです。
 *
 * <p>
 *
 * @author taedium
 */
public class UtilLoggingJdbcLogger extends AbstractJdbcLogger<Level> {

  /** このインスタンスで使用するロガーです。 */
  protected final Logger logger;

  /**
   * インスタンスを構築します。
   *
   * <p>ログレベルは {@link Level#INFO} になります。
   *
   * <p>ロガーの名前は {@link UtilLoggingJdbcLogger} の完全修飾名になります。
   */
  public UtilLoggingJdbcLogger() {
    this(Level.INFO);
  }

  /**
   * ログレベルを指定してインスタンスを構築します。
   *
   * <p>ロガーの名前は {@link UtilLoggingJdbcLogger} の完全修飾名になります。
   *
   * @param level ログレベル
   */
  public UtilLoggingJdbcLogger(Level level) {
    this(level, Logger.getLogger(UtilLoggingJdbcLogger.class.getName()));
  }

  /**
   * ログレベルとロガーを指定してインスタンスを構築します。
   *
   * @param level ログレベル
   * @param logger ロガー
   */
  public UtilLoggingJdbcLogger(Level level, Logger logger) {
    super(level);
    if (logger == null) {
      throw new DomaNullPointerException("logger");
    }
    this.logger = logger;
  }

  @Override
  protected void log(
      Level level,
      String callerClassName,
      String callerMethodName,
      Throwable throwable,
      Supplier<String> messageSupplier) {
    logger.logp(level, callerClassName, callerMethodName, throwable, messageSupplier);
  }
}
