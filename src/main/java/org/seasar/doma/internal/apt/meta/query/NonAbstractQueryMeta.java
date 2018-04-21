package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;

public class NonAbstractQueryMeta extends AbstractQueryMeta {

  public NonAbstractQueryMeta(ExecutableElement method) {
    super(method);
  }

  @Override
  public <P> void accept(QueryMetaVisitor<P> visitor, P p) {
    visitor.visitNonAbstractQueryMeta(this, p);
  }
}
