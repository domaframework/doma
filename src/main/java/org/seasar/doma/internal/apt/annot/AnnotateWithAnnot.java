package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;

public class AnnotateWithAnnot extends AbstractAnnot {

  private final AnnotationValue annotations;

  private final List<AnnotationAnnot> annotationsValue;

  AnnotateWithAnnot(
      AnnotationMirror annotationMirror,
      AnnotationValue annotations,
      ArrayList<AnnotationAnnot> annotationsValues) {
    super(annotationMirror);
    assertNotNull(annotations, annotationsValues);
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
