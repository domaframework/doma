package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.SQLXMLFactory;
import org.seasar.doma.internal.apt.Context;

public class SQLXMLFactoryAnnot {

  protected final AnnotationMirror annotationMirror;

  protected SQLXMLFactoryAnnot(AnnotationMirror annotationMirror) {
    this.annotationMirror = annotationMirror;
  }

  public static SQLXMLFactoryAnnot newInstance(ExecutableElement method, Context ctx) {
    assertNotNull(ctx);
    AnnotationMirror annotationMirror =
        ctx.getElements().getAnnotationMirror(method, SQLXMLFactory.class);
    if (annotationMirror == null) {
      return null;
    }
    return new SQLXMLFactoryAnnot(annotationMirror);
  }
}
