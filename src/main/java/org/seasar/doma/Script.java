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

import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.ScriptException;
import org.seasar.doma.jdbc.ScriptFileNotFoundException;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.dialect.Dialect;

/**
 * Indicates a script.
 * <p>
 * The annotated method must be a member of a {@link Dao} annotated interface.
 * <p>
 * 
 * <pre>
 * &#064;Dao(config = AppConfig.class)
 * public interface EmployeeDao {
 * 
 *     &#064;Script
 *     void createTables();
 * }
 * </pre>
 * 
 * The method may throw following exceptions:
 * <ul>
 * <li>{@link ScriptFileNotFoundException} if a script file is not found
 * <li>{@link ScriptException} if an exception is thrown while executing a
 * script
 * <li>{@link JdbcException} if a JDBC related error occurs
 * </ul>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@DaoMethod
public @interface Script {

    /**
     * The SQL block delimiter.
     * <p>
     * The SQL block delimiter is a mark that indicates the end of definition of
     * such as stored procedures, stored functions and triggers.
     * <p>
     * If not specified, the return value of
     * {@link Dialect#getScriptBlockDelimiter()} is used.
     */
    String blockDelimiter() default "";

    /**
     * Whether to halt a script execution when an error occurs.
     */
    boolean haltOnError() default true;

    /**
     * The output format of SQL logs.
     */
    SqlLogType sqlLog() default SqlLogType.FORMATTED;
}
