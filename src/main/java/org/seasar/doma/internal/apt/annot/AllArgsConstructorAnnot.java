package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNonNullValue;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class AllArgsConstructorAnnot extends AbstractAnnot {

  private static final String STATIC_NAME = "staticName";

  private static final String ACCESS = "access";

  private final AnnotationValue staticName;

  private final AnnotationValue access;

  AllArgsConstructorAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    this.staticName = assertNonNullValue(values, STATIC_NAME);
    this.access = assertNonNullValue(values, ACCESS);
  }

  public AnnotationValue getStaticName() {
    return staticName;
  }

  public AnnotationValue getAccess() {
    return access;
  }

  public String getStaticNameValue() {
    String value = AnnotationValueUtil.toString(staticName);
    if (value == null) {
      throw new AptIllegalStateException(STATIC_NAME);
    }
    return value;
  }

  public boolean isAccessPrivate() {
    VariableElement enumConstant = AnnotationValueUtil.toEnumConstant(access);
    if (enumConstant == null) {
      throw new AptIllegalStateException(ACCESS);
    }
    return "PRIVATE".equals(enumConstant.getSimpleName().toString());
  }

  public boolean isAccessNone() {
    VariableElement enumConstant = AnnotationValueUtil.toEnumConstant(access);
    if (enumConstant == null) {
      throw new AptIllegalStateException(ACCESS);
    }
    return "NONE".equals(enumConstant.getSimpleName().toString());
  }
}
