package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.PreparedStatement;
import java.sql.Statement;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SqlFileNotFoundException;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.UniqueConstraintException;
import org.seasar.doma.jdbc.query.DuplicateKeyType;

/**
 * Indicates a batch insert.
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
 *     &#064;BatchInsert
 *     int[] insert(List&lt;Employee&gt; employee);
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
public @interface BatchInsert {

  /**
   * @return whether the annotated method is mapped to an SQL file.
   */
  boolean sqlFile() default false;

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
   * The properties whose mapped columns are included in SQL INSERT statements.
   *
   * <p>Only if {@link #sqlFile()} is {@code false}, this value is available.
   *
   * @return the included properties
   */
  String[] include() default {};

  /**
   * The properties whose mapped columns are excluded from SQL INSERT statements.
   *
   * <p>Only if {@link #sqlFile()} is {@code false}, this value is available.
   *
   * @return the excluded properties
   */
  String[] exclude() default {};

  /**
   * @return the output format of SQL logs.
   */
  SqlLogType sqlLog() default SqlLogType.FORMATTED;

  /**
   * Whether auto-generated keys are not retrieved from the database to improve performance. If this
   * flag is enabled, entities won't have auto-generated keys.
   *
   * @return Whether auto-generated keys are not retrieved.
   */
  boolean ignoreGeneratedKeys() default false;

  /**
   * This variable represents the type of duplicate key handling strategy for an insert operation.
   * It can have one of three values:
   *
   * <ul>
   *   <li>UPDATE: If a duplicate key is encountered, the existing row in the table will be updated.
   *   <li>IGNORE: If a duplicate key is encountered, the insert operation will be ignored, and no
   *       changes will be made to the table.
   *   <li>EXCEPTION: If a duplicate key is encountered, the operation will throw an exception,
   *       indicating that a duplicate key exists.
   * </ul>
   *
   * @return the type of duplicate key handling strategy for an insert operation.
   */
  DuplicateKeyType duplicateKeyType() default DuplicateKeyType.EXCEPTION;
}
