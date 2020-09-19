package org.seasar.doma.internal.apt.annot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;

public class AnnotateWithAnnot extends AbstractAnnot {

  static final String ANNOTATIONS = "annotations";

  private final AnnotationValue annotations;

  private final List<AnnotationAnnot> annotationsValue;

  AnnotateWithAnnot(
      AnnotationMirror annotationMirror,
      AnnotationValue annotations,
      ArrayList<AnnotationAnnot> annotationsValues) {
    super(annotationMirror);
    this.annotations = annotations;
    this.annotationsValue = Collections.unmodifiableList(annotationsValues);
  }

  public AnnotationValue getAnnotations() {
    return annotations;
  }

  public List<AnnotationAnnot> getAnnotationsValue() {
    return annotationsValue;
  }
}
