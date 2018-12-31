package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.SingletonConfig;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class SingletonConfigAnnot {

  protected final AnnotationMirror annotationMirror;

  protected final Context ctx;

  protected AnnotationValue method;

  protected SingletonConfigAnnot(AnnotationMirror annotationMirror, Context ctx) {
    assertNotNull(annotationMirror, ctx);
    this.annotationMirror = annotationMirror;
    this.ctx = ctx;
  }

  public static SingletonConfigAnnot newInstance(TypeElement typeElement, Context ctx) {
    assertNotNull(ctx);
    AnnotationMirror annotationMirror =
        ctx.getElements().getAnnotationMirror(typeElement, SingletonConfig.class);
    if (annotationMirror == null) {
      return null;
    }
    SingletonConfigAnnot result = new SingletonConfigAnnot(annotationMirror, ctx);
    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry :
        ctx.getElements().getElementValuesWithDefaults(annotationMirror).entrySet()) {
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
