package org.seasar.doma.internal.apt.lombok;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** @author nakamura-to */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Value {
  String staticConstructor() default "";
}
