package org.seasar.doma.internal.jdbc.entity;

import java.lang.reflect.Method;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.PreDeleteContext;

public class AbstractPreDeleteContext<E> extends AbstractEntityListenerContext<E>
    implements PreDeleteContext<E> {

  protected AbstractPreDeleteContext(EntityType<E> entityType, Method method, Config config) {
    super(entityType, method, config);
  }
}
