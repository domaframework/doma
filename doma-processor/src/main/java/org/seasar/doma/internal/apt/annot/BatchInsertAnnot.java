package org.seasar.doma.internal.apt.annot;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;

public class BatchInsertAnnot extends BatchModifyAnnot {

  BatchInsertAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror, values);
  }
}
