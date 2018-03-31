package org.seasar.doma.internal.apt.reflection;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNullValue;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class ResultSetReflection extends AbstractReflection {

  public static final String ENSURE_RESULT_MAPPING = "ensureResultMapping";

  private final AnnotationValue ensureResultMapping;

  ResultSetReflection(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    assertNotNull(values);
    this.ensureResultMapping = assertNotNullValue(values, ENSURE_RESULT_MAPPING);
  }

  public AnnotationValue getEnsureResultMapping() {
    return ensureResultMapping;
  }

  public boolean getEnsureResultMappingValue() {
    Boolean value = AnnotationValueUtil.toBoolean(ensureResultMapping);
    if (value == null) {
      throw new AptIllegalStateException(ENSURE_RESULT_MAPPING);
    }
    return value;
  }
}
