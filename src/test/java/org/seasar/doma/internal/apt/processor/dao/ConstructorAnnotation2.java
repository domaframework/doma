package org.seasar.doma.internal.apt.processor.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.CONSTRUCTOR)
public @interface ConstructorAnnotation2 {

  int aaa();

  boolean bbb();
}
