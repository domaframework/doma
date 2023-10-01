package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a database table.
 *
 * <p>This annotation must be used in conjunction with the {@link Entity} annotation.
 *
 * <pre>
 * &#064;Entity
 * &#064;Table(name = &quot;EMP&quot;)
 * public class Employee {
 *     ...
 * }
 * </pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

  /**
   * @return the catalog name.
   */
  String catalog() default "";

  /**
   * @return the schema name.
   */
  String schema() default "";

  /**
   * The table name.
   *
   * <p>If not specified, the table name is resolved by {@link Entity#naming()}.
   *
   * @return the table name
   */
  String name() default "";

  /**
   * @return whether quotation marks are used for the catalog name, the schema name and the table
   *     name.
   */
  boolean quote() default false;
}
