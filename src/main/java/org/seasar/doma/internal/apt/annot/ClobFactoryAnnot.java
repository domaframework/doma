package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.ClobFactory;
import org.seasar.doma.internal.apt.Context;

public class ClobFactoryAnnot {

  protected final AnnotationMirror annotationMirror;

  protected ClobFactoryAnnot(AnnotationMirror annotationMirror) {
    this.annotationMirror = annotationMirror;
  }

  public static ClobFactoryAnnot newInstance(ExecutableElement method, Context ctx) {
    assertNotNull(ctx);
    AnnotationMirror annotationMirror =
        ctx.getElements().getAnnotationMirror(method, ClobFactory.class);
    if (annotationMirror == null) {
      return null;
    }
    return new ClobFactoryAnnot(annotationMirror);
  }
}
