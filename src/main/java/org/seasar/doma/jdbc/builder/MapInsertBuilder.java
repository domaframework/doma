package org.seasar.doma.jdbc.builder;

import java.sql.Statement;
import java.util.Map;
import java.util.stream.Collectors;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.*;

/**
 * A builder that builds an SQL INSERT statement from a map.
 *
 * <p>This is not thread safe.
 *
 * <h4>Java</h4>
 *
 * <pre>
 * MapInsertBuilder builder = MapInsertBuilder.newInstance(config, "Emp");
 * builder.execute(new LinkedHashMap&lt;String, Object&gt;(){{
 *   put("name", "SMITH");
 *   put("salary", 1000)
 * }});
 * </pre>
 *
 * <h4>built SQL</h4>
 *
 * <pre>
 * insert into Emp
 * (name, salary)
 * values('SMITH', 1000)
 * </pre>
 *
 * @author bakenezumi
 */
public class MapInsertBuilder {

  private final InsertBuilder builder;

  private final String tableName;

  private MapInsertBuilder(Config config, String tableName) {
    this.builder = InsertBuilder.newInstance(config);
    builder.callerClassName(getClass().getName());
    this.tableName = tableName;
  }

  /**
   * Creates a new instance.
   *
   * @param config the configuration
   * @param tableName the table name
   * @return a builder
   * @throws DomaNullPointerException if {@code config} or {@code tableName} is {@code null}
   */
  public static MapInsertBuilder newInstance(Config config, String tableName) {
    if (config == null) {
      throw new DomaNullPointerException("config");
    }
    if (tableName == null) {
      throw new DomaNullPointerException("tableName");
    }
    return new MapInsertBuilder(config, tableName);
  }

  /**
   * Executes an SQL INSERT statement.
   *
   * @return the affected rows count
   * @param parameter the parameter
   * @throws DomaNullPointerException if {@code parameter} is {@code null}
   * @throws DomaIllegalArgumentException if {@code parameter} is empty
   * @throws UniqueConstraintException if an unique constraint violation occurs
   * @throws JdbcException if a JDBC related error occurs
   */
  public int execute(Map<String, Object> parameter) {
    if (parameter == null) {
      throw new DomaNullPointerException("parameter");
    }
    if (parameter.size() < 1) {
      throw new DomaIllegalArgumentException("parameter", "parameter.size() < 1");
    }
    builder
        .sql("insert into ")
        .sql(tableName)
        .sql(" (")
        .sql(parameter.keySet().stream().collect(Collectors.joining(", ")))
        .sql(")");
    builder.sql("values (");
    parameter.forEach(
        (key, value) -> {
          if (value == null) {
            builder.sql("NULL").sql(", ");
          } else {
            @SuppressWarnings("unchecked")
            final Class<Object> clazz = (Class<Object>) value.getClass();
            builder.param(clazz, value).sql(", ");
          }
        });
    builder.removeLast().sql(")");
    return builder.execute();
  }

  /**
   * Sets the query timeout limit in seconds.
   *
   * <p>If not specified, the value of {@link Config#getQueryTimeout()} is used.
   *
   * @param queryTimeout the query timeout limit in seconds
   * @see Statement#setQueryTimeout(int)
   */
  public void queryTimeout(int queryTimeout) {
    builder.queryTimeout(queryTimeout);
  }

  /**
   * Sets the SQL log format.
   *
   * @param sqlLogType the SQL log format type
   */
  public void sqlLogType(SqlLogType sqlLogType) {
    builder.sqlLogType(sqlLogType);
  }

  /**
   * Sets the caller class name.
   *
   * <p>If not specified, the class name of this instance is used.
   *
   * @param className the caller class name
   * @throws DomaNullPointerException if {@code className} is {@code null}
   */
  public void callerClassName(String className) {
    builder.callerClassName(className);
  }

  /**
   * Sets the caller method name.
   *
   * <p>if not specified, {@code execute} is used.
   *
   * @param methodName the caller method name
   * @throws DomaNullPointerException if {@code methodName} is {@code null}
   */
  public void callerMethodName(String methodName) {
    if (methodName == null) {
      throw new DomaNullPointerException("methodName");
    }
    builder.callerMethodName(methodName);
  }

  /**
   * Returns the built SQL.
   *
   * @return the built SQL
   */
  public Sql<?> getSql() {
    return builder.getSql();
  }
}
