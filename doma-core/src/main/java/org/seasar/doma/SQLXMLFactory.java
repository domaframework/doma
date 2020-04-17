package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Connection;
import java.sql.SQLXML;
import org.seasar.doma.jdbc.JdbcException;

/**
 * Indicates to create a {@link SQLXML} instance。
 *
 * <p>The annotated method must be a member of a {@link Dao} annotated interface.
 *
 * <pre>
 * &#064;Dao(config = AppConfig.class)
 * public interface EmployeeDao {
 *
 *     &#064;SQLXMLFactory
 *     SQLXML createSQLXML();
 * }
 * </pre>
 *
 * The method may throws following exceptions:
 *
 * <ul>
 *   <li>{@link JdbcException} if a JDBC related error occurs
 * </ul>
 *
 * @see Connection#createSQLXML()
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@DaoMethod
public @interface SQLXMLFactory {}
