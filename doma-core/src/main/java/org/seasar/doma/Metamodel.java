package org.seasar.doma;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicates a metamodel class.
 *
 * <p>For example, the name of the metamodel is "MyEmployeeMetamodel" when you specify the prefix
 * and the suffix as follows:
 *
 * <pre>
 * &#064;Entity(metamodel = &#064;Metamodel(prefix = "My", suffix="Metamodel")
 * public class Employee {
 *     ...
 * }
 * </pre>
 *
 * <p>If both the {@code prefix} and the {@code suffix} are empty, the values of the following
 * annotation processing options are used:
 *
 * <ul>
 *   <li>doma.metamodel.prefix
 *   <li>doma.metamodel.suffix
 * </ul>
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Metamodel {
  /**
   * @return the prefix for the metamodel class
   */
  String prefix() default "";

  /**
   * @return the suffix for the metamodel class
   */
  String suffix() default "";

  /**
   * @return the scope class array
   */
  Class<?>[] scopes() default {};
}
