package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.annot.BlobFactoryAnnot;

public class BlobCreateQueryMeta extends AbstractCreateQueryMeta {

  private BlobFactoryAnnot blobFactoryAnnot;

  public BlobCreateQueryMeta(ExecutableElement method) {
    super(method);
  }

  public BlobFactoryAnnot getBlobFactoryAnnot() {
    return blobFactoryAnnot;
  }

  public void setBlobFactoryAnnot(BlobFactoryAnnot blobFactoryAnnot) {
    this.blobFactoryAnnot = blobFactoryAnnot;
  }
}
