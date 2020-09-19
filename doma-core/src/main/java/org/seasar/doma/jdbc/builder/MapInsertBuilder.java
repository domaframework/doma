package org.seasar.doma.jdbc.builder;

import java.util.Map;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlLogType;

/**
 * A builder that builds an SQL INSERT statement from a map.
 *
 * <p>This is not thread safe.
 *
 * <h2>Java</h2>
 *
 * <pre>
 * MapInsertBuilder builder = MapInsertBuilder.newInstance(config, "Emp");
 * builder.execute(new LinkedHashMap&lt;String, Object&gt;(){{
 *   put("name", "SMITH");
 *   put("salary", 1000)
 * }});
 * </pre>
 *
 * <h2>built SQL</h2>
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

  public static MapInsertBuilder newInstance(Config config, String tableName) {
    if (config == null) {
      throw new DomaNullPointerException("config");
    }
    if (tableName == null) {
      throw new DomaNullPointerException("tableName");
    }
    return new MapInsertBuilder(config, tableName);
  }

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
        .sql(String.join(", ", parameter.keySet()))
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

  public void queryTimeout(int queryTimeout) {
    builder.queryTimeout(queryTimeout);
  }

  public void sqlLogType(SqlLogType sqlLogType) {
    builder.sqlLogType(sqlLogType);
  }

  public void callerClassName(String className) {
    builder.callerClassName(className);
  }

  public void callerMethodName(String methodName) {
    if (methodName == null) {
      throw new DomaNullPointerException("methodName");
    }
    builder.callerMethodName(methodName);
  }

  public Sql<?> getSql() {
    return builder.getSql();
  }
}
