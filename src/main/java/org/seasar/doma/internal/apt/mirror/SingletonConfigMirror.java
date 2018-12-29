package org.seasar.doma.internal.apt.mirror;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Map;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.SingletonConfig;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.internal.apt.util.ElementUtil;

public class SingletonConfigMirror {

  protected final AnnotationMirror annotationMirror;

  protected final ProcessingEnvironment env;

  protected AnnotationValue method;

  protected SingletonConfigMirror(AnnotationMirror annotationMirror, ProcessingEnvironment env) {
    assertNotNull(annotationMirror, env);
    this.annotationMirror = annotationMirror;
    this.env = env;
  }

  public static SingletonConfigMirror newInstance(
      TypeElement typeElement, ProcessingEnvironment env) {
    assertNotNull(env);
    AnnotationMirror annotationMirror =
        ElementUtil.getAnnotationMirror(typeElement, SingletonConfig.class, env);
    if (annotationMirror == null) {
      return null;
    }
    SingletonConfigMirror result = new SingletonConfigMirror(annotationMirror, env);
    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry :
        env.getElementUtils().getElementValuesWithDefaults(annotationMirror).entrySet()) {
      String name = entry.getKey().getSimpleName().toString();
      AnnotationValue value = entry.getValue();
      if ("method".equals(name)) {
        result.method = value;
      }
    }
    return result;
  }

  public AnnotationMirror getAnnotationMirror() {
    return annotationMirror;
  }

  public AnnotationValue getMethod() {
    return method;
  }

  public String getMethodValue() {
    String methodName = AnnotationValueUtil.toString(method);
    if (methodName == null) {
      throw new AptIllegalStateException("method");
    }
    return methodName;
  }
}
