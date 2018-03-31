package org.seasar.doma;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Used in conjunction with the {@link AnnotateWith} annotation to indicate which kind of annotation
 * is specified for generated code.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Annotation {

  /** The location where the annotation is specified. */
  AnnotationTarget target();

  /** The annotation class that this annotation represents. */
  Class<? extends java.lang.annotation.Annotation> type();

  /**
   * The annotation elements as a set of key-value pair.
   *
   * <p>Represented in CSV format:
   *
   * <pre>
   * elementName1=value1, elementName2=value2
   * </pre>
   */
  String elements() default "";
}
