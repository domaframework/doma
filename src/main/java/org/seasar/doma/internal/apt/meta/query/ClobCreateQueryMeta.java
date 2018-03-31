package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.annot.ClobFactoryAnnot;

public class ClobCreateQueryMeta extends AbstractCreateQueryMeta {

  private ClobFactoryAnnot clobFactoryAnnot;

  public ClobCreateQueryMeta(ExecutableElement method) {
    super(method);
  }

  public ClobFactoryAnnot getClobFactoryAnnot() {
    return clobFactoryAnnot;
  }

  public void setClobFactoryAnnot(ClobFactoryAnnot clobFactoryAnnot) {
    this.clobFactoryAnnot = clobFactoryAnnot;
  }
}
