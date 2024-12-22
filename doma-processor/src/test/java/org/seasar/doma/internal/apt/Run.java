package org.seasar.doma.internal.apt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Run {

  CompilerKind[] onlyIf() default {};

  CompilerKind[] unless() default {};
}
