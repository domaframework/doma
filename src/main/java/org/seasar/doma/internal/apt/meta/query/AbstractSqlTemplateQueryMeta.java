package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;

public abstract class AbstractSqlTemplateQueryMeta extends AbstractQueryMeta {

  protected AbstractSqlTemplateQueryMeta(ExecutableElement method) {
    super(method);
  }

  public boolean isExpandable() {
    return false;
  }

  public boolean isPopulatable() {
    return false;
  }
}
