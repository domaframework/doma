package org.seasar.doma.internal.apt.meta;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

public abstract class AbstractSqlFileQueryMeta extends AbstractQueryMeta {

  protected AbstractSqlFileQueryMeta(ExecutableElement method, TypeElement dao) {
    super(method, dao);
  }
}
