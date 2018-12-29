package org.seasar.doma.internal.jdbc.entity;

import java.lang.reflect.Method;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.PostDeleteContext;

public class AbstractPostDeleteContext<E> extends AbstractEntityListenerContext<E>
    implements PostDeleteContext<E> {

  protected AbstractPostDeleteContext(EntityType<E> entityType, Method method, Config config) {
    super(entityType, method, config);
  }
}
