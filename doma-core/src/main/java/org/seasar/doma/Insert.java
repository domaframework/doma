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
import org.seasar.doma.jdbc.SqlFileNotFoundException;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.UniqueConstraintException;
import org.seasar.doma.jdbc.query.DuplicateKeyType;

/**
 * Indicates a insert.
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
   * The properties whose mapped columns are included in SQL INSERT statements.
   *
   * <p>Only if {@link #sqlFile()} is {@code false}, this element value is used.
   *
   * @return the included properties
   */
  String[] include() default {};

  /**
   * The properties whose mapped columns are excluded from SQL INSERT statements.
   *
   * <p>Only if {@link #sqlFile()} is {@code false}, this element value is used.
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

  /**
   * @return the {@link Returning} annotation configuration, which defines inclusions or exclusions
   *     for returning values.
   */
  Returning returning() default @Returning;
}
