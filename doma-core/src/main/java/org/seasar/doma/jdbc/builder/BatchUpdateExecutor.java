package org.seasar.doma.jdbc.builder;

import java.sql.Statement;
import java.util.List;
import java.util.function.BiConsumer;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.UniqueConstraintException;
import org.seasar.doma.jdbc.command.BatchUpdateCommand;
import org.seasar.doma.jdbc.query.SqlBatchUpdateQuery;

/**
 * An executor that execute SQL UPDATE statements in batches.
 *
 * <p>This is not thread safe.
 *
 * <h2>Java</h2>
 *
 * <pre>
 * List&lt;Employee&gt; employees = Arrays
 *         .asList(new Employee[] { new Employee(10, &quot;SMITH&quot;, new BigDecimal(&quot;1000&quot;)),
 *                 new Employee(20, &quot;ALLEN&quot;, new BigDecimal(&quot;2000&quot;)) });
 * BatchUpdateExecutor executor = BatchUpdateExecutor.newInstance(config);
 * executor.batchSize(10);
 * executor.execute(employees, (emp, builder) -&gt; {
 *     builder.sql(&quot;update Emp&quot;);
 *     builder.sql(&quot;set&quot;);
 *     builder.sql(&quot;name = &quot;).param(String.class, emp.name).sql(&quot;,&quot;);
 *     builder.sql(&quot;salary = &quot;).param(BigDecimal.class, emp.salary);
 *     builder.sql(&quot;where&quot;);
 *     builder.sql(&quot;id = &quot;).param(int.class, emp.id);
 * });
 * </pre>
 *
 * <h2>built SQLs</h2>
 *
 * <pre>
 * update Emp
 * set
 * name = 'SMITH',
 * salary = 1000
 * where
 * id = 10
 *
 * update Emp
 * set
 * name = 'ALLEN',
 * salary = 2000
 * where
 * id = 20
 * </pre>
 *
 * @author bakenezumi
 */
public class BatchUpdateExecutor {

  private final SqlBatchUpdateQuery query;

  private BatchUpdateExecutor(Config config) {
    this.query = new SqlBatchUpdateQuery();
    this.query.setConfig(config);
    this.query.setCallerClassName(getClass().getName());
    this.query.setSqlLogType(SqlLogType.FORMATTED);
  }

  /**
   * Creates a new instance.
   *
   * @param config the configuration
   * @return a executor
   * @throws DomaNullPointerException if {@code config} is {@code null}
   */
  public static BatchUpdateExecutor newInstance(Config config) {
    if (config == null) {
      throw new DomaNullPointerException("config");
    }
    return new BatchUpdateExecutor(config);
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
    query.setQueryTimeout(queryTimeout);
  }

  /**
   * Sets the SQL log format.
   *
   * @param sqlLogType the SQL log format type
   */
  public void sqlLogType(SqlLogType sqlLogType) {
    if (sqlLogType == null) {
      throw new DomaNullPointerException("sqlLogType");
    }
    query.setSqlLogType(sqlLogType);
  }

  /**
   * Sets the batch size.
   *
   * <p>If not specified, the value of {@link Config#getBatchSize()} is used.
   *
   * @param batchSize the batch size
   */
  public void batchSize(int batchSize) {
    query.setBatchSize(batchSize);
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
    if (className == null) {
      throw new DomaNullPointerException("className");
    }
    query.setCallerClassName(className);
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
    query.setCallerMethodName(methodName);
  }

  /**
   * Executes SQL UPDATE statements.
   *
   * @param <P> the parameter type
   * @param params the parameters
   * @param buildConsumer the code block that builds SQL statements
   * @return the array whose each element contains affected rows count. The array length is equal to
   *     the {@code parameter} size.
   * @throws DomaNullPointerException if {@code params} or {@code buildConsumer} is {@code null}
   * @throws UniqueConstraintException if an unique constraint violation occurs
   * @throws JdbcException if a JDBC related error occurs
   */
  public <P> int[] execute(Iterable<P> params, BiConsumer<P, BatchBuilder> buildConsumer) {
    if (params == null) {
      throw new DomaNullPointerException("params");
    }
    if (buildConsumer == null) {
      throw new DomaNullPointerException("buildConsumer");
    }
    if (query.getMethodName() == null) {
      query.setCallerMethodName("execute");
    }
    BatchBuilder builder = BatchBuilder.newInstance(query);
    for (P p : params) {
      buildConsumer.accept(p, builder);
      builder = builder.fixSql();
    }
    return builder.execute(() -> new BatchUpdateCommand(query));
  }

  /**
   * Returns the built SQL.
   *
   * @return the built SQL
   */
  public List<? extends Sql<?>> getSqls() {
    return query.getSqls();
  }
}
