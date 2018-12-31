package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Map;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.Update;
import org.seasar.doma.internal.apt.util.ElementUtil;

public class UpdateAnnot extends ModifyAnnot {

  protected UpdateAnnot(AnnotationMirror annotationMirror) {
    super(annotationMirror);
  }

  public static UpdateAnnot newInstance(ExecutableElement method, ProcessingEnvironment env) {
    assertNotNull(env);
    AnnotationMirror annotationMirror = ElementUtil.getAnnotationMirror(method, Update.class, env);
    if (annotationMirror == null) {
      return null;
    }
    UpdateAnnot result = new UpdateAnnot(annotationMirror);
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
      } else if ("excludeNull".equals(name)) {
        result.excludeNull = value;
      } else if ("suppressOptimisticLockException".equals(name)) {
        result.suppressOptimisticLockException = value;
      } else if ("includeUnchanged".equals(name)) {
        result.includeUnchanged = value;
      } else if ("include".equals(name)) {
        result.include = value;
      } else if ("exclude".equals(name)) {
        result.exclude = value;
      } else if ("sqlLog".equals(name)) {
        result.sqlLog = value;
      }
    }
    return result;
  }
}
