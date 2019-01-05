package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.annot.ClobFactoryAnnot;

public class ClobCreateQueryMeta extends AbstractCreateQueryMeta {

  private ClobFactoryAnnot clobFactoryAnnot;

  public ClobCreateQueryMeta(ExecutableElement method, TypeElement dao) {
    super(method, dao);
  }

  ClobFactoryAnnot getClobFactoryAnnot() {
    return clobFactoryAnnot;
  }

  void setClobFactoryAnnot(ClobFactoryAnnot clobFactoryAnnot) {
    this.clobFactoryAnnot = clobFactoryAnnot;
  }
}
