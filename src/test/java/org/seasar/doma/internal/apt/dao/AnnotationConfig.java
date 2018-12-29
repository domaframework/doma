package org.seasar.doma.internal.apt.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.seasar.doma.AnnotateWith;
import org.seasar.doma.Annotation;
import org.seasar.doma.AnnotationTarget;

/** @author taedium */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@AnnotateWith(
    annotations = {
      @Annotation(
          target = AnnotationTarget.CLASS,
          type = ClassAnnotation.class,
          elements = "aaa = 1, bbb = true"),
      @Annotation(
          target = AnnotationTarget.CLASS,
          type = ClassAnnotation2.class,
          elements = "aaa = 1, bbb = true"),
      @Annotation(
          target = AnnotationTarget.CONSTRUCTOR,
          type = ConstructorAnnotation.class,
          elements = "aaa = 1, bbb = true"),
      @Annotation(
          target = AnnotationTarget.CONSTRUCTOR,
          type = ConstructorAnnotation2.class,
          elements = "aaa = 1, bbb = true"),
      @Annotation(
          target = AnnotationTarget.CONSTRUCTOR_PARAMETER,
          type = ConstructorParameterAnnotation.class,
          elements = "aaa = 1, bbb = true"),
      @Annotation(
          target = AnnotationTarget.CONSTRUCTOR_PARAMETER,
          type = ConstructorParameterAnnotation2.class,
          elements = "aaa = 1, bbb = true")
    })
public @interface AnnotationConfig {}
