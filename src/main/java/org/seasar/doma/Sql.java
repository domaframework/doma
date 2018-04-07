package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Indicates the SQL. */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sql {

  /** The SQL. */
  String value() default "";

  /*
   * Whether to use the file that contains the SQL.
   *
   * <p>if {@code true}, the SQL is loaded from the file.
   */
  boolean useFile() default false;
}
