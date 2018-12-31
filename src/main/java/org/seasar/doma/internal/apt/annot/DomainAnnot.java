package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.Domain;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class DomainAnnot {

  protected final AnnotationMirror annotationMirror;

  protected AnnotationValue valueType;

  protected AnnotationValue factoryMethod;

  protected AnnotationValue accessorMethod;

  protected AnnotationValue acceptNull;

  protected DomainAnnot(AnnotationMirror annotationMirror) {
    assertNotNull(annotationMirror);
    this.annotationMirror = annotationMirror;
  }

  public static DomainAnnot newInstance(TypeElement clazz, Context ctx) {
    assertNotNull(ctx);
    AnnotationMirror annotationMirror = ctx.getElements().getAnnotationMirror(clazz, Domain.class);
    if (annotationMirror == null) {
      return null;
    }
    DomainAnnot result = new DomainAnnot(annotationMirror);
    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry :
        ctx.getElements().getElementValuesWithDefaults(annotationMirror).entrySet()) {
      String name = entry.getKey().getSimpleName().toString();
      AnnotationValue value = entry.getValue();
      if ("valueType".equals(name)) {
        result.valueType = value;
      } else if ("factoryMethod".equals(name)) {
        result.factoryMethod = value;
      } else if ("accessorMethod".equals(name)) {
        result.accessorMethod = value;
      } else if ("acceptNull".equals(name)) {
        result.acceptNull = value;
      }
    }
    return result;
  }

  public AnnotationMirror getAnnotationMirror() {
    return annotationMirror;
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
      throw new AptIllegalStateException("valueType");
    }
    return value;
  }

  public String getFactoryMethodValue() {
    String value = AnnotationValueUtil.toString(factoryMethod);
    if (value == null) {
      throw new AptIllegalStateException("factoryMethod");
    }
    return value;
  }

  public String getAccessorMethodValue() {
    String value = AnnotationValueUtil.toString(accessorMethod);
    if (value == null) {
      throw new AptIllegalStateException("accessorMethod");
    }
    return value;
  }

  public boolean getAcceptNullValue() {
    Boolean value = AnnotationValueUtil.toBoolean(acceptNull);
    if (value == null) {
      throw new AptIllegalStateException("acceptNull");
    }
    return value.booleanValue();
  }
}