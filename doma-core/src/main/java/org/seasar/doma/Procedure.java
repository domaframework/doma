package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Statement;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.UnknownColumnException;

/**
 * Indicates a stored procedure call.
 *
 * <p>The annotated method must be a member of a {@link Dao} annotated interface.
 *
 * <pre>
 * &#064;Dao
 * public interface EmployeeDao {
 *
 *     &#064;Procedure
 *     void updateSalary(@In Integer id, @InOut Reference&lt;BigDecimal&gt; salary);
 * }
 * </pre>
 *
 * The method may throw following exceptions:
 *
 * <ul>
 *   <li>{@link DomaNullPointerException} if any of the method parameters are {@code null}
 *   <li>{@link UnknownColumnException} if {@code ResultSet} is used and a column is not found in
 *       the result set
 *   <li>{@link JdbcException} if a JDBC related error occurs
 * </ul>
 *
 * @see In
 * @see InOut
 * @see Out
 * @see ResultSet
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@DaoMethod
public @interface Procedure {

  /**
   * @return the catalog name.
   */
  String catalog() default "";

  /**
   * @return the schema name.
   */
  String schema() default "";

  /**
   * The stored procedure name.
   *
   * <p>If not specified, the annotated method name is used.
   *
   * @return the stored procedure name
   */
  String name() default "";

  /**
   * @return whether quotation marks are used for the catalog name, the schema name and the stored
   *     procedure name.
   */
  boolean quote() default false;

  /**
   * The query timeout in seconds.
   *
   * <p>If not specified, {@link Config#getQueryTimeout()} is used.
   *
   * @see Statement#setQueryTimeout(int)
   * @return the query timeout
   */
  int queryTimeout() default -1;

  /**
   * The naming convention for keys of {@code Map<Object, String>}.
   *
   * <p>This value is used only if a result set is fetched as {@code Map<Object, String>} or {@code
   * List<Map<Object, String>>}.
   *
   * @return the naming convention
   */
  MapKeyNamingType mapKeyNaming() default MapKeyNamingType.NONE;

  /**
   * @return the output format of SQL logs.
   */
  SqlLogType sqlLog() default SqlLogType.FORMATTED;
}
