package org.seasar.doma.internal.apt.reflection;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;

public class AnnotateWithReflection extends AbstractReflection {

  private final AnnotationValue annotations;

  private final List<AnnotationReflection> annotationsValue;

  AnnotateWithReflection(
      AnnotationMirror annotationMirror,
      AnnotationValue annotations,
      ArrayList<AnnotationReflection> annotationsValues) {
    super(annotationMirror);
    assertNotNull(annotations, annotationsValues);
    this.annotations = annotations;
    this.annotationsValue = Collections.unmodifiableList(annotationsValues);
  }

  public AnnotationValue getAnnotations() {
    return annotations;
  }

  public List<AnnotationReflection> getAnnotationsValue() {
    return annotationsValue;
  }
}
