package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.annot.NClobFactoryAnnot;

public class NClobCreateQueryMeta extends AbstractCreateQueryMeta {

  private NClobFactoryAnnot nClobFactoryAnnot;

  public NClobCreateQueryMeta(ExecutableElement method, TypeElement dao) {
    super(method, dao);
  }

  public NClobFactoryAnnot getNClobFactoryAnnot() {
    return nClobFactoryAnnot;
  }

  void setNClobFactoryMirror(NClobFactoryAnnot nClobFactoryAnnot) {
    this.nClobFactoryAnnot = nClobFactoryAnnot;
  }

  @Override
  public <R> R accept(QueryMetaVisitor<R> visitor) {
    return visitor.visitNClobCreateQueryMeta(this);
  }
}
