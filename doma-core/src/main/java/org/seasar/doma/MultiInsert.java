package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Statement;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SqlFileNotFoundException;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.UniqueConstraintException;

/**
 * Indicates a multi-row insert.
 *
 * <p>The annotated method must be a member of a {@link Dao} annotated interface.
 *
 * <pre>
 * &#064;Entity
 * public class Employee {
 *     ...
 * }
 *
 * &#064;Dao
 * public interface EmployeeDao {
 *
 *     &#064;MultiInsert
 *     int insert(List&lt;Employee&gt; employees);
 * }
 * </pre>
 *
 * The method may throw following exceptions:
 *
 * <ul>
 *   <li>{@link DomaNullPointerException} if any of the method parameters are {@code null}
 *   <li>{@link UniqueConstraintException} if an unique constraint is violated
 *   <li>{@link JdbcException} if a JDBC related error occurs
 * </ul>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@DaoMethod
public @interface MultiInsert {

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
   * The properties whose mapped columns are included in SQL INSERT statements.
   *
   * @return the included properties
   */
  String[] include() default {};

  /**
   * The properties whose mapped columns are excluded from SQL INSERT statements.
   *
   * @return the excluded properties
   */
  String[] exclude() default {};

  /**
   * @return the output format of SQL logs.
   */
  SqlLogType sqlLog() default SqlLogType.FORMATTED;
}
