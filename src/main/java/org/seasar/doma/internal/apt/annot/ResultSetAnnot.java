package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.ResultSet;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class ResultSetAnnot {

  protected final AnnotationMirror annotationMirror;

  protected AnnotationValue ensureResultMapping;

  protected ResultSetAnnot(AnnotationMirror annotationMirror) {
    this.annotationMirror = annotationMirror;
  }

  public static ResultSetAnnot newInstance(VariableElement param, Context ctx) {
    assertNotNull(ctx);
    AnnotationMirror annotationMirror =
        ctx.getElements().getAnnotationMirror(param, ResultSet.class);
    if (annotationMirror == null) {
      return null;
    }
    ResultSetAnnot result = new ResultSetAnnot(annotationMirror);
    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry :
        ctx.getElements().getElementValuesWithDefaults(annotationMirror).entrySet()) {
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
