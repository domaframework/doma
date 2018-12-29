package org.seasar.doma.internal.apt.mirror;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.NClobFactory;
import org.seasar.doma.internal.apt.util.ElementUtil;

/** @author taedium */
public class NClobFactoryMirror {

  protected final AnnotationMirror annotationMirror;

  protected NClobFactoryMirror(AnnotationMirror annotationMirror) {
    this.annotationMirror = annotationMirror;
  }

  public static NClobFactoryMirror newInstance(
      ExecutableElement method, ProcessingEnvironment env) {
    assertNotNull(env);
    AnnotationMirror annotationMirror =
        ElementUtil.getAnnotationMirror(method, NClobFactory.class, env);
    if (annotationMirror == null) {
      return null;
    }
    return new NClobFactoryMirror(annotationMirror);
  }
}
