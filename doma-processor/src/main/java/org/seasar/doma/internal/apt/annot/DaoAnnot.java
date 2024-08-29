package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNonNullValue;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.AccessLevel;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class DaoAnnot extends AbstractAnnot {

  private static final String ACCESS_LEVEL = "accessLevel";

  private final AnnotationValue accessLevel;

  DaoAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    this.accessLevel = assertNonNullValue(values, ACCESS_LEVEL);
  }

  public AnnotationValue getAccessLevel() {
    return accessLevel;
  }

  public AccessLevel getAccessLevelValue() {
    VariableElement enumConstant = AnnotationValueUtil.toEnumConstant(accessLevel);
    if (enumConstant == null) {
      throw new AptIllegalStateException(ACCESS_LEVEL);
    }
    return AccessLevel.valueOf(enumConstant.getSimpleName().toString());
  }
}
