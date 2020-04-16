package org.seasar.doma.internal.apt.annot;

import javax.lang.model.element.AnnotationMirror;

class AbstractAnnot implements Annot {

  private final AnnotationMirror annotationMirror;

  AbstractAnnot(AnnotationMirror annotationMirror) {
    this.annotationMirror = annotationMirror;
  }

  public AnnotationMirror getAnnotationMirror() {
    return annotationMirror;
  }
}
