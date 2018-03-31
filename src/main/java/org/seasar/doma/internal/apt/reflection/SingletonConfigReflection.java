package org.seasar.doma.internal.apt.reflection;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNullValue;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class SingletonConfigReflection extends AbstractReflection {

  public static final String METHOD = "method";

  private final AnnotationValue method;

  SingletonConfigReflection(
      AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    assertNotNull(values);
    this.method = assertNotNullValue(values, METHOD);
  }

  public AnnotationValue getMethod() {
    return method;
  }

  public String getMethodValue() {
    String methodName = AnnotationValueUtil.toString(method);
    if (methodName == null) {
      throw new AptIllegalStateException(METHOD);
    }
    return methodName;
  }
}
