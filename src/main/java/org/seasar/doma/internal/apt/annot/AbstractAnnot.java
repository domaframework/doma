package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.AnnotationMirror;

public abstract class AbstractAnnot {

  private final AnnotationMirror annotationMirror;

  AbstractAnnot(AnnotationMirror annotationMirror) {
    assertNotNull(annotationMirror);
    this.annotationMirror = annotationMirror;
  }

  public AnnotationMirror getAnnotationMirror() {
    return annotationMirror;
  }
}
