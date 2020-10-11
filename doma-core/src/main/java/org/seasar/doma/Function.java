package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Statement;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.ResultMappingException;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.UnknownColumnException;

/**
 * Indicates a stored function call.
 *
 * <p>The annotated method must be a member of a {@link Dao} annotated interface.
 *
 * <pre>
 * &#064;Dao
 * public interface EmployeeDao {
 *
 *     &#064;Function
 *     BigDecimal getSalary(@In Integer id, @Out Reference&lt;String&gt; name);
 * }
 * </pre>
 *
 * The method may throw following exceptions:
 *
 * <ul>
 *   <li>{@link DomaNullPointerException} if any of the method parameters are {@code null}
 *   <li>{@link UnknownColumnException} if {@link ResultSet} is used and a column is not found in
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
public @interface Function {

  /** @return the catalog name. */
  String catalog() default "";

  /** @return the schema name. */
  String schema() default "";

  /**
   * The stored function name.
   *
   * <p>If not specified, the annotated method name is used.
   *
   * @return the stored function name
   */
  String name() default "";

  /**
   * @return whether quotation marks are used for the catalog name, the schema name and the stored
   *     function name.
   */
  boolean quote() default false;

  /**
   * The query timeout in seconds.
   *
   * <p>If not specified, {@link Config#getQueryTimeout()} is used.
   *
   * @return the query timeout
   * @see Statement#setQueryTimeout(int)
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
   * Whether to ensure that all entity properties are mapped to columns of a result set.
   *
   * <p>This value is used only if the result set is fetched as an entity or a entity list.
   *
   * <p>If {@code true} and there are some unmapped properties、 {@link ResultMappingException} is
   * thrown from the annotated method.
   *
   * @return whether to ensure that all entity properties are mapped to columns of a result set
   */
  boolean ensureResultMapping() default false;

  /** @return the output format of SQL logs. */
  SqlLogType sqlLog() default SqlLogType.FORMATTED;
}
