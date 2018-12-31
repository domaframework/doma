package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.BlobFactory;
import org.seasar.doma.internal.apt.util.ElementUtil;

public class BlobFactoryAnnot {

  protected final AnnotationMirror annotationMirror;

  protected BlobFactoryAnnot(AnnotationMirror annotationMirror) {
    this.annotationMirror = annotationMirror;
  }

  public static BlobFactoryAnnot newInstance(ExecutableElement method, ProcessingEnvironment env) {
    assertNotNull(env);
    AnnotationMirror annotationMirror =
        ElementUtil.getAnnotationMirror(method, BlobFactory.class, env);
    if (annotationMirror == null) {
      return null;
    }
    return new BlobFactoryAnnot(annotationMirror);
  }
}
