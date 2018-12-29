package org.seasar.doma.internal.apt.mirror;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.SqlProcessor;
import org.seasar.doma.internal.apt.util.ElementUtil;

/** @author nakamura */
public class SqlProcessorMirror {

  protected final AnnotationMirror annotationMirror;

  protected SqlProcessorMirror(AnnotationMirror annotationMirror) {
    this.annotationMirror = annotationMirror;
  }

  public static SqlProcessorMirror newInstance(
      ExecutableElement method, ProcessingEnvironment env) {
    assertNotNull(env);
    AnnotationMirror annotationMirror =
        ElementUtil.getAnnotationMirror(method, SqlProcessor.class, env);
    if (annotationMirror == null) {
      return null;
    }
    return new SqlProcessorMirror(annotationMirror);
  }

  public AnnotationMirror getAnnotationMirror() {
    return annotationMirror;
  }
}
