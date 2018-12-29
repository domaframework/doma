package org.seasar.doma.internal.apt.mirror;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Map;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.internal.apt.util.ElementUtil;

public class AllArgsConstructorMirror {

  protected final AnnotationMirror annotationMirror;

  protected AnnotationValue staticName;

  protected AnnotationValue access;

  protected AllArgsConstructorMirror(AnnotationMirror annotationMirror) {
    assertNotNull(annotationMirror);
    this.annotationMirror = annotationMirror;
  }

  public static AllArgsConstructorMirror newInstance(
      TypeElement typeElement, ProcessingEnvironment env) {
    assertNotNull(env);
    AnnotationMirror annotationMirror =
        ElementUtil.getAnnotationMirror(typeElement, Options.getLombokAllArgsConstructor(env), env);
    if (annotationMirror == null) {
      return null;
    }
    AllArgsConstructorMirror result = new AllArgsConstructorMirror(annotationMirror);
    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry :
        env.getElementUtils().getElementValuesWithDefaults(annotationMirror).entrySet()) {
      String name = entry.getKey().getSimpleName().toString();
      AnnotationValue value = entry.getValue();
      if ("staticName".equals(name)) {
        result.staticName = value;
      } else if ("access".equals(name)) {
        result.access = value;
      }
    }
    return result;
  }

  public AnnotationMirror getAnnotationMirror() {
    return annotationMirror;
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
      throw new AptIllegalStateException("staticConstructor");
    }
    return value;
  }

  public boolean isAccessPrivate() {
    VariableElement enumConstant = AnnotationValueUtil.toEnumConstant(access);
    if (enumConstant == null) {
      throw new AptIllegalStateException("access");
    }
    return "PRIVATE".equals(enumConstant.getSimpleName().toString());
  }

  public boolean isAccessNone() {
    VariableElement enumConstant = AnnotationValueUtil.toEnumConstant(access);
    if (enumConstant == null) {
      throw new AptIllegalStateException("access");
    }
    return "NONE".equals(enumConstant.getSimpleName().toString());
  }
}
