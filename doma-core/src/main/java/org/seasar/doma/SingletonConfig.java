package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.seasar.doma.jdbc.Config;

/**
 * Indicates that the annotated class is an implementation class of the {@link Config} interface and
 * it is a singleton.
 *
 * <p>The annotated class must have a static method that returns a singleton. The all constructors
 * of the class must be private.
 *
 * @deprecated create a singleton {@link Config} without this annotation
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
public @interface SingletonConfig {

  /**
   * The static method name that returns a singleton.
   *
   * <p>The method must meet following requirements:
   *
   * <ul>
   *   <li>public and static
   *   <li>the return type is same as the annotated class
   *   <li>has no parameter
   * </ul>
   *
   * @return the method name
   */
  String method() default "singleton";
}
