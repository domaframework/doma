package org.seasar.doma.internal.apt.meta;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

/** @author taedium */
public class DefaultQueryMeta extends AbstractQueryMeta {

  public DefaultQueryMeta(ExecutableElement method, TypeElement dao) {
    super(method, dao);
  }

  @Override
  public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
    return visitor.visitDefaultQueryMeta(this, p);
  }
}
