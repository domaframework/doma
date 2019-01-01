package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.List;
import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class DomainConvertersAnnot extends AbstractAnnot {

  private static final String VALUE = "value";

  private final AnnotationValue value;

  DomainConvertersAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    this.value = assertNonNullValue(values, VALUE);
  }

  public AnnotationValue getValue() {
    return value;
  }

  public List<TypeMirror> getValueValue() {
    List<TypeMirror> typeList = AnnotationValueUtil.toTypeList(value);
    if (typeList == null) {
      throw new AptIllegalStateException(VALUE);
    }
    return typeList;
  }
}
