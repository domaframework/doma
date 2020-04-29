package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNonNullValue;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class EntityDescAnnot extends AbstractAnnot {

  private static final String VALUE = "value";

  private final AnnotationValue value;

  EntityDescAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    this.value = assertNonNullValue(values, VALUE);
  }

  public AnnotationValue getValue() {
    return value;
  }

  public TypeMirror getValueValue() {
    TypeMirror result = AnnotationValueUtil.toType(value);
    if (result == null) {
      throw new AptIllegalStateException(VALUE);
    }
    return result;
  }
}
