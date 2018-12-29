package org.seasar.doma.internal.apt.mirror;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.Map;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.ResultSet;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.internal.apt.util.ElementUtil;

public class ResultSetMirror {

  protected final AnnotationMirror annotationMirror;

  protected AnnotationValue ensureResultMapping;

  protected ResultSetMirror(AnnotationMirror annotationMirror) {
    this.annotationMirror = annotationMirror;
  }

  public static ResultSetMirror newInstance(VariableElement param, ProcessingEnvironment env) {
    assertNotNull(env);
    AnnotationMirror annotationMirror =
        ElementUtil.getAnnotationMirror(param, ResultSet.class, env);
    if (annotationMirror == null) {
      return null;
    }
    ResultSetMirror result = new ResultSetMirror(annotationMirror);
    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry :
        env.getElementUtils().getElementValuesWithDefaults(annotationMirror).entrySet()) {
      String name = entry.getKey().getSimpleName().toString();
      AnnotationValue value = entry.getValue();
      if ("ensureResultMapping".equals(name)) {
        result.ensureResultMapping = value;
      }
    }
    return result;
  }

  public AnnotationValue getEnsureResultMapping() {
    return ensureResultMapping;
  }

  public boolean getEnsureResultMappingValue() {
    Boolean value = AnnotationValueUtil.toBoolean(ensureResultMapping);
    if (value == null) {
      throw new AptIllegalStateException("ensureResultMapping");
    }
    return value.booleanValue();
  }
}
