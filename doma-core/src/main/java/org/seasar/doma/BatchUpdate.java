package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.PreparedStatement;
import java.sql.Statement;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.OptimisticLockException;
import org.seasar.doma.jdbc.SqlFileNotFoundException;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.UniqueConstraintException;

/**
 * Indicates a batch update.
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
 *     &#064;BatchUpdate
 *     int[] update(List&lt;Employee&gt; employee);
 * }
 * </pre>
 *
 * The method may throw following exceptions:
 *
 * <ul>
 *   <li>{@link DomaNullPointerException} if any of the method parameters are {@code null}
 *   <li>{@link OptimisticLockException} if optimistic locking is enabled and an update count is 0
 *       for each entity
 *   <li>{@link UniqueConstraintException} if an unique constraint is violated
 *   <li>{@link SqlFileNotFoundException} if {@code sqlFile} is {@code true} and the SQL file is not
 *       found
 *   <li>{@link JdbcException} if a JDBC related error occurs
 * </ul>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@DaoMethod
public @interface BatchUpdate {

  /**
   * @return whether an annotated method is mapped to an SQL file.
   */
  boolean sqlFile() default false;

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
   * The batch size.
   *
   * <p>If not specified, {@link Config#getBatchSize()} is used.
   *
   * <p>This value is used when {@link PreparedStatement#executeBatch()} is executed.
   *
   * @return the batch size
   * @see PreparedStatement#addBatch()
   */
  int batchSize() default -1;

  /**
   * Whether the version property is ignored.
   *
   * <p>If {@code true}, the column that mapped to the version property is excluded from SQL UPDATE
   * statements.
   *
   * @return whether the version property is ignored
   */
  boolean ignoreVersion() default false;

  /**
   * The properties whose mapped columns are included in SQL UPDATE statements.
   *
   * <p>Only if {@link #sqlFile()} is {@code false}, this value is used.
   *
   * @return the included properties
   */
  String[] include() default {};

  /**
   * The properties whose mapped columns are excluded from SQL UPDATE statements.
   *
   * <p>Only if {@link #sqlFile()} is {@code false}, this value is used.
   *
   * @return excluded properties
   */
  String[] exclude() default {};

  /**
   * Whether {@link OptimisticLockException} is suppressed.
   *
   * <p>Only if {@link #sqlFile()} is {@code false}, this element value is used.
   *
   * @return whether {@link OptimisticLockException} is suppressed
   */
  boolean suppressOptimisticLockException() default false;

  /**
   * @return the output format of SQL logs.
   */
  SqlLogType sqlLog() default SqlLogType.FORMATTED;
}
