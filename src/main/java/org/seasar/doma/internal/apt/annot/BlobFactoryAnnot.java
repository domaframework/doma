package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.BlobFactory;
import org.seasar.doma.internal.apt.Context;

public class BlobFactoryAnnot {

  protected final AnnotationMirror annotationMirror;

  protected BlobFactoryAnnot(AnnotationMirror annotationMirror) {
    this.annotationMirror = annotationMirror;
  }

  public static BlobFactoryAnnot newInstance(ExecutableElement method, Context ctx) {
    assertNotNull(ctx);
    AnnotationMirror annotationMirror =
        ctx.getElements().getAnnotationMirror(method, BlobFactory.class);
    if (annotationMirror == null) {
      return null;
    }
    return new BlobFactoryAnnot(annotationMirror);
  }
}
