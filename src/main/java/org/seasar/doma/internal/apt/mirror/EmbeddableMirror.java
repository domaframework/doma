package org.seasar.doma.internal.apt.mirror;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.Embeddable;
import org.seasar.doma.internal.apt.util.ElementUtil;

/** @author nakamura-to */
public class EmbeddableMirror {

  protected final AnnotationMirror annotationMirror;

  public EmbeddableMirror(AnnotationMirror annotationMirror) {
    this.annotationMirror = annotationMirror;
  }

  public AnnotationMirror getAnnotationMirror() {
    return annotationMirror;
  }

  public static EmbeddableMirror newInstance(TypeElement clazz, ProcessingEnvironment env) {
    assertNotNull(env);
    AnnotationMirror annotationMirror =
        ElementUtil.getAnnotationMirror(clazz, Embeddable.class, env);
    if (annotationMirror == null) {
      return null;
    }
    return new EmbeddableMirror(annotationMirror);
  }
}
