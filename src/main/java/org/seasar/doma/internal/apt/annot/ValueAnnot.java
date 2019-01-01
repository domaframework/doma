package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNonNullValue;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class ValueAnnot extends AbstractAnnot {

  private static final String STATIC_CONSTRUCTOR = "staticConstructor";

  private final AnnotationValue staticConstructor;

  ValueAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    this.staticConstructor = assertNonNullValue(values, STATIC_CONSTRUCTOR);
  }

  public AnnotationValue getStaticConstructor() {
    return staticConstructor;
  }

  public String getStaticConstructorValue() {
    String value = AnnotationValueUtil.toString(staticConstructor);
    if (value == null) {
      throw new AptIllegalStateException(STATIC_CONSTRUCTOR);
    }
    return value;
  }
}
