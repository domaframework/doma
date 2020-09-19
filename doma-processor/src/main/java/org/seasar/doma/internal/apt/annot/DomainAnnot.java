package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNonNullValue;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class DomainAnnot extends AbstractAnnot {

  private static final String VALUE_TYPE = "valueType";

  private static final String FACTORY_METHOD = "factoryMethod";

  private static final String ACCESSOR_METHOD = "accessorMethod";

  private static final String ACCEPT_NULL = "acceptNull";

  private final AnnotationValue valueType;

  private final AnnotationValue factoryMethod;

  private final AnnotationValue accessorMethod;

  private final AnnotationValue acceptNull;

  DomainAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    this.valueType = assertNonNullValue(values, VALUE_TYPE);
    this.factoryMethod = assertNonNullValue(values, FACTORY_METHOD);
    this.accessorMethod = assertNonNullValue(values, ACCESSOR_METHOD);
    this.acceptNull = assertNonNullValue(values, ACCEPT_NULL);
  }

  public AnnotationValue getValueType() {
    return valueType;
  }

  public AnnotationValue getFactoryMethod() {
    return factoryMethod;
  }

  public AnnotationValue getAccessorMethod() {
    return accessorMethod;
  }

  public AnnotationValue getAcceptNull() {
    return acceptNull;
  }

  public TypeMirror getValueTypeValue() {
    TypeMirror value = AnnotationValueUtil.toType(valueType);
    if (value == null) {
      throw new AptIllegalStateException(VALUE_TYPE);
    }
    return value;
  }

  public String getFactoryMethodValue() {
    String value = AnnotationValueUtil.toString(factoryMethod);
    if (value == null) {
      throw new AptIllegalStateException(FACTORY_METHOD);
    }
    return value;
  }

  public String getAccessorMethodValue() {
    String value = AnnotationValueUtil.toString(accessorMethod);
    if (value == null) {
      throw new AptIllegalStateException(ACCESSOR_METHOD);
    }
    return value;
  }

  public boolean getAcceptNullValue() {
    Boolean value = AnnotationValueUtil.toBoolean(acceptNull);
    if (value == null) {
      throw new AptIllegalStateException(ACCEPT_NULL);
    }
    return value;
  }
}
