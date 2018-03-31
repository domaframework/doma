package org.seasar.doma.internal.apt.reflection;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;

public class BatchInsertReflection extends BatchModifyReflection {

  BatchInsertReflection(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror, values);
  }
}
