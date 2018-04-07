package org.seasar.doma.jdbc;

import java.lang.reflect.Method;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.Sql;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.internal.jdbc.util.SqlFileUtil;
import org.seasar.doma.internal.util.ResourceUtil;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.message.Message;

/** A skeletal implementation of the {@link SqlTemplateRepository} interface. */
public abstract class AbstractSqlTemplateRepository implements SqlTemplateRepository {

  @Override
  public final SqlTemplate getSqlTemplate(Method method, Dialect dialect) {
    if (method == null) {
      throw new DomaNullPointerException("method");
    }
    if (dialect == null) {
      throw new DomaNullPointerException("dialect");
    }
    return getSqlTemplateWithCacheControl(method, dialect);
  }

  /**
   * Returns the SQL template in consideration of cache control.
   *
   * @param method the Dao method
   * @param dialect the dialect
   * @return the SQL template
   * @throws SqlFileNotFoundException if the SQL file is not found
   * @throws JdbcException if an error occurs
   */
  protected abstract SqlTemplate getSqlTemplateWithCacheControl(Method method, Dialect dialect);

  /**
   * Creates the SQL template.
   *
   * @param method the Dao method
   * @param dialect the dialect
   * @return the SQL template
   */
  protected final SqlTemplate createSqlTemplate(Method method, Dialect dialect) {
    var sqlAnnotation = method.getAnnotation(Sql.class);
    var path = SqlFileUtil.buildPath(method.getDeclaringClass().getName(), method.getName());
    if (sqlAnnotation == null || sqlAnnotation.useFile()) {
      return createSqlTemplate(path, dialect);
    }
    var sql = sqlAnnotation.value();
    var sqlNode = parse(sql);
    return new SqlTemplate(sql, sqlNode, null);
  }

  /**
   * Creates the SQL template.
   *
   * @param path the SQL file path
   * @param dialect the dialect
   * @return the SQL template
   */
  protected final SqlTemplate createSqlTemplate(String path, Dialect dialect) {
    var primaryPath = getPrimaryPath(path, dialect);
    var sql = loadSql(primaryPath);
    if (sql != null) {
      var sqlNode = parse(sql);
      return new SqlTemplate(sql, sqlNode, primaryPath);
    }
    sql = loadSql(path);
    if (sql != null) {
      var sqlNode = parse(sql);
      return new SqlTemplate(sql, sqlNode, path);
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
    var parser = new SqlParser(sql);
    return parser.parse();
  }

  /**
   * Retrieves the SQL string from the SQL file.
   *
   * @param path the SQL file path
   * @return the SQL string
   */
  protected final String loadSql(String path) {
    try {
      return ResourceUtil.getResourceAsString(path);
    } catch (WrapException e) {
      var cause = e.getCause();
      throw new JdbcException(Message.DOMA2010, cause, path, cause);
    }
  }
}
