package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNullValue;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class ValueAnnot extends AbstractAnnot {

  public static final String STATIC_CONSTRUCTOR = "staticConstructor";

  private final AnnotationValue staticConstructor;

  ValueAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    assertNotNull(values);
    this.staticConstructor = assertNotNullValue(values, STATIC_CONSTRUCTOR);
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
