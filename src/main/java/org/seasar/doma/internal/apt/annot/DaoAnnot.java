package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNullValue;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.AccessLevel;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class DaoAnnot extends AbstractAnnot {

  public static final String ACCESS_LEVEL = "accessLevel";

  public static final String CONFIG = "config";

  private final AnnotationValue config;

  private final AnnotationValue accessLevel;

  private final boolean hasUserDefinedConfig;

  DaoAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    assertNotNull(values);
    this.config = assertNotNullValue(values, CONFIG);
    this.accessLevel = assertNotNullValue(values, ACCESS_LEVEL);
    this.hasUserDefinedConfig =
        annotationMirror
            .getElementValues()
            .keySet()
            .stream()
            .anyMatch(e -> e.getSimpleName().contentEquals(CONFIG));
  }

  public AnnotationValue getConfig() {
    return config;
  }

  public AnnotationValue getAccessLevel() {
    return accessLevel;
  }

  public TypeMirror getConfigValue() {
    var value = AnnotationValueUtil.toType(config);
    if (value == null) {
      throw new AptIllegalStateException(CONFIG);
    }
    return value;
  }

  public AccessLevel getAccessLevelValue() {
    var enumConstant = AnnotationValueUtil.toEnumConstant(accessLevel);
    if (enumConstant == null) {
      throw new AptIllegalStateException(ACCESS_LEVEL);
    }
    return AccessLevel.valueOf(enumConstant.getSimpleName().toString());
  }

  public boolean hasUserDefinedConfig() {
    return hasUserDefinedConfig;
  }
}
