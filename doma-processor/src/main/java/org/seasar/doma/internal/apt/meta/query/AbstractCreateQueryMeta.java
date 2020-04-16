package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

public abstract class AbstractCreateQueryMeta extends AbstractQueryMeta {

  AbstractCreateQueryMeta(TypeElement daoElement, ExecutableElement methodElement) {
    super(daoElement, methodElement);
  }
}
