package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.Map;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.ArrayFactory;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.internal.apt.util.ElementUtil;

public class ArrayFactoryAnnot {

  protected final AnnotationMirror annotationMirror;

  protected AnnotationValue typeName;

  protected ArrayFactoryAnnot(AnnotationMirror annotationMirror) {
    this.annotationMirror = annotationMirror;
  }

  public static ArrayFactoryAnnot newInstance(ExecutableElement method, ProcessingEnvironment env) {
    assertNotNull(env);
    AnnotationMirror annotationMirror =
        ElementUtil.getAnnotationMirror(method, ArrayFactory.class, env);
    if (annotationMirror == null) {
      return null;
    }
    ArrayFactoryAnnot result = new ArrayFactoryAnnot(annotationMirror);
    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry :
        env.getElementUtils().getElementValuesWithDefaults(annotationMirror).entrySet()) {
      String name = entry.getKey().getSimpleName().toString();
      AnnotationValue value = entry.getValue();
      if ("typeName".equals(name)) {
        result.typeName = value;
      }
    }
    return result;
  }

  public String getTypeNameValue() {
    String result = AnnotationValueUtil.toString(typeName);
    if (result == null) {
      throw new AptIllegalStateException("typeName");
    }
    return result;
  }
}
