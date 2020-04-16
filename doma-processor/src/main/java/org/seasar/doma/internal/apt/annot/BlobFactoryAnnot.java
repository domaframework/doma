package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.lang.model.element.AnnotationMirror;

public class BlobFactoryAnnot extends AbstractAnnot {

  BlobFactoryAnnot(AnnotationMirror annotationMirror) {
    super(annotationMirror);
  }
}
