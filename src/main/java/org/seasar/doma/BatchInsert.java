/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
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

/**
 * Indicates a batch insert.
 * <p>
 * The annotated method must be a member of a {@link Dao} annotated interface.
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
 *     &#064;BatchInsert
 *     int[] insert(List&lt;Employee&gt; employee);
 * }
 * </pre>
 * 
 * The method may throw following exceptions:
 * <ul>
 * <li>{@link DomaNullPointerException} if any of the method parameters are
 * {@code null}
 * <li>{@link UniqueConstraintException} if an unique constraint is violated
 * <li>{@link SqlFileNotFoundException} if {@code sqlFile} is {@code true} and
 * the SQL file is not found
 * <li>{@link JdbcException} if a JDBC related error occurs
 * </ul>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@DaoMethod
public @interface BatchInsert {

    /**
     * Whether the annotated method is mapped to an SQL file.
     */
    boolean sqlFile() default false;

    /**
     * The query timeout in seconds.
     * <p>
     * If not specified, {@link Config#getQueryTimeout()} is used.
     * 
     * @see Statement#setQueryTimeout(int)
     */
    int queryTimeout() default -1;

    /**
     * The batch size.
     * <p>
     * If not specified, {@link Config#getBatchSize()} is used.
     * <p>
     * This value is used when {@link PreparedStatement#executeBatch()} is
     * executed.
     * 
     * @see PreparedStatement#addBatch()
     */
    int batchSize() default -1;

    /**
     * The properties whose mapped columns are included in SQL INSERT
     * statements.
     * <p>
     * Only if {@link #sqlFile()} is {@code false}, this value is available.
     */
    String[] include() default {};

    /**
     * The properties whose mapped columns are excluded from SQL INSERT
     * statements.
     * <p>
     * Only if {@link #sqlFile()} is {@code false}, this value is available.
     */
    String[] exclude() default {};

    /**
     * The output format of SQL logs.
     */
    SqlLogType sqlLog() default SqlLogType.FORMATTED;
}
