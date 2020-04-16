package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.annot.ClobFactoryAnnot;

public class ClobCreateQueryMeta extends AbstractCreateQueryMeta {

  private ClobFactoryAnnot clobFactoryAnnot;

  public ClobCreateQueryMeta(TypeElement daoElement, ExecutableElement methodElement) {
    super(daoElement, methodElement);
  }

  public ClobFactoryAnnot getClobFactoryAnnot() {
    return clobFactoryAnnot;
  }

  void setClobFactoryAnnot(ClobFactoryAnnot clobFactoryAnnot) {
    this.clobFactoryAnnot = clobFactoryAnnot;
  }

  @Override
  public <R> R accept(QueryMetaVisitor<R> visitor) {
    return visitor.visitClobCreateQueryMeta(this);
  }
}
