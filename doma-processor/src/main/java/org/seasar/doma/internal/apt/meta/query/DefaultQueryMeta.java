package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

public class DefaultQueryMeta extends AbstractQueryMeta {

  private boolean isVirtual;

  public DefaultQueryMeta(
      TypeElement daoElement, ExecutableElement methodElement, boolean isVirtual) {
    super(daoElement, methodElement);
    this.isVirtual = isVirtual;
  }

  @Override
  public <R> R accept(QueryMetaVisitor<R> visitor) {
    return visitor.visitDefaultQueryMeta(this);
  }

  public boolean isVirtual() {
    return isVirtual;
  }
}
