package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.Embeddable;
import org.seasar.doma.internal.apt.Context;

public class EmbeddableAnnot {

  protected final AnnotationMirror annotationMirror;

  public EmbeddableAnnot(AnnotationMirror annotationMirror) {
    this.annotationMirror = annotationMirror;
  }

  public AnnotationMirror getAnnotationMirror() {
    return annotationMirror;
  }

  public static EmbeddableAnnot newInstance(TypeElement clazz, Context ctx) {
    assertNotNull(ctx);
    AnnotationMirror annotationMirror =
        ctx.getElements().getAnnotationMirror(clazz, Embeddable.class);
    if (annotationMirror == null) {
      return null;
    }
    return new EmbeddableAnnot(annotationMirror);
  }
}
