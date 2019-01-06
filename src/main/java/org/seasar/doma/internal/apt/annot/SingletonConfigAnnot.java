package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNonNullValue;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class SingletonConfigAnnot extends AbstractAnnot {

  private static final String METHOD = "method";

  private final AnnotationValue method;

  SingletonConfigAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    this.method = assertNonNullValue(values, METHOD);
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
