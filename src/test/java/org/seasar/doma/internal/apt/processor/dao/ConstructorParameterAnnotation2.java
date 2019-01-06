package org.seasar.doma.internal.apt.processor.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
public @interface ConstructorParameterAnnotation2 {

  int aaa();

  boolean bbb();
}
