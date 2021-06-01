package org.seasar.doma.internal.apt.processor.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.seasar.doma.AnnotateWith;
import org.seasar.doma.Annotation;
import org.seasar.doma.AnnotationTarget;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@AnnotateWith(
    annotations = {
      @Annotation(
          target = AnnotationTarget.CONSTRUCTOR,
          type = ConstructorAnnotation.class,
          elements = "aaa = 1, bbb = true"),
      @Annotation(
          target = AnnotationTarget.CONSTRUCTOR,
          type = ConstructorAnnotation2.class,
          elements = "aaa = 1, bbb = true"),
    })
public @interface MultipleAnnotationConfig2 {}
