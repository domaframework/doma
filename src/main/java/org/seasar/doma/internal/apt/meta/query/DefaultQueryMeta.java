package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;

public class DefaultQueryMeta extends AbstractQueryMeta {

  public DefaultQueryMeta(ExecutableElement method) {
    super(method);
  }

  @Override
  public <P> void accept(QueryMetaVisitor<P> visitor, P p) {
    visitor.visitDefaultQueryMeta(this, p);
  }
}
