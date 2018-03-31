package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Connection;
import java.sql.NClob;
import org.seasar.doma.jdbc.JdbcException;

/**
 * Indicates to create a {@link NClob} instance.
 *
 * <p>The annotated method must be a member of a {@link Dao} annotated interface.
 *
 * <p>
 *
 * <h3>例:</h3>
 *
 * <pre>
 * &#064;Dao(config = AppConfig.class)
 * public interface EmployeeDao {
 *
 *     &#064;NClobFactory
 *     NClob createNClob();
 * }
 * </pre>
 *
 * The annotated method may throw following exceptions:
 *
 * <ul>
 *   <li>{@link DomaNullPointerException} if any of the method parameters are {@code null}
 *   <li>{@link JdbcException} if a JDBC related error occurs
 * </ul>
 *
 * @see Connection#createNClob()
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NClobFactory {}
