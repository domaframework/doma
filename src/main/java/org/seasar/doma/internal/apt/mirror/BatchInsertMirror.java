package org.seasar.doma.internal.apt.mirror;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Map;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.internal.apt.util.ElementUtil;

public class BatchInsertMirror extends BatchModifyMirror {

  protected BatchInsertMirror(AnnotationMirror annotationMirror) {
    super(annotationMirror);
  }

  public static BatchInsertMirror newInstance(ExecutableElement method, ProcessingEnvironment env) {
    assertNotNull(env);
    AnnotationMirror annotationMirror =
        ElementUtil.getAnnotationMirror(method, BatchInsert.class, env);
    if (annotationMirror == null) {
      return null;
    }
    BatchInsertMirror result = new BatchInsertMirror(annotationMirror);
    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry :
        env.getElementUtils().getElementValuesWithDefaults(annotationMirror).entrySet()) {
      String name = entry.getKey().getSimpleName().toString();
      AnnotationValue value = entry.getValue();
      if ("sqlFile".equals(name)) {
        result.sqlFile = value;
      } else if ("queryTimeout".equals(name)) {
        result.queryTimeout = value;
      } else if ("batchSize".equals(name)) {
        result.batchSize = value;
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
