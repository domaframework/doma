package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Statement;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.UniqueConstraintException;
import org.seasar.doma.jdbc.query.DuplicateKeyType;

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

  /**
   * This variable represents the keys that should be used to determine if a duplicate key exists.
   * If the duplicate key exists, the operation will use the {@link #duplicateKeyType()} strategy to
   * handle the duplicate key.
   *
   * <p>Note: This value is only utilized when the {@link #duplicateKeyType()} value is either
   * {@code DuplicateKeyType.UPDATE} or {@code DuplicateKeyType.IGNORE}.
   *
   * <p>Note: Certain DBMSs, such as MySQL, do not utilize this value.
   *
   * @return the keys that should be used to determine if a duplicate key exists.
   */
  String[] duplicateKeys() default {};
}
