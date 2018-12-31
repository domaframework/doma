package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Map;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.internal.apt.util.ElementUtil;

public class ValueAnnot {

  protected final AnnotationMirror annotationMirror;

  protected AnnotationValue staticConstructor;

  protected ValueAnnot(AnnotationMirror annotationMirror) {
    assertNotNull(annotationMirror);
    this.annotationMirror = annotationMirror;
  }

  public static ValueAnnot newInstance(TypeElement typeElement, ProcessingEnvironment env) {
    assertNotNull(env);
    AnnotationMirror annotationMirror =
        ElementUtil.getAnnotationMirror(typeElement, Options.getLombokValue(env), env);
    if (annotationMirror == null) {
      return null;
    }
    ValueAnnot result = new ValueAnnot(annotationMirror);
    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry :
        env.getElementUtils().getElementValuesWithDefaults(annotationMirror).entrySet()) {
      String name = entry.getKey().getSimpleName().toString();
      AnnotationValue value = entry.getValue();
      if ("staticConstructor".equals(name)) {
        result.staticConstructor = value;
      }
    }
    return result;
  }

  public AnnotationMirror getAnnotationMirror() {
    return annotationMirror;
  }

  public AnnotationValue getStaticConstructor() {
    return staticConstructor;
  }

  public String getStaticConstructorValue() {
    String value = AnnotationValueUtil.toString(staticConstructor);
    if (value == null) {
      throw new AptIllegalStateException("staticConstructor");
    }
    return value;
  }
}
