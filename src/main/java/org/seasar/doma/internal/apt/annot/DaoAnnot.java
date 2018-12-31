package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.AccessLevel;
import org.seasar.doma.Dao;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.jdbc.Config;

public class DaoAnnot {

  protected final AnnotationMirror annotationMirror;

  protected final Context ctx;

  protected AnnotationValue config;

  protected AnnotationValue accessLevel;

  protected TypeMirror configValue;

  protected DaoAnnot(AnnotationMirror annotationMirror, Context ctx) {
    assertNotNull(annotationMirror, ctx);
    this.annotationMirror = annotationMirror;
    this.ctx = ctx;
  }

  public static DaoAnnot newInstance(TypeElement interfase, Context ctx) {
    assertNotNull(ctx);
    AnnotationMirror annotationMirror = ctx.getElements().getAnnotationMirror(interfase, Dao.class);
    if (annotationMirror == null) {
      return null;
    }
    DaoAnnot result = new DaoAnnot(annotationMirror, ctx);
    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry :
        ctx.getElements().getElementValuesWithDefaults(annotationMirror).entrySet()) {
      String name = entry.getKey().getSimpleName().toString();
      AnnotationValue value = entry.getValue();
      if ("config".equals(name)) {
        result.config = value;
        result.configValue = AnnotationValueUtil.toType(value);
        if (result.configValue == null) {
          throw new AptIllegalStateException("config");
        }
      } else if ("accessLevel".equals(name)) {
        result.accessLevel = value;
      }
    }
    return result;
  }

  public AnnotationMirror getAnnotationMirror() {
    return annotationMirror;
  }

  public AnnotationValue getConfig() {
    return config;
  }

  public AnnotationValue getAccessLevel() {
    return accessLevel;
  }

  public TypeMirror getConfigValue() {
    TypeMirror value = AnnotationValueUtil.toType(config);
    if (value == null) {
      throw new AptIllegalStateException("config");
    }
    return value;
  }

  public AccessLevel getAccessLevelValue() {
    VariableElement enumConstant = AnnotationValueUtil.toEnumConstant(accessLevel);
    if (enumConstant == null) {
      throw new AptIllegalStateException("accessLevel");
    }
    return AccessLevel.valueOf(enumConstant.getSimpleName().toString());
  }

  public boolean hasUserDefinedConfig() {
    return !ctx.getTypes().isSameType(configValue, Config.class);
  }
}
