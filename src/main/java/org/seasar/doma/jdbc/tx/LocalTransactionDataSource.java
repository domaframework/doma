package org.seasar.doma.jdbc.tx;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.SimpleDataSource;
import org.seasar.doma.message.Message;

/**
 * ローカルトランザクションと連動するデータソースです。
 *
 * <p>このクラスはスレッドセーフです。
 *
 * @see LocalTransaction
 * @author taedium
 * @since 1.1.0
 */
public final class LocalTransactionDataSource implements DataSource {

  /** コネクションのホルダー */
  private final ThreadLocal<LocalTransactionContext> localTxContextHolder =
      new ThreadLocal<LocalTransactionContext>();

  /** データソース */
  private final DataSource dataSource;

  /**
   * インスタンスを構築します。
   *
   * @param dataSource データソース
   * @throws DomaNullPointerException {@code dataSource} が {@code null} の場合
   */
  public LocalTransactionDataSource(DataSource dataSource) {
    if (dataSource == null) {
      throw new DomaNullPointerException("dataSource");
    }
    this.dataSource = dataSource;
  }

  /**
   * インスタンスを構築します。
   *
   * @param url JDBCのURL
   * @param user JDBCのユーザー
   * @param password JDBCのパスワード
   * @throws DomaNullPointerException {@code url} が {@code null} の場合
   */
  public LocalTransactionDataSource(String url, String user, String password) {
    if (url == null) {
      throw new DomaNullPointerException("url");
    }
    SimpleDataSource simpleDataSource = new SimpleDataSource();
    simpleDataSource.setUrl(url);
    if (user != null) {
      simpleDataSource.setUser(user);
    }
    if (password != null) {
      simpleDataSource.setPassword(password);
    }
    this.dataSource = simpleDataSource;
  }

  /**
   * {@inheritDoc}
   *
   * <p>このメソッドを実行する前にローカルトランザクションを開始しておかなければいけません。
   *
   * @see LocalTransaction
   * @throws TransactionNotYetBegunException ローカルトランザクションがまだ開始されていない場合
   */
  @Override
  public Connection getConnection() throws SQLException {
    return getConnectionInternal();
  }

  /**
   * {@inheritDoc}
   *
   * <p>このメソッドを実行する前にローカルトランザクションを開始しておかなければいけません。
   *
   * @see LocalTransaction
   * @throws TransactionNotYetBegunException ローカルトランザクションがまだ開始されていない場合
   */
  @Override
  public Connection getConnection(String username, String password) throws SQLException {
    return getConnectionInternal();
  }

  private Connection getConnectionInternal() {
    LocalTransactionContext context = localTxContextHolder.get();
    if (context == null) {
      throw new TransactionNotYetBegunException(Message.DOMA2048);
    }
    return context.getConnection();
  }

  @Override
  public int getLoginTimeout() throws SQLException {
    return dataSource.getLoginTimeout();
  }

  @Override
  public PrintWriter getLogWriter() throws SQLException {
    return dataSource.getLogWriter();
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    if (iface == null) {
      return false;
    }
    if (iface.isAssignableFrom(getClass())) {
      return true;
    }
    return dataSource.isWrapperFor(iface);
  }

  @Override
  public void setLoginTimeout(int seconds) throws SQLException {
    dataSource.setLoginTimeout(seconds);
  }

  @Override
  public void setLogWriter(PrintWriter out) throws SQLException {
    dataSource.setLogWriter(out);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    if (iface == null) {
      throw new SQLException("iface must not be null");
    }
    if (iface.isAssignableFrom(getClass())) {
      return (T) this;
    }
    return dataSource.unwrap(iface);
  }

  /**
   * ローカルトランザクションを返します。
   *
   * @param jdbcLogger JDBCに関するロガー
   * @return ローカルトランザクション
   * @throws DomaNullPointerException 引数が {@code null} の場合
   */
  public LocalTransaction getLocalTransaction(JdbcLogger jdbcLogger) {
    if (jdbcLogger == null) {
      throw new DomaNullPointerException("jdbcLogger");
    }
    return new LocalTransaction(dataSource, localTxContextHolder, jdbcLogger);
  }

  /**
   * デフォルトのトランザクション分離レベルを指定してローカルトランザクションを返します。
   *
   * @param jdbcLogger JDBCに関するロガー
   * @param transactionIsolationLevel デフォルトのトランザクション分離レベル
   * @return ローカルトランザクション
   * @throws DomaNullPointerException 引数のいずれかが {@code null} の場合
   */
  public LocalTransaction getLocalTransaction(
      JdbcLogger jdbcLogger, TransactionIsolationLevel transactionIsolationLevel) {
    if (jdbcLogger == null) {
      throw new DomaNullPointerException("jdbcLogger");
    }
    if (transactionIsolationLevel == null) {
      throw new DomaNullPointerException("transactionIsolationLevel");
    }
    return new LocalTransaction(
        dataSource, localTxContextHolder, jdbcLogger, transactionIsolationLevel);
  }

  /**
   * 明示的に破棄されるまで接続を維持し続けるローカルトランザクションを返します。
   *
   * @param jdbcLogger JDBCに関するロガー
   * @return ローカルトランザクション
   * @throws DomaNullPointerException 引数が {@code null} の場合
   */
  public KeepAliveLocalTransaction getKeepAliveLocalTransaction(JdbcLogger jdbcLogger) {
    if (jdbcLogger == null) {
      throw new DomaNullPointerException("jdbcLogger");
    }
    return new KeepAliveLocalTransaction(dataSource, localTxContextHolder, jdbcLogger);
  }

  /**
   * デフォルトのトランザクション分離レベルを指定して、明示的に破棄されるまで接続を維持し続けるローカルトランザクションを返します。
   *
   * @param jdbcLogger JDBCに関するロガー
   * @param transactionIsolationLevel デフォルトのトランザクション分離レベル
   * @return ローカルトランザクション
   * @throws DomaNullPointerException 引数のいずれかが {@code null} の場合
   */
  public KeepAliveLocalTransaction getKeepAliveLocalTransaction(
      JdbcLogger jdbcLogger, TransactionIsolationLevel transactionIsolationLevel) {
    if (jdbcLogger == null) {
      throw new DomaNullPointerException("jdbcLogger");
    }
    if (transactionIsolationLevel == null) {
      throw new DomaNullPointerException("transactionIsolationLevel");
    }
    return new KeepAliveLocalTransaction(
        dataSource, localTxContextHolder, jdbcLogger, transactionIsolationLevel);
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    return dataSource.getParentLogger();
  }
}
