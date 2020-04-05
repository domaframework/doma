package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class ResultSetAnnot extends AbstractAnnot {

  private static final String ENSURE_RESULT_MAPPING = "ensureResultMapping";

  private final AnnotationValue ensureResultMapping;

  ResultSetAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    this.ensureResultMapping = assertNonNullValue(values, ENSURE_RESULT_MAPPING);
  }

  public AnnotationValue getEnsureResultMapping() {
    return ensureResultMapping;
  }

  public boolean getEnsureResultMappingValue() {
    Boolean value = AnnotationValueUtil.toBoolean(ensureResultMapping);
    if (value == null) {
      throw new AptIllegalStateException(ENSURE_RESULT_MAPPING);
    }
    return value.booleanValue();
  }
}
