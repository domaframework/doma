package org.seasar.doma.internal.apt.meta;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.mirror.BlobFactoryMirror;

public class BlobCreateQueryMeta extends AbstractCreateQueryMeta {

  protected BlobFactoryMirror blobFactoryMirror;

  public BlobCreateQueryMeta(ExecutableElement method, TypeElement dao) {
    super(method, dao);
  }

  BlobFactoryMirror getBlobFactoryMirror() {
    return blobFactoryMirror;
  }

  void setBlobFactoryMirror(BlobFactoryMirror blobFactoryMirror) {
    this.blobFactoryMirror = blobFactoryMirror;
  }
}
