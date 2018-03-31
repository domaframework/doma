package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Statement;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.NoResultException;
import org.seasar.doma.jdbc.NonSingleColumnException;
import org.seasar.doma.jdbc.NonUniqueResultException;
import org.seasar.doma.jdbc.ResultMappingException;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.SqlFileNotFoundException;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.UnknownColumnException;

/**
 * Indicates a select.
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
 *     &#064;Select
 *     String selectNameById(Integer id);
 *
 *     &#064;Select
 *     List&lt;Employee&gt; selectNamesByAgeAndSalary(Integer age, BigDecimal salary);
 *
 *     &#064;Select
 *     Employee selectById(Integer id);
 *
 *     &#064;Select
 *     List&lt;Employee&gt; selectByExample(Employee example);
 *
 *     &#064;Select(strategy = SelectStrategyType.STREAM)
 *     &lt;R&gt; R selectSalary(Integer departmentId, Function&lt;Stream&lt;BigDecimal&gt;, R&gt; mapper);
 * }
 * </pre>
 *
 * The method may throw following exceptions:
 *
 * <ul>
 *   <li>{@link DomaNullPointerException} if any of the method parameters are {@code null}
 *   <li>{@link SqlFileNotFoundException} if {@code sqlFile} is {@code true} and a SQL file is not
 *       found
 *   <li>{@link UnknownColumnException} if a property whose mapped column is included in a result
 *       set is not found
 *   <li>{@link NonUniqueResultException} if an unique row is expected but two or more rows are
 *       found in a result set
 *   <li>{@link NonSingleColumnException} if a single column is expected but two or more columns are
 *       found in a result set
 *   <li>{@link NoResultException} if {@link #ensureResult} is {@code true} and no row is found in a
 *       result set
 *   <li>{@link ResultMappingException} if {@link #ensureResultMapping()} is {@code true} and all
 *       entity properties are not mapped to columns of a result set
 *   <li>{@link JdbcException} if a JDBC related error occurs
 * </ul>
 *
 * @see SelectOptions
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@DaoMethod
public @interface Select {

  /**
   * The query timeout in seconds.
   *
   * <p>If not specified, {@link Config#getQueryTimeout()} is used.
   *
   * @see Statement#setQueryTimeout(int)
   */
  int queryTimeout() default -1;

  /**
   * The fetch size.
   *
   * <p>If not specified, {@link Config#getFetchSize()} is used.
   *
   * @see Statement#setFetchSize(int)
   */
  int fetchSize() default -1;

  /**
   * The maximum number of rows.
   *
   * <p>If not specified, {@link Config#getMaxRows()} is used.
   *
   * @see Statement#setMaxRows(int)
   */
  int maxRows() default -1;

  /** The strategy for handling an object that is mapped to a result set. */
  SelectType strategy() default SelectType.RETURN;

  /** The fetch type. */
  FetchType fetch() default FetchType.LAZY;

  /**
   * Whether to ensure that one or more rows are found in a result set.
   *
   * <p>if {@code true} and no row is found, {@link NoResultException} is thrown from the method.
   */
  boolean ensureResult() default false;

  /**
   * Whether to ensure that all entity properties are mapped to columns of a result set.
   *
   * <p>This value is used only if the result set is fetched as an entity or a entity list.
   *
   * <p>If {@code true} and there are some unmapped properties、 {@link ResultMappingException} is
   * thrown from the method.
   */
  boolean ensureResultMapping() default false;

  /**
   * The naming convention for keys of {@code Map<Object, String>}.
   *
   * <p>This value is used only if a result set is fetched as {@code Map<Object, String>} or {@code
   * List<Map<Object, String>>}.
   */
  MapKeyNamingType mapKeyNaming() default MapKeyNamingType.NONE;

  /** The output format of SQL logs. */
  SqlLogType sqlLog() default SqlLogType.FORMATTED;
}
