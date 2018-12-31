package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.SqlProcessor;
import org.seasar.doma.internal.apt.Context;

public class SqlProcessorAnnot {

  protected final AnnotationMirror annotationMirror;

  protected SqlProcessorAnnot(AnnotationMirror annotationMirror) {
    this.annotationMirror = annotationMirror;
  }

  public static SqlProcessorAnnot newInstance(ExecutableElement method, Context ctx) {
    assertNotNull(ctx);
    AnnotationMirror annotationMirror =
        ctx.getElements().getAnnotationMirror(method, SqlProcessor.class);
    if (annotationMirror == null) {
      return null;
    }
    return new SqlProcessorAnnot(annotationMirror);
  }

  public AnnotationMirror getAnnotationMirror() {
    return annotationMirror;
  }
}
