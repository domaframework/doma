package org.seasar.doma.internal.apt.mirror;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.ClobFactory;
import org.seasar.doma.internal.apt.util.ElementUtil;

/** @author taedium */
public class ClobFactoryMirror {

  protected final AnnotationMirror annotationMirror;

  protected ClobFactoryMirror(AnnotationMirror annotationMirror) {
    this.annotationMirror = annotationMirror;
  }

  public static ClobFactoryMirror newInstance(ExecutableElement method, ProcessingEnvironment env) {
    assertNotNull(env);
    AnnotationMirror annotationMirror =
        ElementUtil.getAnnotationMirror(method, ClobFactory.class, env);
    if (annotationMirror == null) {
      return null;
    }
    return new ClobFactoryMirror(annotationMirror);
  }
}
