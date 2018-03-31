package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.annot.NClobFactoryAnnot;

public class NClobCreateQueryMeta extends AbstractCreateQueryMeta {

  private NClobFactoryAnnot nClobFactoryAnnot;

  public NClobCreateQueryMeta(ExecutableElement method) {
    super(method);
  }

  public NClobFactoryAnnot getNClobFactoryAnnot() {
    return nClobFactoryAnnot;
  }

  public void setNClobFactoryAnnot(NClobFactoryAnnot nClobFactoryAnnot) {
    this.nClobFactoryAnnot = nClobFactoryAnnot;
  }
}
