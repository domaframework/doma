/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Statement;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.OptimisticLockException;
import org.seasar.doma.jdbc.SqlFileNotFoundException;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.UniqueConstraintException;

/**
 * Indicates an update.
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
 *     &#064;Update
 *     int update(Employee employee);
 * }
 * </pre>
 *
 * <p>The method may throw following exceptions:
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
public @interface Update {

  /**
   * @return whether the annotated method is mapped to an SQL file.
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
   * Whether SQL NULL columns are excluded from SQL INSERT statements.
   *
   * <p>Only if {@link #sqlFile()} is {@code false}, this element value is used.
   *
   * @return whether SQL NULL columns are excluded
   */
  boolean excludeNull() default false;

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
   * Whether columns mapped to unchanged properties are included in SQL UPDATE statements.
   *
   * <p>Only if the method parameter type is entity class that has a {@link OriginalStates}
   * annotated filed, this element value is used.
   *
   * @return whether columns mapped to unchanged properties are included
   */
  boolean includeUnchanged() default false;

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
   * @return the excluded properties
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
