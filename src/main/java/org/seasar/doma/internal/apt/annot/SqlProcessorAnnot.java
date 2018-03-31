package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.AnnotationMirror;

public class SqlProcessorAnnot extends AbstractAnnot {

  SqlProcessorAnnot(AnnotationMirror annotationMirror) {
    super(annotationMirror);
    assertNotNull(annotationMirror);
  }
}
