package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a database column.
 *
 * <p>The annotated field must be a member of an {@link Entity} annotated class.
 *
 * <pre>
 * &#064;Entity
 * public class Employee {
 *
 *     &#064;Column(name = &quot;EMPLOYEE_NAME&quot;)
 *     String employeeName;
 *
 *     &#064;Column(name = &quot;SALARY&quot;)
 *     BigDecimal salary;
 *
 *     ...
 * }
 * </pre>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

  /**
   * The name of the column.
   *
   * <p>If not specified, the name is resolved by {@link Entity#naming()}.
   *
   * @return the name
   */
  String name() default "";

  /**
   * @return whether the column is included in SQL INSERT statements.
   */
  boolean insertable() default true;

  /**
   * @return whether the column is included in SQL UPDATE statements.
   */
  boolean updatable() default true;

  /**
   * @return whether the column name is enclosed by quotation marks in SQL statements.
   */
  boolean quote() default false;
}
