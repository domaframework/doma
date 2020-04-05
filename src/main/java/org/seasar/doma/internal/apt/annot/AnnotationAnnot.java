package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class AnnotationAnnot extends AbstractAnnot {

  private static final String TARGET = "target";

  private static final String TYPE = "type";

  private static final String ELEMENTS = "elements";

  private final AnnotationValue target;

  private final AnnotationValue type;

  private final AnnotationValue elements;

  AnnotationAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    this.target = assertNonNullValue(values, TARGET);
    this.type = assertNonNullValue(values, TYPE);
    this.elements = assertNonNullValue(values, ELEMENTS);
  }

  public AnnotationValue getTarget() {
    return target;
  }

  public AnnotationValue getType() {
    return type;
  }

  public AnnotationValue getElements() {
    return elements;
  }

  public VariableElement getTargetValue() {
    VariableElement value = AnnotationValueUtil.toEnumConstant(target);
    if (value == null) {
      throw new AptIllegalStateException(TARGET);
    }
    return value;
  }

  public TypeMirror getTypeValue() {
    TypeMirror value = AnnotationValueUtil.toType(type);
    if (value == null) {
      throw new AptIllegalStateException(TYPE);
    }
    return value;
  }

  public String getElementsValue() {
    String value = AnnotationValueUtil.toString(elements);
    if (value == null) {
      throw new AptIllegalStateException(ELEMENTS);
    }
    return value;
  }
}
