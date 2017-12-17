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
