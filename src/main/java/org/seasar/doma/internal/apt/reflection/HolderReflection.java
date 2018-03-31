package org.seasar.doma.internal.apt.reflection;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNullValue;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

/** @author taedium */
public class HolderReflection extends AbstractReflection {

  public static final String ACCEPT_NULL = "acceptNull";

  public static final String ACCESSOR_METHOD = "accessorMethod";

  public static final String FACTORY_METHOD = "factoryMethod";

  public static final String VALUE_TYPE = "valueType";

  private final AnnotationValue valueType;

  private final AnnotationValue factoryMethod;

  private final AnnotationValue accessorMethod;

  private final AnnotationValue acceptNull;

  HolderReflection(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    assertNotNull(values);
    this.valueType = assertNotNullValue(values, VALUE_TYPE);
    this.factoryMethod = assertNotNullValue(values, FACTORY_METHOD);
    this.accessorMethod = assertNotNullValue(values, ACCESSOR_METHOD);
    this.acceptNull = assertNotNullValue(values, ACCEPT_NULL);
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
