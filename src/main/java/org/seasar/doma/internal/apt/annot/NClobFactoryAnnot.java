package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.NClobFactory;
import org.seasar.doma.internal.apt.Context;

public class NClobFactoryAnnot {

  protected final AnnotationMirror annotationMirror;

  protected NClobFactoryAnnot(AnnotationMirror annotationMirror) {
    this.annotationMirror = annotationMirror;
  }

  public static NClobFactoryAnnot newInstance(ExecutableElement method, Context ctx) {
    assertNotNull(ctx);
    AnnotationMirror annotationMirror =
        ctx.getElements().getAnnotationMirror(method, NClobFactory.class);
    if (annotationMirror == null) {
      return null;
    }
    return new NClobFactoryAnnot(annotationMirror);
  }
}
