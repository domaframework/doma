package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Array;
import java.sql.Connection;
import org.seasar.doma.jdbc.JdbcException;

/**
 * Indicates to create an {@link Array} instance.
 *
 * <p>The annotated method must be a member of a {@link Dao} annotated interface.
 *
 * <pre>
 * &#064;Dao(config = AppConfig.class)
 * public interface EmployeeDao {
 *
 *     &#064;ArrayFactory(typeName = &quot;integer&quot;)
 *     Array createIntegerArray(Integer[] elements);
 * }
 * </pre>
 *
 * The method may throw following exceptions:
 *
 * <ul>
 *   <li>{@link DomaNullPointerException} if any of the method parameters are {@code null}
 *   <li>{@link JdbcException} if a JDBC related error occurs
 * </ul>
 *
 * @see Connection#createArrayOf(String, Object[])
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@DaoMethod
public @interface ArrayFactory {

  /**
   * The SQL name of the type the elements of the array map to.
   *
   * <p>The value is passed as the first argument to {@link Connection#createArrayOf(String,
   * Object[])}.
   *
   * @return the type name
   */
  String typeName();
}
