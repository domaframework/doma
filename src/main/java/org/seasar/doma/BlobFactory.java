package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Blob;
import java.sql.Connection;
import org.seasar.doma.jdbc.JdbcException;

/**
 * Indicates to create a {@link Blob} instance.
 *
 * <p>The annotated method must be a member of a {@link Dao} annotated interface.
 *
 * <pre>
 * &#064;Dao(config = AppConfig.class)
 * public interface EmployeeDao {
 *
 *     &#064;BlobFactory
 *     Blob createBlob();
 * }
 * </pre>
 *
 * The method may throw following exceptions:
 *
 * <ul>
 *   <li>{@link JdbcException} if a JDBC related error occurs
 * </ul>
 *
 * @see Connection#createBlob()
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@DaoMethod
public @interface BlobFactory {}
