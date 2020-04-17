package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.annot.BlobFactoryAnnot;

public class BlobCreateQueryMeta extends AbstractCreateQueryMeta {

  private BlobFactoryAnnot blobFactoryAnnot;

  public BlobCreateQueryMeta(TypeElement daoElement, ExecutableElement methodElement) {
    super(daoElement, methodElement);
  }

  public BlobFactoryAnnot getBlobFactoryAnnot() {
    return blobFactoryAnnot;
  }

  void setBlobFactoryAnnot(BlobFactoryAnnot blobFactoryAnnot) {
    this.blobFactoryAnnot = blobFactoryAnnot;
  }

  @Override
  public <R> R accept(QueryMetaVisitor<R> visitor) {
    return visitor.visitBlobCreateQueryMeta(this);
  }
}
