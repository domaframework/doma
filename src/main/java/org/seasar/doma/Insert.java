package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Statement;
import org.seasar.doma.jdbc.*;

/**
 * Indicates a insert.
 *
 * <p>The annotated method must be a member of a {@link Dao} annotated interface.
 *
 * <p>
 *
 * <pre>
 * &#064;Entity
 * public class Employee {
 *     ...
 * }
 *
 * &#064;Dao(config = AppConfig.class)
 * public interface EmployeeDao {
 *
 *     &#064;Insert
 *     int insert(Employee employee);
 * }
 * </pre>
 *
 * The method may throw following exceptions:
 *
 * <ul>
 *   <li>{@link DomaNullPointerException} if any of the method parameters are {@code null}
 *   <li>{@link UniqueConstraintException} if an unique constraint is violated
 *   <li>{@link SqlFileNotFoundException} if {@code sqlFile} is {@code true} and the SQL file is not
 *       found
 *   <li>{@link JdbcException} if a JDBC related error occurs
 * </ul>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@DaoMethod
public @interface Insert {

  /** Whether the annotated method is mapped to an SQL file. */
  boolean sqlFile() default false;

  /**
   * The query timeout in seconds.
   *
   * <p>If not specified, {@link Config#getQueryTimeout()} is used.
   *
   * @see Statement#setQueryTimeout(int)
   */
  int queryTimeout() default -1;

  /**
   * Whether SQL NULL columns are excluded from SQL INSERT statements.
   *
   * <p>Only if {@link #sqlFile()} is {@code false}, this element value is used.
   */
  boolean excludeNull() default false;

  /**
   * The properties whose mapped columns are included in SQL INSERT statements.
   *
   * <p>Only if {@link #sqlFile()} is {@code false}, this element value is used.
   */
  String[] include() default {};

  /**
   * The properties whose mapped columns are excluded from SQL INSERT statements.
   *
   * <p>Only if {@link #sqlFile()} is {@code false}, this element value is used.
   */
  String[] exclude() default {};

  /** The output format of SQL logs. */
  SqlLogType sqlLog() default SqlLogType.FORMATTED;
}
