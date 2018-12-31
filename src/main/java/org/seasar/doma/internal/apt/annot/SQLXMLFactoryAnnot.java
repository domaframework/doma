package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.SQLXMLFactory;
import org.seasar.doma.internal.apt.util.ElementUtil;

public class SQLXMLFactoryAnnot {

  protected final AnnotationMirror annotationMirror;

  protected SQLXMLFactoryAnnot(AnnotationMirror annotationMirror) {
    this.annotationMirror = annotationMirror;
  }

  public static SQLXMLFactoryAnnot newInstance(
      ExecutableElement method, ProcessingEnvironment env) {
    assertNotNull(env);
    AnnotationMirror annotationMirror =
        ElementUtil.getAnnotationMirror(method, SQLXMLFactory.class, env);
    if (annotationMirror == null) {
      return null;
    }
    return new SQLXMLFactoryAnnot(annotationMirror);
  }
}
