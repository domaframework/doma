package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.annot.BlobFactoryAnnot;

public class BlobCreateQueryMeta extends AbstractCreateQueryMeta {

  protected BlobFactoryAnnot blobFactoryAnnot;

  public BlobCreateQueryMeta(ExecutableElement method, TypeElement dao) {
    super(method, dao);
  }

  BlobFactoryAnnot getBlobFactoryAnnot() {
    return blobFactoryAnnot;
  }

  void setBlobFactoryAnnot(BlobFactoryAnnot blobFactoryAnnot) {
    this.blobFactoryAnnot = blobFactoryAnnot;
  }
}
