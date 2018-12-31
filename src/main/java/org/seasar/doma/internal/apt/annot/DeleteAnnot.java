package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Map;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.Delete;
import org.seasar.doma.internal.apt.util.ElementUtil;

public class DeleteAnnot extends ModifyAnnot {

  protected DeleteAnnot(AnnotationMirror annotationMirror) {
    super(annotationMirror);
  }

  public static DeleteAnnot newInstance(ExecutableElement method, ProcessingEnvironment env) {
    assertNotNull(env);
    AnnotationMirror annotationMirror = ElementUtil.getAnnotationMirror(method, Delete.class, env);
    if (annotationMirror == null) {
      return null;
    }
    DeleteAnnot result = new DeleteAnnot(annotationMirror);
    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry :
        env.getElementUtils().getElementValuesWithDefaults(annotationMirror).entrySet()) {
      String name = entry.getKey().getSimpleName().toString();
      AnnotationValue value = entry.getValue();
      if ("sqlFile".equals(name)) {
        result.sqlFile = value;
      } else if ("queryTimeout".equals(name)) {
        result.queryTimeout = value;
      } else if ("ignoreVersion".equals(name)) {
        result.ignoreVersion = value;
      } else if ("suppressOptimisticLockException".equals(name)) {
        result.suppressOptimisticLockException = value;
      } else if ("sqlLog".equals(name)) {
        result.sqlLog = value;
      }
    }
    return result;
  }
}
