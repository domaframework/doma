package org.seasar.doma.jdbc;

import java.lang.reflect.Method;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.experimental.Sql;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.internal.jdbc.util.SqlFileUtil;
import org.seasar.doma.internal.util.ResourceUtil;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.message.Message;

/** A skeletal implementation of the {@link SqlFileRepository} interface. */
public abstract class AbstractSqlFileRepository implements SqlFileRepository {

  @Override
  public final SqlFile getSqlFile(Method method, String path, Dialect dialect) {
    if (method == null) {
      throw new DomaNullPointerException("method");
    }
    if (path == null) {
      throw new DomaNullPointerException("path");
    }
    if (!method.isAnnotationPresent(Sql.class)) {
      if (!path.startsWith(Constants.SQL_PATH_PREFIX)) {
        throw new DomaIllegalArgumentException(
            "path", "The path does not start with '" + Constants.SQL_PATH_PREFIX + "'");
      }
      if (!path.endsWith(Constants.SQL_PATH_SUFFIX)) {
        throw new DomaIllegalArgumentException(
            "path", "The path does not end with '" + Constants.SQL_PATH_SUFFIX + "'");
      }
    }
    if (dialect == null) {
      throw new DomaNullPointerException("dialect");
    }
    return getSqlFileWithCacheControl(method, path, dialect);
  }

  /**
   * Returns the SQL file in consideration of cache control.
   *
   * @param method the Dao method
   * @param path the SQL file path
   * @param dialect the dialect
   * @return the SQL file
   * @throws SqlFileNotFoundException if the SQL file is not found
   * @throws JdbcException if an error occurs
   */
  protected abstract SqlFile getSqlFileWithCacheControl(
      Method method, String path, Dialect dialect);

  /**
   * Creates the SQL file.
   *
   * @param method the Dao method
   * @param path the SQL file path
   * @param dialect the dialect
   * @return the SQL file
   */
  protected final SqlFile createSqlFile(Method method, String path, Dialect dialect) {
    Sql sqlAnnotation = method.getAnnotation(Sql.class);
    if (sqlAnnotation != null) {
      String sql = sqlAnnotation.value();
      SqlNode sqlNode = parse(sqlAnnotation.value());
      return new SqlFile(path, sql, sqlNode);
    }
    String primaryPath = getPrimaryPath(path, dialect);
    String sql = getSql(primaryPath);
    if (sql != null) {
      SqlNode sqlNode = parse(sql);
      return new SqlFile(primaryPath, sql, sqlNode);
    }
    sql = getSql(path);
    if (sql != null) {
      SqlNode sqlNode = parse(sql);
      return new SqlFile(path, sql, sqlNode);
    }
    throw new SqlFileNotFoundException(path);
  }

  /**
   * Returns the primary path to find SQL file for specific RDBMS.
   *
   * @param path the SQL file path
   * @param dialect the dialect
   * @return the primary path
   */
  protected final String getPrimaryPath(String path, Dialect dialect) {
    return SqlFileUtil.convertToDbmsSpecificPath(path, dialect);
  }

  /**
   * Parses the SQL string.
   *
   * @param sql the SQL string
   * @return the SQL node
   */
  protected final SqlNode parse(String sql) {
    SqlParser parser = new SqlParser(sql);
    return parser.parse();
  }

  /**
   * Retrieves the SQL string from the SQL file.
   *
   * @param path the SQL file path
   * @return the SQL string
   */
  protected final String getSql(String path) {
    try {
      return ResourceUtil.getResourceAsString(path);
    } catch (WrapException e) {
      Throwable cause = e.getCause();
      throw new JdbcException(Message.DOMA2010, cause, path, cause);
    }
  }
}
