package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.annot.NClobFactoryAnnot;

public class NClobCreateQueryMeta extends AbstractCreateQueryMeta {

  protected NClobFactoryAnnot nClobFactoryAnnot;

  public NClobCreateQueryMeta(ExecutableElement method, TypeElement dao) {
    super(method, dao);
  }

  NClobFactoryAnnot getNClobFactoryMirror() {
    return nClobFactoryAnnot;
  }

  void setNClobFactoryMirror(NClobFactoryAnnot nClobFactoryAnnot) {
    this.nClobFactoryAnnot = nClobFactoryAnnot;
  }
}
