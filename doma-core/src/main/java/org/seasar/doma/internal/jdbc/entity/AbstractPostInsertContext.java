package org.seasar.doma.internal.jdbc.entity;

import java.lang.reflect.Method;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.PostInsertContext;

public class AbstractPostInsertContext<E> extends AbstractEntityListenerContext<E>
    implements PostInsertContext<E> {

  protected AbstractPostInsertContext(EntityType<E> entityType, Method method, Config config) {
    super(entityType, method, config);
  }
}
